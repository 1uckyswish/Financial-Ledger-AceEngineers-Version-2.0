package com.pluralsight;

import com.pluralsight.utility.UtilityMethods;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Mysql import statements
 **/

import com.pluralsight.data.DatabaseConnector;
import com.pluralsight.data.interfaces.TransactionDao;
import com.pluralsight.data.interfaces.UserDao;
import com.pluralsight.data.mysql.MySqlTransactionDao;
import com.pluralsight.data.mysql.MySqlUserDao;
import com.pluralsight.models.Transaction;
import com.pluralsight.models.User;
import org.apache.commons.dbcp2.BasicDataSource;

public class AccountingLedger {
    // Hold all Transactions read from MYSql DB and apply them to an Arraylist to
    // easily append and retrieve values
    // Static variable to hold the current user
    static User user = null;
    // Data Access Object (DAO) for user operations
    static UserDao userDao = new MySqlUserDao();
    // DAO for transaction operations
    static TransactionDao transactionDao = new MySqlTransactionDao();
    // Data source for database connections
    public static BasicDataSource basicDataSource = DatabaseConnector.connect();
    // List to store the transaction history read from the MySQL database
    static List<Transaction> transactionHistory = new ArrayList<>();


    /**
     * Entry point of the Accounting Ledger application.
     *
     * @throws IOException If an I/O error occurs.
     */
    // ~~~~~~~~~~~~~~~~~~ Authored by Zamir and Tina ~~~~~~~~~~~~~~~~~~
    public static void main(String[] args) throws IOException {
        // Create a Scanner object to handle user input throughout the application
        Scanner scanner = new Scanner(System.in);

        // Call the loginOrRegister method to prompt the user to log in or register
        loginOrRegister(scanner);
    }

    /**
     * This method handles user login and registration options from the user with a UI.
     * @param scanner Scanner object to read user input.
     * @throws IOException  If an I/O error occurs during operation.
     */
    public static void loginOrRegister(Scanner scanner) throws IOException {
        // Welcome the user and display the options for them to choose from
        System.out.println("\n------------------------------------------------------------");
        System.out.println("\t 🌟💰Welcome to the Account Ledger Application💰🌟");
        System.out.println("\t\t - Would you like to login or register 🤓? - \t");
        System.out.println("------------------------------------------------------------\n");
        System.out.println("Please select from the following options:");
        System.out.println("(L) Login 🖥️");
        System.out.println("(R) Register 📝");
        System.out.println("(X) Exit the application 👋🏼");
        // Prompt user for input
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().toUpperCase();
        // Handle user's choice by Letter
        switch (choice) {
            case "L":
                // Call method to login
                handleUserData(true, scanner);
                break;
            case "R":
                // Call method to register
                handleUserData(false, scanner);
                break;
            case "X":
                // Handle Exit option
                System.out.println("Exiting the application...");
                System.exit(0);
                break;
            default:
                // If user types the wrong option not displayed. Use recursion.
                System.out.println("\n----Invalid choice. Please enter a valid option.----\n");
                loginOrRegister(scanner);
        }

    }

    /**
     * Handles user login and registration process.
     *
     * @param isLoggedIn Boolean flag indicating if the user is logging in (true) or registering (false).
     * @param scanner Scanner object to read user input.
     * @throws IOException If an I/O error occurs during operation.
     */
    public static void handleUserData(boolean isLoggedIn, Scanner scanner) throws IOException {
        // Prompt the user to enter their username
        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();

        // Prompt the user to enter their password
        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        // If the user is logging in
        if (isLoggedIn) {
            // Attempt to log the user in using the provided username and password
            user = userDao.userLogin(username, password);

            // Read and add transaction history to the list
            readAndAddToTransactionHistory(scanner);

            // Display the home screen of the application
            displayHomeScreen(scanner);
        } else {
            // If the user is registering
            // Attempt to register the user with the provided username and password
            user = userDao.userRegister(username, password);

            // If registration fails, prompt the user to login or register again
            if (user == null) {
                loginOrRegister(scanner);
            } else {
                // If registration is successful, read and add transaction history
                readAndAddToTransactionHistory(scanner);

                // Display the home screen of the application
                displayHomeScreen(scanner);
            }
        }
    }

    /**
     * Reads transaction data from the MySQL database and adds transactions to the
     * transaction history ArrayList.
     *
     * @param scanner Scanner object to handle user input in case of a login error.
     * @throws IOException If an I/O error occurs.
     */
    public static void readAndAddToTransactionHistory(Scanner scanner) throws IOException {
        // Check if the user is logged in
        if (user != null) {
            // Retrieve all transactions for the logged-in user and add them to the transaction history list
            transactionHistory = transactionDao.getAllTransactions(user.getUserId());
        } else {
            // If user credentials are incorrect, inform the user and prompt them to log in or register again
            System.out.println("\n~~~~ Login credentials are incorrect. Please try again 😁 ~~~~\n");
            loginOrRegister(scanner);
        }
    }


    /**
     * Displays the main menu of the Account Ledger application and handles user
     * interactions.
     *
     * @param scanner A Scanner object used for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayHomeScreen(Scanner scanner) throws IOException {
        // Welcome the user and display the options for them to choose from
        System.out.println("\n==================================================================");
        System.out.printf("\t\t\t\t\t💵💴🌟 Welcome, %s! 🌟💵💴\n",
                user.getUsername().substring(0, 1).toUpperCase() + user.getUsername().substring(1));
        System.out.println("==================================================================\n");

        System.out.println("Please select from the following options:");
        System.out.println("(D) Add Deposit - Add a deposit to the ledger");
        System.out.println("(P) Make Payment (Debit) - & deduct it from the ledger");
        System.out.println("(L) Ledger - Display the ledger");
        System.out.println("(B) Balance - View Ledger Balance");
        System.out.println("(X) Exit - Exit the application");
        // Prompt user for input
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().toUpperCase();
        // Handle user's choice by Letter
        switch (choice) {
            case "D":
                // Call method to add deposit
                // Pass the scanner as argument one
                // Pass true for the second argument if the transaction is a deposit
                addTransaction(scanner, true);
                break;
            case "P":
                // Call method to make payment
                // Pass the scanner as argument one
                // Pass false for the second argument since the transaction is not a deposit
                addTransaction(scanner, false);
                break;
            case "L":
                // Call method to display ledger
                // Pass the scanner as argument one
                displayLedger(scanner);
                break;
            case "B":
                // Call method to display ledger balance
                // Pass the scanner as argument one
                calculateLedgerBalance(scanner);
                break;
            case "X":
                // Handle Exit option
                System.out.println("Exiting the application...");
                break;
            default:
                // If user types the wrong option not displayed. Use recursion by calling the
                // method within itself
                System.out.println("\n----Invalid choice. Please enter a valid option.----\n");
                displayHomeScreen(scanner);
        }
        // Releases all resources associated with the reader.
//        scanner.close();
    }

    /**
     * Prompts the user to enter details for a new transaction and adds it to the
     * ledger.
     *
     * @param scanner   A Scanner object for user input.
     * @param isDeposit A boolean indicating if the transaction is a deposit (true)
     *                  or payment (false).
     * @throws IOException If an I/O error occurs.
     */
    public static void addTransaction(Scanner scanner, boolean isDeposit) throws IOException {
        // Display information prompt for the user to fill out
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("\t   - Please Fill out the following information - \t");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // Ask the user if they want to use the current date
        System.out.print("Do you want to use the current date 📅? (Y/N): ");
        String choice = scanner.nextLine().toUpperCase();
        String date;
        // If a user chooses to use the current date, set it to the current date
        if (choice.equals("Y")) {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            // Prompt the user to enter a specific date
            date = UtilityMethods.validateDateFormat(scanner, "Please Enter The Date (YYYY-MM-DD): ");
        }

        // Ask the user if they want to use the current time
        System.out.print("Do you want to use the current time ⏱️? (Y/N): ");
        choice = scanner.nextLine().toUpperCase();
        LocalTime currentTime = LocalTime.now();
        String time;
        // If a user chooses to use the current time, set it to the current time
        if (choice.equals("Y")) {
            time = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } else {
            // Ask the user to enter a specific time
            time = UtilityMethods.validateTimeFormat(scanner, "Please Enter The Time (HH:MM:SS): ");
        }
        // Ask the user to enter a specific time
        String description = UtilityMethods.validateStringInput(scanner, "Please Enter The Description: ");

        String vendor = UtilityMethods.validateStringInput(scanner, "Please Enter The Vendor: ");

        double amount = UtilityMethods.validateDoubleInput(scanner, "Please Enter The Amount: ");
        scanner.nextLine(); // Consume the newline character left by nextDouble()

        // Convert positive amount to negative if it's a payment
        if (!isDeposit && amount > 0) {
            amount = -amount;
        }

        // Use ternary to check what if (IsDeposit) is true or false
        System.out.println("- Summary of " + (isDeposit ? "Deposit" : "Payment") + " -");
        // Create a new transaction
        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        // Add the new transaction to the MYSql DB
        // Pass the value of the inputted data to have the method write to the MYSql DB
        transactionDao.createTransaction(newTransaction, user.getUserId());
        // Add the new transaction to the transactionHistory ArrayList immediately
        readAndAddToTransactionHistory(scanner);
        System.out.println(newTransaction);
        // Provide feedback to the user
        System.out.println((isDeposit ? "Deposit" : "Payment") + " added successfully!");

        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Displays the ledger options to the user and prompts for their choice.
     *
     * @param scanner A Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayLedger(Scanner scanner) throws IOException {
        // Display ledger options for the user to choose from
        System.out.println("\nLedger Options:");
        System.out.println("(A) Show All - Display all transaction entries");
        System.out.println("(D) Deposits Only - Display only deposit transactions");
        System.out.println("(P) Payments Only - Display only negative transactions (payments)");
        System.out.println("(R) Run Reports - Open a new screen to run predefined transaction reports");
        System.out.print("Enter your choice: ");
        // Read user input and convert to uppercase to prevent case sensitivity
        String choice = scanner.nextLine().trim().toUpperCase();
        // Another way would be to do the .equalsIgnore()
        switch (choice) {
            // Invoke the same method to handle the first three options.
            // Specifying the category, each option corresponds to as the first argument
            // pass scanner as the second argument
            case "A":
                displayTransactions("All", scanner);
                break;
            case "D":
                displayTransactions("Deposits", scanner);
                break;
            case "P":
                displayTransactions("Payments", scanner);
                break;
            case "R":
                // Call method to handle running reports
                // pass scanner as the second argument
                displayReports(scanner);
                break;
            default:
                // If user types the wrong option not displayed. Use recursion by calling the
                // method within itself
                System.out.println("\n----Invalid choice. Please choose a valid option (A, D, P, or R).----\n");
                displayLedger(scanner);
        }

    }

    /**
     * Displays transactions based on the specified display option.
     *
     * @param displayOption The option to filter transactions (e.g., "All",
     *                      "Deposits", "Payments").
     * @param scanner       The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayTransactions(String displayOption, Scanner scanner) throws IOException {
        // Display header for transactions based on the chosen option
        System.out.println("\nTransactions (" + displayOption + "):");

        if (transactionHistory.isEmpty()) {
            System.out.println("\nApologies, there are currently no transactions to display.");
            return;
        }

        // Sort the transactionHistory list
        Collections.sort(transactionHistory, Comparator.comparing(Transaction::getDate).reversed());

        boolean hasTransactions = false;

        for (Transaction transaction : transactionHistory) {
            switch (displayOption.toLowerCase()) {
                case "all":
                    // Display all transactions
                    System.out.println(transaction);
                    hasTransactions = true;
                    break;
                case "deposits":
                    // Display only deposit transactions
                    if (transaction.getAmount() >= 0) {
                        System.out.println(transaction);
                        hasTransactions = true;
                    }
                    break;
                case "payments":
                    // Display only payment transactions (Negative)
                    if (transaction.getAmount() < 0) {
                        System.out.println(transaction);
                        hasTransactions = true;
                    }
                    break;
                default:
                    // Handle an invalid display option (will never happen)
                    System.out.println("Invalid display option");
                    return;
            }
        }

        if (!hasTransactions) {
            System.out.println("\nApologies, there are currently no transactions to display.");
        }

        // Prompt the user for the next action
        while (true) {
            System.out.println("\nPlease select your next action:");
            System.out.println("1. Go back home");
            System.out.println("2. View other ledger reports");
            System.out.print("Enter your choice: ");
            String userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1":
                    // Go back home
                    displayHomeScreen(scanner);
                    return; // Break out of the loop
                case "2":
                    // View other ledger reports
                    displayLedger(scanner);
                    return; // Break out of the loop
                default:
                    System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        }
    }
    /**
     * Calculates the total income and expenses from the transaction history
     * ArrayList.
     *
     * @param scanner The scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void calculateLedgerBalance(Scanner scanner) throws IOException {
        // Variables to store the total income and expenses
        double totalIncome = 0;
        double totalExpenses = 0;
        // Loop through the transaction history ArrayList to calculate totals
        for (Transaction transaction : transactionHistory) {
            // Check if the transaction amount is greater than 0 (income) or less than 0
            // (expenses) using the Getter Method
            if (transaction.getAmount() > 0) {
                // Accumulate income each time through the loop iteration
                totalIncome += transaction.getAmount();
            } else {
                // Accumulate expenses (amounts are negative) each time through the loop
                // iteration
                totalExpenses += transaction.getAmount();
            }
        }
        // Print out the ledger balance report for the user
        System.out.println("\n------------------------------------------------------------");
        System.out.println("\t\tYour Current Ledger Balance Report");
        System.out.println("------------------------------------------------------------");

        System.out.printf("Total Income: $%,.2f", totalIncome);
        System.out.printf("\nTotal Expenses: $%,.2f", totalExpenses);
        double balance = totalIncome + totalExpenses;
        System.out.printf("\nLedger Balance: $%,.2f", balance);

        // Navigate back to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Displays pre-defined reports or allows the user to run custom searches.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayReports(Scanner scanner) throws IOException {
        // Display report options to the user
        System.out.println("\nReports - Access pre-defined reports or run custom searches:");
        System.out.println("(1) Month To Date");
        System.out.println("(2) Previous Month");
        System.out.println("(3) Year To Date");
        System.out.println("(4) Previous Year");
        System.out.println("(5) Search by Vendor - Enter the vendor name to display all entries for that vendor");
        System.out.println("(6) Custom Search - Enter any input for filtered search");
        System.out.println("(7) Back - Return to the ledger page");
        System.out.println("(8) Home - Return to the home page");

        // Prompt user for choice
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                // Generate the Month To Date report to user
                displayMonthToDateReport(scanner);
                break;
            case "2":
                // Generate the previous Month report to user
                displayPreviousMonthReport(scanner);
                break;
            case "3":
                // Generate a Year To Date report to user
                displayYearToDateReport(scanner);
                break;
            case "4":
                // Generate a Previous Year report to user
                displayPreviousYearReport(scanner);
                break;
            case "5":
                // Handle Search by Vendor to user by user input
                searchByVendor(scanner);
                break;
            case "6":
                // Handle Custom Search to user by user input
                customReportSearch(scanner);
                break;
            case "7":
                // Return to the previous page (Ledger screen)
                displayLedger(scanner);
                break;
            case "8":
                // Return to the home page (Welcome screen)
                displayHomeScreen(scanner);
                break;
            default:
                // Inform the user of an invalid choice and prompt again
                System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                // Re-display the report menu
                displayReports(scanner);
        }
    }

    /**
     * Generates and displays a report of transactions from the beginning of the
     * current month to the current date.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayMonthToDateReport(Scanner scanner) throws IOException {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        // Get the first day of the month with its methods
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        // Display report header
        System.out.println("------------------------------------------------------------");
        System.out.printf("\t\tReports from %s through %s%n", firstDayOfMonth, currentDate);
        System.out.println("------------------------------------------------------------");
        if (transactionHistory.size() <= 0) {
            System.out.println("\nApologies, there are currently no transactions to display.");
        }
        // loop through the ArrayList
        for (Transaction transaction : transactionHistory) {
            // Parse transaction date from string to LocalDate object
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());
            // Display the transaction if it falls within the range
            // Check if the date is equal to today or is after the first of current month
            // AND is before the current date
            if (transactionDate.isEqual(currentDate)
                    || (transactionDate.isAfter(firstDayOfMonth) && transactionDate.isBefore(currentDate))) {
                System.out.println(transaction);
            }
        }
        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Generates and displays a report of transactions from the previous month.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayPreviousMonthReport(Scanner scanner) throws IOException {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Get the first day of the previous month
        LocalDate firstDayOfPreviousMonth = currentDate.minusMonths(1).withDayOfMonth(1);

        // Get the last day of the previous month
        LocalDate lastDayOfPreviousMonth = currentDate.minusMonths(1)
                .withDayOfMonth(currentDate.minusMonths(1).lengthOfMonth());
        // calculates and assigns the last day of the previous month to the variable

        // Display report header
        System.out.println("------------------------------------------------------------");
        System.out.printf("\t\tReports from %s through %s%n", firstDayOfPreviousMonth, lastDayOfPreviousMonth);
        System.out.println("------------------------------------------------------------");
        if (transactionHistory.size() <= 0) {
            System.out.println("\nApologies, there are currently no transactions to display.");
        }
        // Loop through the transaction history
        for (Transaction transaction : transactionHistory) {
            // Parse transaction date from string to LocalDate object
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            // Check if the transaction date falls within the previous month
            if (transactionDate.isEqual(firstDayOfPreviousMonth) ||
                    (transactionDate.isAfter(firstDayOfPreviousMonth)
                            && transactionDate.isBefore(lastDayOfPreviousMonth.plusDays(1)))) {
                System.out.println(transaction);
            }
        }

        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Generates and displays a report of transactions from the beginning of the
     * year to the current date.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayYearToDateReport(Scanner scanner) throws IOException {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        // Get the first day of the month with its methods
        LocalDate firstDayOfYear = LocalDate.of(currentDate.getYear(), Month.JANUARY, 1);
        // Display report header
        System.out.println("------------------------------------------------------------");
        System.out.printf("\t\tReports from %s through %s%n", firstDayOfYear, currentDate);
        System.out.println("------------------------------------------------------------");
        if (transactionHistory.size() <= 0) {
            System.out.println("\nApologies, there are currently no transactions to display.");
        }
        // loop through the ArrayList
        for (Transaction transaction : transactionHistory) {
            // Parse transaction date from string to LocalDate object
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());
            // Check if the date is equal to today or is after the first of current month
            // AND is before the current date
            if (transactionDate.isEqual(currentDate)
                    || (transactionDate.isAfter(firstDayOfYear) && transactionDate.isBefore(currentDate))) {
                System.out.println(transaction);
            }
        }

        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Generates and displays a report of transactions from the previous year.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void displayPreviousYearReport(Scanner scanner) throws IOException {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Get the first day of the previous year
        LocalDate firstDayOfPreviousYear = currentDate.minusYears(1).withDayOfYear(1);

        // Get the last day of the previous year
        LocalDate lastDayOfPreviousYear = firstDayOfPreviousYear.plusYears(1).minusDays(1);
        // Calculates and assigns the last day of the previous month to the variable

        // Display report header
        System.out.println("------------------------------------------------------------");
        System.out.printf("\t\tReports from %s through %s%n", firstDayOfPreviousYear, lastDayOfPreviousYear);
        System.out.println("------------------------------------------------------------");
        if (transactionHistory.size() <= 0) {
            System.out.println("\nApologies, there are currently no transactions to display.");
        }
        // Loop through the transaction history
        for (Transaction transaction : transactionHistory) {
            // Parse transaction date from string to LocalDate object
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            // Check if the transaction date falls within the previous month
            if (transactionDate.isEqual(firstDayOfPreviousYear) ||
                    (transactionDate.isAfter(firstDayOfPreviousYear)
                            && transactionDate.isBefore(lastDayOfPreviousYear.plusDays(1)))) {
                System.out.println(transaction);
            }
        }

        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Searches for transactions by vendor name and displays the matching entries.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void searchByVendor(Scanner scanner) throws IOException {
        // Prompt the user to enter the name of the vendor to search for
        String userVendorInput = UtilityMethods
                .validateStringInput(scanner, "Please enter the name of the vendor you wish to search for: ")
                .toUpperCase();
        // loop through the ArrayList to find matching results
        Collections.sort(transactionHistory, Comparator.comparing(Transaction::getDate).reversed());
        boolean vendorFound = false;
        for (Transaction transaction : transactionHistory) {
            // Check if the vendor name contains the user's input (case-insensitive)
            // You can also do .equalIgnore();
            if (transaction.getVendor().toUpperCase().contains(userVendorInput)) {
                vendorFound = true;
                System.out.println(transaction);
            }
        }

        // Display an error message if no vendor is found
        if (!vendorFound) {
            System.out.println("\nNo transactions found for the vendor: " + userVendorInput);
        }
        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Allows the user to perform a custom search based on specified criteria and
     * displays the matching transactions.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void customReportSearch(Scanner scanner) throws IOException {
        // Declare variables to store user input and search criteria
        boolean foundMatch = false; // a boolean variable to track if any matches were found
        double convertedAmountInput = 0.0;
        // Prompt the user to enter search criteria
        System.out.print("Reports - Please Insert the following Search Criteria: ");

        // Validate start date input
        String userStartDateInput = UtilityMethods.validateDateFormat(scanner,
                "\nStart date (YYYY-MM-DD) (press Enter to skip): ", true);
        LocalDate checkStartDate = userStartDateInput.isEmpty() ? null : LocalDate.parse(userStartDateInput);

        // Validate end date input
        String userEndDateInput = UtilityMethods.validateDateFormat(scanner,
                "End date (YYYY-MM-DD) (press Enter to skip): ", true);
        LocalDate checkEndDate = userEndDateInput.isEmpty() ? null : LocalDate.parse(userEndDateInput);

        // Validate description input
        String checkDescription = UtilityMethods
                .validateStringInput(scanner, "Description (press Enter to skip): ", true).toLowerCase();

        // Validate vendor input
        String checkVendor = UtilityMethods.validateStringInput(scanner, "Vendor (press Enter to skip): ", true)
                .toLowerCase();
        // Validate amount input
        convertedAmountInput = UtilityMethods.validateDoubleInput(scanner, "Amount (press Enter to skip): ", true);

        // Display header for a custom search report
        System.out.println("------------------------------------------------------------");
        System.out.println("\t\t\t\tYour Custom Search Report");
        System.out.println("------------------------------------------------------------");
        Collections.sort(transactionHistory, Comparator.comparing(Transaction::getDate).reversed());
        for (Transaction transaction : transactionHistory) {
            // Parses the date string from the Transaction object to a LocalDate object.
            // This allows us to perform date comparisons.
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            // Check if the transaction matches the criteria
            boolean isStartDateMatched = checkStartDate == null || !transactionDate.isBefore(checkStartDate);
            boolean isEndDateMatched = checkEndDate == null || !transactionDate.isAfter(checkEndDate);
            boolean isDescriptionMatched = checkDescription.isEmpty()
                    || transaction.getDescription().toLowerCase().contains(checkDescription);
            boolean isVendorMatched = checkVendor.isEmpty()
                    || transaction.getVendor().toLowerCase().contains(checkVendor);
            boolean isAmountMatched = convertedAmountInput == 0.0 || transaction.getAmount() == convertedAmountInput;

            // If all criteria are matched, display the transaction
            if (isStartDateMatched && isEndDateMatched && isDescriptionMatched && isVendorMatched && isAmountMatched) {
                // Print the matched transaction here
                foundMatch = true; // Set foundMatch to true if a match is found
                System.out.println(transaction);
            }
        }

        // If no matches are found, display a message
        if (!foundMatch) {
            System.out.println("\nNo transactions found matching the criteria.");
        }

        // Return to the home screen
        goToHomeScreen(scanner);
    }

    /**
     * Redirects the user to the home screen or other options based on their choice.
     *
     * @param scanner The Scanner object for user input.
     * @throws IOException If an I/O error occurs.
     */
    public static void goToHomeScreen(Scanner scanner) throws IOException {
        // Display menu options for the user
        while (true) {
            System.out.println("\nPlease select your next action:");
            System.out.println("1. Go back home");
            System.out.println("2. View reports");
            System.out.println("3. Exit Program");
            System.out.print("Enter your choice: ");
            String userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1":
                    // Go back home
                    displayHomeScreen(scanner);
                    return; // Break out of the loop
                case "2":
                    // View other ledger reports
                    displayReports(scanner);
                    return; // Break out of the loop
                case "3":
                    // Exit
                    System.out.println("Exiting....");
                    return; // Break out of the loop
                default:
                    System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        }
    }

}

/*
 * -resources
 * How to write method comments--
 * https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html
 * How to sort ArrayList by ASC --
 * https://www.bezkoder.com/java-sort-arraylist-of-objects/
 * How to check dates before or after--
 * https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
 * How to write a good read me-- https://www.makeareadme.com/
 */
