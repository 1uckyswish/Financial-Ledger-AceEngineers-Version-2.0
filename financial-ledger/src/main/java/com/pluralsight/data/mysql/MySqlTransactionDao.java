package com.pluralsight.data.mysql;

import com.pluralsight.Transaction;
import com.pluralsight.data.TransactionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.pluralsight.AccountingLedger.basicDataSource;

public class MySqlTransactionDao implements TransactionDao {
    //column names
    // "date"
    // "description"
    // "vendor"
    // "amount"
    // "transaction_id"
    // "user_id"

    // SELECT * from transactions WHERE user_id = 1;
    // SELECT * from transactions WHERE user_id = 1 AND amount > 0;
    // SELECT * from transactions WHERE user_id = 1 AND amount < 0;

//    String transactionId = resultSet.getString("transaction_id");
//    String userId = resultSet.getString("user_id");


// ~~~~~~~~~~~~~~~~~ Authored by Tina and Zamir ~~~~~~~~~~~~~~~~~~~~~
    @Override
    public List<Transaction> getAllTransactions(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try{
            String sql = "SELECT * from transactions WHERE user_id = ?";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                // no need to set string
                preparedStatement.setInt(1,userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
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

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }

    @Override
    public List<Transaction> getAllDeposits(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try{
            String sql = "SELECT * from transactions WHERE user_id = 1 AND amount > 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                // no need to set string
                preparedStatement.setInt(1,userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
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

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }

    @Override
    public List<Transaction> getAllPayments(int userId) {
        ArrayList allTransactions = new ArrayList<>();
        try{
            String sql = "SELECT * from transactions WHERE user_id = 1 AND amount < 0";
            try (Connection connection = basicDataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                // no need to set string
                preparedStatement.setInt(1,userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
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

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return allTransactions;
    }
}
