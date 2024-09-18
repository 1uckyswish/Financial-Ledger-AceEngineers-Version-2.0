import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RepoAutomation {

    private static final String REPOS_DIR = "c:\\"; // Directory containing the repositories
    private static final String GIT_PATH = ""; // Path to the Git executable
    private static final String MAVEN_PATH = ""; // Path to the Maven executable
    private static final List<String> failedRepos = new ArrayList<>(); // List to store names of repositories that failed to build
    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public static void main(String[] args) {
        // Check if the user has provided a branch name as an argument
        if (args.length != 1) {
            System.out.println("Usage: java RepoAutomation <branch>");
            return;
        }

        String branch = args[0]; // Get the branch name from command-line arguments
        File repoDir = new File(REPOS_DIR); // Create a File object for the repository directory
        File[] repos = repoDir.listFiles(File::isDirectory); // List all subdirectories (repositories)

        if (repos == null) {
            System.out.println("No repositories found.");
            return;
        }

        // Process each repository
        for (File repo : repos) {
            System.out.println("Processing: " + repo.getName()); // Print the current repository being processed
            try {
                // Attempt to checkout and pull the specified branch
                if (!checkoutAndPull(repo, branch)) {
                    System.out.println("Branch " + branch + " does not exist.");
                    // Prompt the user to either pull from the master branch or specify a different branch
                    String choice = promptBranchChoice();
                    if ("master".equalsIgnoreCase(choice)) {
                        checkoutAndPull(repo, "master");
                    } else {
                        checkoutAndPull(repo, choice);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage()); // Print any error encountered during git operations
            }
        }

        // Ask the user if they want to perform Maven build
        if (promptBuildChoice()) {
            // Perform Maven build for each repository
            for (File repo : repos) {
                System.out.println("Building: " + repo.getName()); // Print the repository being built
                try {
                    // Run Maven clean install
                    if (!executeCommand(new ProcessBuilder(MAVEN_PATH, "clean", "install"), repo)) {
                        // If the build fails, prompt the user to retry
                        if (!retryBuild(repo)) {
                            // If not retrying, add to the list of failed repositories
                            failedRepos.add(repo.getName());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage()); // Print any error encountered during Maven build
                    failedRepos.add(repo.getName()); // Add to the list of failed repositories
                }
            }

            // Print the list of repositories that failed to build
            System.out.println("Failed repositories:");
            failedRepos.forEach(System.out::println);
        } else {
            System.out.println("Build process aborted."); // Inform the user if the build process is aborted
        }
    }

    // Method to checkout and pull a specified branch
    private static boolean checkoutAndPull(File repo, String branch) {
        try {
            // Checkout the specified branch
            if (!executeCommand(new ProcessBuilder(GIT_PATH, "checkout", branch), repo)) {
                return false;
            }
            // Pull changes from the specified branch
            return executeCommand(new ProcessBuilder(GIT_PATH, "pull"), repo);
        } catch (Exception e) {
            System.out.println("Error during git operations: " + e.getMessage());
            return false;
        }
    }

    // Method to execute a command in a given directory
    private static boolean executeCommand(ProcessBuilder builder, File directory) throws Exception {
        builder.directory(directory); // Set the working directory for the command
        Process process = builder.start(); // Start the process
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream())); // Read error stream
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line); // Print error output
        }
        return process.waitFor() == 0; // Return true if the process completed successfully
    }

    // Method to prompt the user for branch choice if the initial branch does not exist
    private static String promptBranchChoice() {
        System.out.println("Do you want to pull from master or specify a different branch?");
        System.out.println("Type 'master' for the master branch or enter a different branch name:");
        return scanner.nextLine().trim(); // Read user input
    }

    // Method to prompt the user if they want to perform Maven build on all repositories
    private static boolean promptBuildChoice() {
        System.out.println("Do you want to perform mvn clean install on all repositories? (yes/no/y/n)");
        String response = scanner.nextLine().trim().toLowerCase(); // Read and normalize user input
        return "yes".equals(response) || "y".equals(response); // Return true if user confirms
    }

    // Method to prompt the user to retry the build if it fails
    private static boolean retryBuild(File repo) {
        String response;
        while (true) {
            System.out.println("Build failed for " + repo.getName() + ". Retry? (yes/no/y/n)");
            response = scanner.nextLine().trim().toLowerCase(); // Read and normalize user input
            if ("yes".equals(response) || "y".equals(response)) {
                try {
                    // Retry the Maven build
                    return executeCommand(new ProcessBuilder(MAVEN_PATH, "clean", "install"), repo);
                } catch (Exception e) {
                    System.out.println("Retry failed: " + e.getMessage());
                }
            } else if ("no".equals(response) || "n".equals(response)) {
                return false; // Do not retry
            } else {
                // Handle invalid input
                System.out.println("Sorry, that's invalid input. Please type 'yes', 'no', 'y', or 'n'.");
            }
        }
    }
}
