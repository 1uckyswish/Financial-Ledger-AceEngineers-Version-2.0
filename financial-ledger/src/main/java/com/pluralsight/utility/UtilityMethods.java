package com.pluralsight.utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UtilityMethods {

    /**
     * Validates integer input from the user without allowing empty input.
     *
     * @param scanner The Scanner object used for input.
     * @param prompt  The message prompt displayed to the user.
     * @return The validated integer input from the user.
     */
    public static int validateIntInput(Scanner scanner, String prompt) {
        // Call the overloaded method with allowEmpty set to false by default
        return validateIntInput(scanner, prompt, false);
    }

    /**
     * Validates integer input from the user.
     *
     * @param scanner    The Scanner object used for input.
     * @param prompt     The message prompt displayed to the user.
     * @param allowEmpty Boolean flag indicating whether empty input is allowed.
     * @return The validated integer input from the user.
     */
    public static int validateIntInput(Scanner scanner, String prompt, boolean allowEmpty) {
        // Initialise a variable that will be the return value once complete
        int input;
        // Enter a while loop for user input till user types correct data Type
        while (true) {
            // Print the argument passed as a parameter to be read within the command line
            System.out.print(prompt);
            // If you allow empty space (Skip)
            // Check if there is another line to read (when you click enter)
            // Check if the nextLine you read is empty (true)
            if (allowEmpty && scanner.hasNextLine() && scanner.nextLine().isEmpty()) {
                return 0; // Return 0 if input is empty and allowed
            }
            // If allowEmpty is false, it completely ignores the top check and goes below
            // this condition checks if the next scanner in the input is an integer.
            // If there is an integer scanner available to be read.
            // scanner.hasNextInt() returns true
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                // Once the user types an integer, it breaks out the while loop, and we return
                // the value;
                break;
            } else {
                // Since we are still stuck in the while loop with no break, the whole process
                // gets repeated and the user gets prompted with an error message
                System.out.println("\n==== Sorry, that input is invalid. Please enter a valid numeric value ===\n");
                scanner.next(); // Consume invalid input
            }
        }
        // Return scanner inputted int
        return input;
    }

    public static String validateStringInput(Scanner scanner, String prompt) {
        return validateStringInput(scanner, prompt, false);
    }

    /**
     * Validates string input from the user.
     *
     * @param scanner    The Scanner object used for input.
     * @param prompt     The message prompt displayed to the user.
     * @param allowEmpty Flag indicating whether empty input is allowed.
     * @return The validated string input from the user.
     */
    public static String validateStringInput(Scanner scanner, String prompt, boolean allowEmpty) {
        // Initialise a variable that will be the return value once complete
        String input;
        // Enter a while loop for user input till user types correct data Type
        while (true) {
            // Print the argument passed as a parameter to be read within the command line
            System.out.print(prompt);
            // Have the user type a string data type input that trims all white space
            input = scanner.nextLine().trim();
            // Check if empty input is allowed or if the input is not empty.
            if (allowEmpty || !input.isEmpty()) {
                // If one of the two equal true break out the loop
                break;
            }
            // Since we are still stuck in the while loop with no break, the whole process
            // gets repeated and the user gets prompted with an error message
            System.out.println("\n==== Sorry, that input is invalid. Please enter a valid non-empty string ===\n");
        }
        // Return scanner inputted string
        return input;
    }

    /**
     * Validates double input from the user without allowing empty input.
     *
     * @param scanner The Scanner object used for input.
     * @param prompt  The message prompt displayed to the user.
     * @return The validated double input from the user.
     */
    public static double validateDoubleInput(Scanner scanner, String prompt) {
        // Set a starter value to use for returning
        double input;
        // Use a while loop to keep prompting until a valid double is entered
        while (true) {
            // Print out the prompt for the user to respond to
            System.out.print(prompt);
            // Check if the input is a valid double
            if (scanner.hasNextDouble()) {
                // If a valid double is entered, assign it to the variable and break the loop
                input = scanner.nextDouble();
                break;
            } else {
                // If the input is not a valid double, alert the user and consume the invalid
                // input
                System.out.println("\n==== Sorry, that input is invalid. Please enter a valid numeric value ===\n");
                scanner.next(); // Consume invalid input
            }
        }
        // Return the validated user input
        return input;
    }

    /**
     * Validates double input from the user.
     *
     * @param scanner   The Scanner object used for input.
     * @param prompt    The message prompt displayed to the user.
     * @param allowSkip Boolean flag indicating whether skipping input is allowed.
     * @return The validated double input from the user.
     */
    public static double validateDoubleInput(Scanner scanner, String prompt, boolean allowSkip) {
        // If skipping is not allowed, use the original method
        if (!allowSkip) {
            return validateDoubleInput(scanner, prompt);
        } else {
            // Set a default value to use if the user skips input
            double input = 0.0;

            // Use a while loop to keep prompting until a valid double is entered or the
            // user skips
            while (true) {
                // Print out the prompt for the user to respond to
                System.out.print(prompt);

                // Check if there is input available
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine().trim(); // Read user input

                    // If user input is empty, return the default value
                    if (userInput.isEmpty()) {
                        break; // Exit loop
                    }

                    // If the input is a valid double, assign it to the variable and break the loop
                    try {
                        input = Double.parseDouble(userInput); // Parse user input
                        break; // Exit loop
                    } catch (NumberFormatException e) {
                        // If user input cannot be parsed as a double, alert the user
                        System.out.println(
                                "\n==== Sorry, that input is invalid. Please enter a valid numeric value ===\n");
                    }
                }
            }

            // Return the validated user input
            return input;
        }
    }

    /**
     * Validates date input from the user in the format (YYYY-MM-DD).
     *
     * @param scanner The Scanner object used for input.
     * @param prompt  The message prompt displayed to the user.
     * @return The validated date input from the user.
     */
    public static String validateDateFormat(Scanner scanner, String prompt) {
        // Call the overloaded method with allowEmpty set to false
        return validateDateFormat(scanner, prompt, false);
    }

    /**
     * Validates date input from the user in the format of (YYYY-MM-DD).
     *
     * @param scanner    The Scanner object used for input.
     * @param prompt     The message prompt displayed to the user.
     * @param allowEmpty Flag indicating whether empty input is allowed.
     * @return The validated date input from the user.
     */
    public static String validateDateFormat(Scanner scanner, String prompt, boolean allowEmpty) {
        // Initialize a variable to store the validated date input that will be the
        // return value once complete
        String date;

        // Enter a while loop for user input till user types correct data Type
        while (true) {
            // Display the prompt to the user from the prompt param
            System.out.print(prompt);
            // Read the input from the user and trim the white space
            date = scanner.nextLine().trim();
            // Check if empty input is allowed and if the input is empty from the user
            if (allowEmpty && date.isEmpty()) {
                // If empty input is allowed and the input is empty, Exit the loop
                // if all false or just one false
                // continue looping until a non-empty input is received
                break;
            }

            // Check if the input has a valid date format (YYYY-MM-DD)
            if (isValidDateFormat(date)) {
                // If the input has a valid date format (true)
                // Exit the loop
                // else, continue looping until a valid date input is received
                break;
            } else {
                // Display an error message if the input does not have a valid date format
                System.out.print(
                        "\n==== Sorry, that input is invalid. Please enter a valid date in the format (YYYY-MM-DD) ===\n");
            }
        }

        // Return the validated date input
        return date;
    }

    /**
     * Checks if the input string has a valid date format (YYYY-MM-DD).
     *
     * @param date The input string to be validated.
     * @return True if the input has a valid date format, otherwise false.
     */
    private static boolean isValidDateFormat(String date) {
        try {
            // try to parse the input string as a LocalDate with the specified date pattern
            // format
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // If parsing succeeds, the input has a valid date format
            return true;
        } catch (DateTimeParseException e) {
            // If parsing fails, the input does not have a valid date format
            return false;
        }
    }

    /**
     * Validates time input from the user in the format (HH:MM:SS).
     *
     * @param scanner The Scanner object used for input.
     * @param prompt  The message prompt displayed to the user.
     * @return The validated time input from the user.
     */
    public static String validateTimeFormat(Scanner scanner, String prompt) {
        // Call the overloaded method with allowEmpty set to false
        return validateTimeFormat(scanner, prompt, false);
    }

    /**
     * Validates time input from the user in the format (HH:MM:SS).
     *
     * @param scanner    The Scanner object used for input.
     * @param prompt     The message prompt displayed to the user.
     * @param allowEmpty Flag indicating whether empty input is allowed.
     * @return The validated time input from the user.
     */
    public static String validateTimeFormat(Scanner scanner, String prompt, boolean allowEmpty) {
        // Initialize a variable to store the validated time input
        String time;

        // Loop until valid input is obtained
        while (true) {
            // Display the prompt to the user from the prompt param
            System.out.print(prompt);
            // Read the input from the user and trim whitespace
            time = scanner.nextLine().trim();
            // Check if empty input is allowed and if the input is empty
            if (allowEmpty && time.isEmpty()) {
                // If empty input is allowed and the input is empty
                // Exit, the loop
                // else continues looping until a non-empty input is received
                break;
            }

            // Check if the input has a valid time format (HH:MM:SS)
            if (isValidTimeFormat(time)) {
                // If the input has a valid time format (true)
                // Exit the loop
                // else continue looping until a valid time input is entered
                break;
            } else {
                // Display an error message if the input does not have a valid time format
                System.out.println(
                        "\n==== Sorry, that input is invalid. Please enter a valid time in the format (HH:MM:SS) ===");
            }
        }

        // Return the validated time input
        return time;
    }

    /**
     * Checks if the input string has a valid time format (HH:MM:SS).
     *
     * @param time The input string to be validated.
     * @return True if the input has a valid time format, otherwise false.
     */
    private static boolean isValidTimeFormat(String time) {
        try {
            // Attempt to parse the input string as a LocalTime with the specified time
            // format
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
            // If parsing succeeds, the input has a valid time format
            return true;
        } catch (DateTimeParseException e) {
            // If parsing fails, the input does not have a valid time format
            return false;
        }
    }
}
