import java.io.*; // For handling I/O operations (reading and writing files)
import java.nio.file.*; // For handling file paths
import java.util.Scanner; // To take input from the user

public class GitMavenAutomation {

    // Directory where all the Git repositories are located
    private static final String REPOS_DIR = "C:\\cpsrepo";

    // Scanner object to read user input
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // If the user doesn't provide a branch as an argument, prompt them and exit
        if (args.length == 0) {
            System.out.println("Please specify the branch to checkout.");
            System.out.println("Usage: java -cp bin GitMavenAutomation <branch_name>");
            return;
        }

        // Get the branch name from the first command-line argument
        String branch = args[0];

        // Check if the specified repositories directory exists and is a directory
        File reposDir = new File(REPOS_DIR);
        if (!reposDir.exists() || !reposDir.isDirectory()) {
            System.out.println("Invalid repository directory: " + REPOS_DIR);
            return;
        }

        // Get a list of all directories (repositories) in the repository directory
        File[] repos = reposDir.listFiles(File::isDirectory);
        if (repos != null) {
            // Loop through each repository directory
            for (File repo : repos) {
                // Check if the directory contains a ".git" folder, which signifies a Git repo
                if (new File(repo, ".git").exists()) {
                    System.out.println("\nProcessing repository: " + repo.getName());

                    // Checkout the branch and pull the latest changes
                    String branchToUse = checkoutBranch(repo, branch);
                    if (branchToUse != null) {
                        pullBranch(repo, branchToUse);
                        // Attempt to build the Maven project, prompting the user on failure
                        buildMavenProjectWithPrompt(repo);
                    }
                }
            }
        }
    }

    // Function to checkout a Git branch
    private static String checkoutBranch(File repoDir, String branch) {
        System.out.println("Attempting to checkout branch: " + branch);

        // Command to checkout the specified branch
        String[] checkoutCmd = {"git", "checkout", branch};

        // Run the command, if it fails, fallback to master branch
        if (runCommand(checkoutCmd, repoDir) != 0) {
            System.out.println("Branch " + branch + " not found, checking out 'master'.");
            String[] checkoutMasterCmd = {"git", "checkout", "master"};
            if (runCommand(checkoutMasterCmd, repoDir) != 0) {
                System.out.println("Failed to checkout 'master' branch.");
                return null; // If both branch and master fail, return null
            }
            return "master"; // If master succeeds, return "master"
        }
        return branch; // If the branch checkout was successful, return the branch name
    }

    // Function to pull the latest changes for the current branch
    private static void pullBranch(File repoDir, String branch) {
        System.out.println("Pulling latest changes for branch: " + branch);
        String[] pullCmd = {"git", "pull"};
        runCommand(pullCmd, repoDir); // Run the Git pull command
    }

    // Function to build the Maven project with retry prompt on failure
    private static void buildMavenProjectWithPrompt(File repoDir) {
        // Path to the Maven executable. Update this to match your Maven installation
        String mavenExecutable = "C:\\Program Files\\Maven\\apache-maven-3.9.2\\bin\\mvn.cmd";
        boolean buildSuccess = false; // Flag to track if the build was successful

        while (!buildSuccess) {
            System.out.println("Building Maven project in: " + repoDir.getName());

            // Command to clean and build the project
            String[] buildCmd = {mavenExecutable, "clean", "install"};
            int result = runCommand(buildCmd, repoDir); // Run the build command

            if (result == 0) {
                // If the build was successful
                buildSuccess = true;
                System.out.println("Build successful.");
            } else {
                // If the build failed, prompt the user to retry or skip
                System.out.println("Build failed. Do you want to try again? (Y/YES or N/NO)");

                String userInput = getUserInput();
                // Retry the build if the user responds with "Y" or "YES"
                if (userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("YES")) {
                    System.out.println("Retrying build...");
                } else if (userInput.equalsIgnoreCase("N") || userInput.equalsIgnoreCase("NO")) {
                    // Skip to the next repository if the user responds with "N" or "NO"
                    System.out.println("Skipping to the next repository...");
                    break;
                } else {
                    // Invalid input, skip to the next repository
                    System.out.println("Invalid input. Skipping to the next repository...");
                    break;
                }
            }
        }
    }

    // Function to get user input from the console
    private static String getUserInput() {
        System.out.print("Enter your choice: ");
        return scanner.nextLine().trim(); // Read and trim user input
    }

    // Function to run a command in the specified directory
    private static int runCommand(String[] command, File workingDir) {
        // Set up the command to run using ProcessBuilder
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir); // Set the working directory for the command
        builder.redirectErrorStream(true); // Merge stdout and stderr for easier output capture

        try {
            Process process = builder.start(); // Start the process
            printProcessOutput(process.getInputStream()); // Print the process output
            return process.waitFor(); // Wait for the process to finish and return exit code
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running command: " + String.join(" ", command));
            e.printStackTrace();
            return -1; // Return error code if something goes wrong
        }
    }

    // Function to print the output of a process to the console
    private static void printProcessOutput(InputStream inputStream) throws IOException {
        // Try-with-resources to automatically close the BufferedReader
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print each line of the process output
            }
        }
    }
}
