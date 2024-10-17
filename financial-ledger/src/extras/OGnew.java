import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class GitMavenAutomation {

    private static final String REPOS_DIR = "C:\\";
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> failedRepos = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_RETRIES = 5;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final String FAILED_REPOS_CSV = "failed_repos.csv";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify the branch to checkout.");
            return;
        }

        String branch = args[0];
        File reposDir = new File(REPOS_DIR);

        if (!reposDir.exists() || !reposDir.isDirectory()) {
            System.out.println("Invalid repository directory: " + REPOS_DIR);
            return;
        }

        File[] repos = reposDir.listFiles(File::isDirectory);
        if (repos != null) {
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            // Submit each repo for processing in a separate thread
            for (File repo : repos) {
                if (new File(repo, ".git").exists()) {
                    executor.submit(() -> processRepo(repo, branch));
                }
            }

            executor.shutdown(); // No more tasks will be submitted
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Executor interrupted: " + e.getMessage());
            }
        }

        // Write failed repositories to CSV file
        writeFailedReposToCSV();

        // Display the failed repositories at the end
        System.out.println("\nFailed repositories:");
        failedRepos.forEach(System.out::println);
    }

    // Function to process each repository (checkout, pull if needed, and build)
    private static void processRepo(File repo, String branch) {
        System.out.println("\nProcessing repository: " + repo.getName());

        String branchToUse = checkoutBranch(repo, branch);
        if (branchToUse != null) {
            boolean changesPulled = pullBranchIfNeeded(repo, branchToUse);
            if (changesPulled) {
                buildMavenProjectWithRetry(repo); // Only build if changes were pulled
            } else {
                System.out.println("Skipping Maven build for " + repo.getName() + " as there are no changes.");
            }
        }
    }

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

        System.out.println("No remote changes for branch: " + branch);
        return false;
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

    private static void buildMavenProjectWithRetry(File repoDir) {
        String mavenExecutable = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";
        boolean buildSuccess = false;
        int attempts = 0;

        while (!buildSuccess && attempts < MAX_RETRIES) {
            System.out.println("Building Maven project in: " + repoDir.getName() + " (Attempt " + (attempts + 1) + "/" + MAX_RETRIES + ")");
            String[] buildCmd = {mavenExecutable, "clean", "install", "-T", String.valueOf(THREAD_POOL_SIZE)};
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
            System.out.println("Skipping repository: " + repoDir.getName());
        }
    }

    private static int runCommand(String[] command, File workingDir) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            printProcessOutput(process.getInputStream());
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running command: " + String.join(" ", command));
            e.printStackTrace();
            return -1;
        }
    }

    private static List<String> runCommandAndCaptureOutput(String[] command, File workingDir) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        builder.redirectErrorStream(true);

        List<String> output = new ArrayList<>();
        try {
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static void printProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    // Method to write failed repositories to CSV file
    private static void writeFailedReposToCSV() {
        File csvFile = new File(FAILED_REPOS_CSV);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("Failed Repositories\n");
            for (String repo : failedRepos) {
                writer.write(repo + "\n");
            }
            System.out.println("\nFailed repositories written to " + FAILED_REPOS_CSV);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
