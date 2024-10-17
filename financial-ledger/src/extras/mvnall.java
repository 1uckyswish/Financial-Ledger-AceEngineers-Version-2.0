import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class MvnAll {

    private static final String REPOS_DIR = "C:\\";
    private static final String CSV_FILE = "failed_repos.csv";
    private static final List<String> failedRepos = Collections.synchronizedList(new ArrayList<>());
    private static final List<String> reposWithChanges = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_RETRIES = 5;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final String MAVEN_EXECUTABLE = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Select an operation:\n1. Pull and build from a specific branch\n2. Perform 'mvn clean install' from CSV");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        switch (choice) {
            case 1:
                System.out.println("Please specify the branch to checkout:");
                String branch = scanner.nextLine();
                processRepositories(branch);
                break;
            case 2:
                processRepositoriesFromCSV();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Process repositories by checking out and pulling from a specific branch
    private static void processRepositories(String branch) {
        File reposDir = new File(REPOS_DIR);

        if (!reposDir.exists() || !reposDir.isDirectory()) {
            System.out.println("Invalid repository directory: " + REPOS_DIR);
            return;
        }

        File[] repos = reposDir.listFiles(File::isDirectory);
        if (repos != null) {
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            for (File repo : repos) {
                if (new File(repo, ".git").exists()) {
                    executor.submit(() -> processRepo(repo, branch));
                }
            }

            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Executor interrupted: " + e.getMessage());
            }
        }

        // Print the repositories that had changes
        System.out.println("\nRepositories with changes:");
        reposWithChanges.forEach(System.out::println);

        // Display failed repositories
        if (!failedRepos.isEmpty()) {
            writeReposToCSV(failedRepos);
            promptForRebuild();
        }
    }

    // Process repositories from the CSV
    private static void processRepositoriesFromCSV() {
        List<String> repos = readReposFromCSV();
        if (repos.isEmpty()) {
            System.out.println("No repositories found in CSV.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (String repoName : repos) {
            File repoDir = new File(REPOS_DIR + repoName);
            if (repoDir.exists() && new File(repoDir, ".git").exists()) {
                executor.submit(() -> processRepo(repoDir, null));
            } else {
                System.out.println("Repository not found: " + repoName);
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println("Executor interrupted: " + e.getMessage());
        }

        if (!failedRepos.isEmpty()) {
            promptForRebuild();
        }
    }

    // Process each repository, including Git pull and Maven build
    private static void processRepo(File repo, String branch) {
        System.out.println("\nProcessing repository: " + repo.getName());

        if (branch != null) {
            String branchToUse = checkoutBranch(repo, branch);
            if (branchToUse != null) {
                boolean changesPulled = pullBranchIfNeeded(repo, branchToUse);
                if (changesPulled) {
                    reposWithChanges.add(repo.getName());
                    buildMavenProjectWithRetry(repo);
                } else {
                    System.out.println("Skipping Maven build for " + repo.getName() + " as there are no changes.");
                }
            }
        } else {
            buildMavenProjectWithRetry(repo);
        }
    }

    // Git checkout and pull logic
    private static String checkoutBranch(File repoDir, String branch) {
        System.out.println("Attempting to checkout branch: " + branch);
        String[] checkoutCmd = {"git", "checkout", branch};

        if (runCommand(checkoutCmd, repoDir) != 0) {
            System.out.println("Branch " + branch + " not found, checking out 'master'.");
            String[] checkoutMasterCmd = {"git", "checkout", "master"};
            if (runCommand(checkoutMasterCmd, repoDir) != 0) {
                System.out.println("Failed to checkout 'master' branch.");
                return null;
            }
            return "master";
        }
        return branch;
    }

    private static boolean pullBranchIfNeeded(File repoDir, String branch) {
        if (hasRemoteChanges(repoDir, branch)) {
            System.out.println("Pulling latest changes for branch: " + branch);
            String[] pullCmd = {"git", "pull"};
            runCommand(pullCmd, repoDir);
            return true;
        } else {
            System.out.println("Skipping pull, no changes detected.");
            return false;
        }
    }

    private static boolean hasRemoteChanges(File repoDir, String branch) {
        System.out.println("Checking for remote changes on branch: " + branch);
        String[] fetchCmd = {"git", "fetch"};
        runCommand(fetchCmd, repoDir);

        String[] checkCmd = {"git", "status", "-uno"};
        List<String> output = runCommandAndCaptureOutput(checkCmd, repoDir);
        for (String line : output) {
            if (line.contains("Your branch is behind")) {
                System.out.println("Remote changes found for branch: " + branch);
                return true;
            }
        }
        return false;
    }

    // Maven build logic with retry
    private static void buildMavenProjectWithRetry(File repoDir) {
        boolean buildSuccess = false;
        int attempts = 0;

        while (!buildSuccess && attempts < MAX_RETRIES) {
            System.out.println("Building Maven project in: " + repoDir.getName() + " (Attempt " + (attempts + 1) + "/" + MAX_RETRIES + ")");
            String[] buildCmd = {MAVEN_EXECUTABLE, "clean", "install", "-T", String.valueOf(THREAD_POOL_SIZE)};
            int result = runCommand(buildCmd, repoDir);

            if (result == 0) {
                buildSuccess = true;
                System.out.println("Build successful.");
            } else {
                attempts++;
                System.out.println("Build failed for " + repoDir.getName() + ". Retry " + attempts + "/" + MAX_RETRIES);
            }
        }

        if (!buildSuccess) {
            failedRepos.add(repoDir.getName());
        }
    }

    // Prompt user to rebuild failed repositories
    private static void promptForRebuild() {
        System.out.println("The following repositories failed to build:");
        failedRepos.forEach(System.out::println);

        System.out.println("Would you like to retry building the failed repositories? (yes/no)");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            retryFailedRepos();
        }
    }

    // Retry building failed repositories
    private static void retryFailedRepos() {
        failedRepos.clear(); // Clear the failedRepos list before retrying
        processRepositoriesFromCSV(); // Retry the repositories listed in the CSV file
    }

    // Read repository names from CSV file
    private static List<String> readReposFromCSV() {
        List<String> repos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                repos.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return repos;
    }

    // Write the failed repository list to a CSV file
    private static void writeReposToCSV(List<String> repos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (String repo : repos) {
                writer.write(repo);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Run a shell command in the given directory
    private static int runCommand(String[] command, File workingDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir);
            pb.inheritIO();
            Process process = pb.start();
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running command: " + Arrays.toString(command) + " in directory: " + workingDir.getPath());
            return -1;
        }
    }

    // Run a shell command and capture its output
    private static List<String> runCommandAndCaptureOutput(String[] command, File workingDir) {
        List<String> output = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error capturing output for command: " + Arrays.toString(command));
        }
        return output;
    }
}
