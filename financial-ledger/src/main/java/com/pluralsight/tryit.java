import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GitPullAutomation {

    private static final String REPOS_DIR = "c:\\cpsrepo"; // Directory containing repositories
    private static final String GIT_PATH = "\"C:\\program files\\git\\bin\\git.exe\""; // Path to Git executable
    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GitPullAutomation <branch>");
            return;
        }

        String branch = args[0]; // Get the branch name from command-line arguments
        File repoDir = new File(REPOS_DIR); // Repositories directory
        File[] repos = repoDir.listFiles(File::isDirectory); // List all subdirectories (repositories)

        if (repos == null) {
            System.out.println("No repositories found.");
            return;
        }

        // Iterate through all repositories
        for (File repo : repos) {
            System.out.println("====================================================================");
            System.out.println("🌟 Git pulling from " + repo.getName() + " on feature branch - " + branch);
            System.out.println("====================================================================");
            try {
                // Check if the directory is a valid Git repository
                if (!isGitRepository(repo)) {
                    System.out.println(repo.getName() + " is not a valid Git repository.");
                    continue; // Skip to the next repository
                }

                // Attempt to checkout and pull the specified branch
                if (!checkoutAndPull(repo, branch)) {
                    System.out.println("Branch " + branch + " does not exist.");
                    String choice = promptBranchChoice();
                    if ("master".equalsIgnoreCase(choice)) {
                        System.out.println("🌟 Git pulling from " + repo.getName() + " on master branch");
                        checkoutAndPull(repo, "master");
                    } else {
                        System.out.println("🌟 Git pulling from " + repo.getName() + " on branch - " + choice);
                        checkoutAndPull(repo, choice);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Method to check if a directory is a valid Git repository
    private static boolean isGitRepository(File repo) {
        try {
            return executeCommand(new ProcessBuilder(GIT_PATH, "rev-parse", "--is-inside-work-tree"), repo);
        } catch (Exception e) {
            return false;
        }
    }

    // Method to checkout and pull a specified branch
    private static boolean checkoutAndPull(File repo, String branch) {
        try {
            // Checkout the branch
            if (!executeCommand(new ProcessBuilder(GIT_PATH, "checkout", branch), repo)) {
                return false;
            }
            // Pull changes from the branch
            return executeCommand(new ProcessBuilder(GIT_PATH, "pull"), repo);
        } catch (Exception e) {
            System.out.println("Error during git operations: " + e.getMessage());
            return false;
        }
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

    // Method to prompt the user for branch choice if the initial branch doesn't exist
    private static String promptBranchChoice() {
        System.out.println("Do you want to pull from master or specify a different branch?");
        System.out.println("Type 'master' for the master branch or enter a different branch name:");
        return scanner.nextLine().trim(); // Read user input
    }
}
