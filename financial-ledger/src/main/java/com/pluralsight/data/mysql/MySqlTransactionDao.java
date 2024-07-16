package com.pluralsight.data.mysql;

import com.pluralsight.models.Transaction;
import com.pluralsight.data.interfaces.TransactionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.pluralsight.AccountingLedger.basicDataSource;

/**
 * Implementation of TransactionDao interface for MySQL database.
 * Provides methods to retrieve, create, and categorize transactions.
 */
public class MySqlTransactionDao implements TransactionDao {

    // Column names in the 'transactions' table
    // "date"
    // "description"
    // "vendor"
    // "amount"
    // "transaction_id"
    // "user_id"

    /**
     * Retrieves all transactions associated with a specific user from the database.
     *
     * @param userId The ID of the user whose transactions are to be retrieved.
     * @return A list of Transaction objects representing all transactions for the user.
     */
    // ~~~~~~~~~~~~~~~~~ Authored by Tina and Zamir ~~~~~~~~~~~~~~~~~~~~~
    @Override
    public List<Transaction> getAllTransactions(int userId) {
        ArrayList<Transaction> allTransactions = new ArrayList<>();
        try {
            String sql = "SELECT * FROM transactions WHERE user_id = ?";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");

                    // Split date and time from dateTime string
                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];

                    // Create a Transaction object and add it to the list
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allTransactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to retrieve transactions.");
        }
        return allTransactions;
    }

    /**
     * Creates a new transaction record in the database for a specific user.
     *
     * @param transaction The Transaction object representing the new transaction.
     * @param userId      The ID of the user creating the transaction.
     * @return The Transaction object representing the newly created transaction if successful, null otherwise.
     */
    // ~~~~~~~~~~~~~~~~~ Authored by Staphon ~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Transaction createTransaction(Transaction transaction, int userId) {
        String sql = "INSERT INTO transactions (user_id, date, description, vendor, amount) VALUES (?, ?, ?, ?, ?)";
        Transaction createdTransaction = null;

        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Combine date and time into a single string for storage
            String dateTime = transaction.getDate() + " " + transaction.getTime();

            // Set parameters for the SQL statement
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, dateTime);
            preparedStatement.setString(3, transaction.getDescription());
            preparedStatement.setString(4, transaction.getVendor());
            preparedStatement.setDouble(5, transaction.getAmount());

            // Execute the SQL statement and check if any rows were affected
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                createdTransaction = transaction; // Return the created transaction if insertion was successful
            }

        } catch (SQLException e) {
            System.out.println("Error adding new transaction.");
            e.printStackTrace(); // Print the stack trace for debugging
        }

        return createdTransaction;
    }

    /**
     * Retrieves all deposit transactions (transactions with positive amounts) for a specific user from the database.
     *
     * @param userId The ID of the user whose deposit transactions are to be retrieved.
     * @return A list of Transaction objects representing all deposit transactions for the user.
     */
    @Override
    public List<Transaction> getAllDeposits(int userId) {
        ArrayList<Transaction> allDeposits = new ArrayList<>();
        try {
            String sql = "SELECT * FROM transactions WHERE user_id = ? AND amount > 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");

                    // Split date and time from dateTime string
                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];

                    // Create a Transaction object and add it to the list
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allDeposits.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to retrieve deposit transactions.");
        }
        return allDeposits;
    }

    /**
     * Retrieves all payment transactions (transactions with negative amounts) for a specific user from the database.
     *
     * @param userId The ID of the user whose payment transactions are to be retrieved.
     * @return A list of Transaction objects representing all payment transactions for the user.
     */
    @Override
    public List<Transaction> getAllPayments(int userId) {
        ArrayList<Transaction> allPayments = new ArrayList<>();
        try {
            String sql = "SELECT * FROM transactions WHERE user_id = ? AND amount < 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");

                    // Split date and time from dateTime string
                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];

                    // Create a Transaction object and add it to the list
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allPayments.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to retrieve payment transactions.");
        }
        return allPayments;
    }
}
