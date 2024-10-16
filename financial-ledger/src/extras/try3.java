import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MavenBuildAutomation {

    private static final String MAVEN_COMMAND = "C:/"; // Adjust path to your Maven executable
    private static final String REPO_FOLDER_PATH = "C:"; // Path to your repositories folder
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        File repoFolder = new File(REPO_FOLDER_PATH);
        if (!repoFolder.isDirectory()) {
            System.out.println("The provided path is not a directory.");
            return;
        }

        File[] repos = repoFolder.listFiles(File::isDirectory); // Get all directories (repositories) in the folder
        if (repos == null || repos.length == 0) {
            System.out.println("No repositories found in the folder.");
            return;
        }

        List<String> failedRepos = new ArrayList<>();

        for (File repo : repos) {
            System.out.println("\nBuilding repository: " + repo.getName());

            boolean success = runMavenBuild(repo);
            if (!success) {
                handleFailedRepo(repo, failedRepos);
            }
        }

        if (!failedRepos.isEmpty()) {
            System.out.println("\nBuild failed for the following repositories:");
            for (String failedRepo : failedRepos) {
                System.out.println(failedRepo);
            }
        } else {
            System.out.println("\nAll repositories built successfully.");
        }
    }

    private static boolean runMavenBuild(File repo) {
        try {
            ProcessBuilder builder = new ProcessBuilder(MAVEN_COMMAND, "clean", "install");
            builder.directory(repo); // Set the working directory to the repo
            builder.redirectErrorStream(true);

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print Maven build output
            }

            int exitCode = process.waitFor();
            return exitCode == 0; // Return true if build was successful, false otherwise
        } catch (Exception e) {
            System.out.println("An error occurred while building " + repo.getName());
            e.printStackTrace();
            return false;
        }
    }

    private static void handleFailedRepo(File repo, List<String> failedRepos) {
        while (true) {
            System.out.println("\nBuild failed for repository: " + repo.getName());
            System.out.print("Would you like to try building again? (yes/y or no/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                boolean success = runMavenBuild(repo);
                if (success) {
                    return; // Build was successful on retry, exit the method
                }
            } else if (input.equals("no") || input.equals("n")) {
                failedRepos.add(repo.getName());
                return; // Add to failed list and exit the method
            } else {
                System.out.println("Invalid input. Please enter 'yes/y' or 'no/n'.");
            }
        }
    }
}
