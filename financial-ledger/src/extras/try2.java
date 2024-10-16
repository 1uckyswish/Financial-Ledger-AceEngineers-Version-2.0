import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MavenBuildAutomation {

    private static final String REPOS_DIR = "c:\\jee_wiz"; // Directory containing repositories
    private static final String MAVEN_PATH = "\"C:\\program files\\apache-maven-3.92\\bin\\mvn.cmd\""; // Path to Maven executable
    private static final List<String> failedRepos = new ArrayList<>(); // List to store failed repositories
    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public static void main(String[] args) {
        File repoDir = new File(REPOS_DIR); // Repositories directory
        File[] repos = repoDir.listFiles(File::isDirectory); // List all subdirectories (repositories)

        if (repos == null) {
            System.out.println("No repositories found.");
            return;
        }

        for (File repo : repos) {
            System.out.println("====================================================================");
            System.out.println("🚀 Building: " + repo.getName() + " using mvn clean install");
            System.out.println("====================================================================");
            try {
                // Run Maven clean install
                if (!executeCommand(new ProcessBuilder(MAVEN_PATH, "clean", "install"), repo)) {
                    if (!retryBuild(repo)) {
                        failedRepos.add(repo.getName()); // Add to failed repositories list
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                failedRepos.add(repo.getName());
            }
        }

        // Print the list of repositories that failed to build
        System.out.println("====================================================================");
        System.out.println("❌ Failed repositories:");
        failedRepos.forEach(System.out::println);
        System.out.println("====================================================================");
    }

    // Method to execute a command in a given directory
    private static boolean executeCommand(ProcessBuilder builder, File directory) throws Exception {
        builder.directory(directory); // Set the working directory for the command
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line); // Print error output
        }
        return process.waitFor() == 0; // Return true if the command completed successfully
    }

    // Method to prompt the user to retry the build if it fails
    private static boolean retryBuild(File repo) {
        String response;
        while (true) {
            System.out.println("Build failed for " + repo.getName() + ". Retry? (yes/no/y/n)");
            response = scanner.nextLine().trim().toLowerCase(); // Read user input
            if ("yes".equals(response) || "y".equals(response)) {
                try {
                    // Retry the Maven build
                    return executeCommand(new ProcessBuilder(MAVEN_PATH, "clean", "install"), repo);
                } catch (Exception e) {
                    System.out.println("Retry failed: " + e.getMessage());
                }
            } else if ("no".equals(response) || "n".equals(response)) {
                return false;
            } else {
                System.out.println("Invalid input. Please type 'yes', 'no', 'y', or 'n'.");
            }
        }
    }
}
