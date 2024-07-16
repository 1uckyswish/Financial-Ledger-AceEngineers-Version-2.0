package com.pluralsight.data.mysql;

import com.pluralsight.models.User;
import com.pluralsight.data.interfaces.UserDao;

import java.sql.*;

//sql
import static com.pluralsight.AccountingLedger.basicDataSource;

// ~~~~~~~~~~~~~~~~~ Authored by Staphon ~~~~~~~~~~~~~~~~~~~~~~~~~
public class MySqlUserDao implements UserDao {
    // column names
    // "user_id"
    // "username"
    // "password"

    /**
     * Login a user with the provided username and password.
     * @param username
     * @param password
     * @return
     */
    @Override
    public User userLogin(String username, String password) {
        // SQL query to select a user with the provided username and password
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?;";
        User user = null; // Initialize the User object to null

        try (Connection connection = basicDataSource.getConnection();
             // Prepare the SQL statement
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the username and password parameters in the prepared statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                // Retrieve the user ID and username from the result set
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("username");

                // Create a new User object with the retrieved user ID and username
                user = new User(userId, username);
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during the process
            System.out.println("Error!");
        }
        // Return the User object (or null if the login failed)
        return user;
    }

    /**
     * Register a new user with the provided username and password.
     * @param username
     * @param password
     * @return
     */
    // Authored by Tina
    @Override
    public User userRegister(String username, String password) {
        // SQL query to insert a new user with the provided username and password
        String sql = "INSERT INTO users (username, password) VALUES (?, ?);";
        User user = null; // Initialize the User object to null

        try (Connection connection = basicDataSource.getConnection();
             // Prepare the SQL statement and specify that we want to return generated keys
             PreparedStatement preparedStatement = connection.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set the username and password parameters in the prepared statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // Execute the update and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if any rows were affected (i.e., if the insertion was successful)
            if (rowsAffected > 0) {
                // Get the generated keys (in this case, the user ID)
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        // Retrieve the generated user ID from the result set
                        int userId = resultSet.getInt(1);
                        // Create a new User object with the generated ID and the provided username
                        user = new User(userId, username);
                    }
                }
            } else {
                // If no rows were affected, print a failure message
                System.out.println("Failed to insert user: " + username);
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during the process
            System.out.println(
                    "\nUser registration failed: Unable to register user due to a database error. \n\t\t\tPlease try again with a different username.");
        }
        // Return the created User object (or null if the registration failed)
        return user;
    }

}
