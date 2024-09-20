import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*; // For multithreading

public class GitMavenAutomation {

    private static final String REPOS_DIR = "C:\\";
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> failedRepos = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_RETRIES = 5;
    private static final int THREAD_POOL_SIZE = 5; // Adjust this based on your system's capabilities

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

        // Display the failed repositories at the end
        System.out.println("\nFailed repositories:");
        failedRepos.forEach(System.out::println);
    }

    // Function to process each repository (pull branch and build)
    private static void processRepo(File repo, String branch) {
        System.out.println("\nProcessing repository: " + repo.getName());

        String branchToUse = checkoutBranch(repo, branch);
        if (branchToUse != null) {
            pullBranch(repo, branchToUse);
            buildMavenProjectWithRetry(repo);
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

    private static void pullBranch(File repoDir, String branch) {
        System.out.println("Pulling latest changes for branch: " + branch);
        String[] pullCmd = {"git", "pull"};
        runCommand(pullCmd, repoDir);
    }

    private static void buildMavenProjectWithRetry(File repoDir) {
        String mavenExecutable = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";
        boolean buildSuccess = false;
        int attempts = 0;

        while (!buildSuccess && attempts < MAX_RETRIES) {
            System.out.println("Building Maven project in: " + repoDir.getName() + " (Attempt " + (attempts + 1) + "/" + MAX_RETRIES + ")");
            String[] buildCmd = {mavenExecutable, "clean", "install"};
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

    private static void printProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
