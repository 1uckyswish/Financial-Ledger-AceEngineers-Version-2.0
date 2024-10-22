import java.io.*;
import java.util.*;

public class MavenAutomation {

    private static final String REPOS_CSV = "C:/";  // CSV file containing the list of repos
    private static final String MAVEN_EXECUTABLE = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";
    private static final int RETRY_LIMIT = 5; // Retry 5 times before prompting
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<String> repositories = readRepositoriesFromCSV();

        for (String repoPath : repositories) {
            File repoDir = new File(repoPath);

            if (repoDir.exists() && repoDir.isDirectory()) {
                int attempts = 0;
                boolean buildSuccess = false;

                while (!buildSuccess) {
                    attempts++;
                    System.out.println("Building Maven project in: " + repoDir.getName() + " (Attempt " + attempts + ")");
                    int result = runMavenBuild(repoDir);

                    if (result == 0) {
                        buildSuccess = true;
                        removeRepoFromCSV(repoPath);
                        printInGreen("Build successful on attempt " + attempts);
                    } else {
                        printInRed("Build failed for " + repoDir.getName() + " on attempt " + attempts);
                        if (attempts % RETRY_LIMIT == 0) {
                            if (!promptContinueBuilding()) {
                                System.out.println("Terminating the build process.");
                                return; // Exit the program
                            }
                        }
                    }
                }
            } else {
                System.out.println("Repository directory not found: " + repoPath);
            }
        }
    }

    // Method to read repositories from CSV
    private static List<String> readRepositoriesFromCSV() {
        List<String> repositories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REPOS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                repositories.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading repositories from CSV: " + e.getMessage());
        }
        return repositories;
    }

    // Method to run the Maven build
    private static int runMavenBuild(File repoDir) {
        String[] buildCmd = {MAVEN_EXECUTABLE, "clean", "install"};
        ProcessBuilder builder = new ProcessBuilder(buildCmd);
        builder.directory(repoDir);
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running Maven build: " + e.getMessage());
            return -1;
        }
    }

    // Method to remove a successfully built repository from the CSV file
    private static void removeRepoFromCSV(String repoPath) {
        File csvFile = new File(REPOS_CSV);
        List<String> updatedRepos = new ArrayList<>();

        // Read all repositories except the one to remove
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals(repoPath)) {
                    updatedRepos.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        // Write back the updated list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String repo : updatedRepos) {
                writer.write(repo + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Method to print green text
    private static void printInGreen(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    // Method to print red text
    private static void printInRed(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    // Method to prompt user whether to continue building after retries
    private static boolean promptContinueBuilding() {
        System.out.print("Would you like to continue building? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("yes");
    }
}
