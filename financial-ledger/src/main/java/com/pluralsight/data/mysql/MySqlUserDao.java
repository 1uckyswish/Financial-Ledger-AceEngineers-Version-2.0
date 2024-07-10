package com.pluralsight.data.mysql;

import com.pluralsight.models.User;
import com.pluralsight.data.interfaces.UserDao;

import java.sql.*;

//sql
import static com.pluralsight.AccountingLedger.basicDataSource;

// ~~~~~~~~~~~~~~~~~ Authored by Staphon ~~~~~~~~~~~~~~~~~~~~~~~~~
public class MySqlUserDao implements UserDao {
    //column names
    // "user_id"
    // "username"
    // "password"

    //SELECT * FROM users WHERE username = ? AND password = ?;
    @Override
    public User userLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?;";
        User user = null;
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("username");

                user = new User(userId,username);
            }

        } catch (SQLException e){
            System.out.println("Error!");
        }
        return user;

    }

    // INSERT INTO users (username, password) VALUES ("Tina", "456");
    // Authored by Tina
    @Override
    public User userRegister(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?);";
        User user = null;
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt(1);
                        user = new User(userId, username);
                    }
                }
            } else {
                System.out.println("Failed to insert user: " + username);
            }

        }catch (SQLException e) {
            System.out.println("\nUser registration failed: Unable to register user due to a database error. \n\t\tPlease try again with a different username.");
        }
        return user;
    }


}
