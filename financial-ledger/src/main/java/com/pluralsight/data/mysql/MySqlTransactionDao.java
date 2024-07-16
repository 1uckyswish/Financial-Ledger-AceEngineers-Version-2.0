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

public class MySqlTransactionDao implements TransactionDao {
    // column names
    // "date"
    // "description"
    // "vendor"
    // "amount"
    // "transaction_id"
    // "user_id"


    /**
     * Get all transactions
     * @param userId
     * @return
     */
    // ~~~~~~~~~~~~~~~~~ Authored by Tina and Zamir ~~~~~~~~~~~~~~~~~~~~~
    @Override
    public List<Transaction> getAllTransactions(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try {
            String sql = "SELECT * from transactions WHERE user_id = ?";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // no need to set string
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");

                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allTransactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }

    // column names
    // "date"
    // "description"
    // "vendor"
    // "amount"
    // "transaction_id"
    // "user_id"

    /**
     * Create new transaction
     * @param transaction
     * @param userId
     * @return
     */
    // ~~~~~~~~~~~~~~~~~ Authored by Staphon ~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Transaction createTransaction(Transaction transaction, int userId) {
        String sql = "INSERT INTO transactions (user_id, date, description, vendor, amount) VALUES (?, ?, ?, ?, ?)";
        Transaction createNewTransaction = null;

        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String date = transaction.getDate() + " " + transaction.getTime();

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, transaction.getDescription());
            preparedStatement.setString(4, transaction.getVendor());
            preparedStatement.setDouble(5, transaction.getAmount());

            // Use executeUpdate() for INSERT, UPDATE, DELETE statements
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                createNewTransaction = transaction; // Return the created transaction if insertion was successful
            }

        } catch (SQLException e) {
            System.out.println("Error adding new transaction");
            e.printStackTrace(); // Print the stack trace for debugging
        }

        return createNewTransaction;
    }

    /**
     * Get all deposits
     * @param userId
     * @return
     */
    @Override
    public List<Transaction> getAllDeposits(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try {
            String sql = "SELECT * from transactions WHERE user_id = 1 AND amount > 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // no need to set string
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");
                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allTransactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }

    /**
     * Get all payments
     * @param userId
     * @return
     */
    @Override
    public List<Transaction> getAllPayments(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try {
            String sql = "SELECT * from transactions WHERE user_id = 1 AND amount < 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // no need to set string
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String dateTime = resultSet.getString("date");
                    String description = resultSet.getString("description");
                    String vendor = resultSet.getString("vendor");
                    double amount = resultSet.getDouble("amount");
                    String[] splittingDateTime = dateTime.split(" ");
                    String date = splittingDateTime[0];
                    String time = splittingDateTime[1];
                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    allTransactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }
}
