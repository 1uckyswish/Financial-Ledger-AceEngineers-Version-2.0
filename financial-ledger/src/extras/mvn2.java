import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class GitMavenAutomation {

    private static final String REPOS_DIR = "";
    private static final String CSV_FILE = "";
    private static final int MAX_RETRIES = 5;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final String MAVEN_EXECUTABLE = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";

    public static void main(String[] args) {
        List<String> repos = readReposFromCSV();
        if (repos.isEmpty()) {
            System.out.println("No repositories found in CSV.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Submit each repo for processing in a separate thread
        for (String repoName : repos) {
            File repoDir = new File(REPOS_DIR + repoName);
            if (repoDir.exists() && new File(repoDir, ".git").exists()) {
                executor.submit(() -> processRepo(repoDir, repoName));
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

    // Write the updated repository list to CSV file
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

    // Process each repository
    private static void processRepo(File repoDir, String repoName) {
        System.out.println("\nProcessing repository: " + repoName);

        if (buildMavenProjectWithRetry(repoDir)) {
            System.out.println("Build successful for: " + repoName);
            removeRepoFromCSV(repoName);  // Remove the repo from the CSV on successful build
        } else {
            System.out.println("Build failed for: " + repoName);
        }
    }

    // Maven build with retry logic and progress simulation
    private static boolean buildMavenProjectWithRetry(File repoDir) {
        boolean buildSuccess = false;
        int attempts = 0;

        while (!buildSuccess && attempts < MAX_RETRIES) {
            System.out.println("Building Maven project in: " + repoDir.getName() + " (Attempt " + (attempts + 1) + "/" + MAX_RETRIES + ")");
            simulateProgress("Building " + repoDir.getName());
            String[] buildCmd = {MAVEN_EXECUTABLE, "clean", "install", "-T", String.valueOf(THREAD_POOL_SIZE)};
            int result = runCommand(buildCmd, repoDir);

            if (result == 0) {
                buildSuccess = true;
                System.out.println("\nBuild successful.");
            } else {
                attempts++;
                System.out.println("\nBuild failed for " + repoDir.getName() + ". Retry " + attempts + "/" + MAX_RETRIES);
            }
        }
        return buildSuccess;
    }

    // Simulate a rotating progress spinner or progress bar
    private static void simulateProgress(String message) {
        char[] spinner = {'|', '/', '-', '\\'};
        for (int i = 0; i < 20; i++) {  // Run progress animation for a short while (20 iterations)
            System.out.print("\r" + message + " " + spinner[i % spinner.length]);
            try {
                Thread.sleep(200);  // Sleep for 200ms to simulate progress
            } catch (InterruptedException e) {
                System.err.println("Progress simulation interrupted");
            }
        }
        System.out.print("\r" + message + " Done!\n");
    }

    // Remove a successful repo from the CSV
    private static void removeRepoFromCSV(String repoName) {
        List<String> repos = readReposFromCSV();
        repos = repos.stream().filter(repo -> !repo.equalsIgnoreCase(repoName)).collect(Collectors.toList());
        writeReposToCSV(repos);
    }

    // Run a command and return the exit code
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

    // Print process output
    private static void printProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
