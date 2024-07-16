package com.pluralsight.data.mysql;

import com.pluralsight.models.User;
import com.pluralsight.data.interfaces.UserDao;

import java.sql.*;

import static com.pluralsight.AccountingLedger.basicDataSource;

/**
 * Implementation of UserDao interface for MySQL database operations related to users.
 */
// ~~~~~~~~~~~~~~~~~ Authored by Staphon ~~~~~~~~~~~~~~~~~~~~~~~~~
public class MySqlUserDao implements UserDao {

    // Column names in the 'users' table
    // "user_id"
    // "username"
    // "password"

    /**
     * Authenticates a user by verifying the provided username and password.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return A User object if authentication succeeds; null otherwise.
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
            System.out.println("\n *** Invalid Credentials *** \n -- Please try again 😄 --");
        }
        // Return the User object (or null if the login failed)
        return user;
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The username of the new user to register.
     * @param password The password of the new user to register.
     * @return A User object representing the newly registered user if successful; null otherwise.
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
            System.out.println("\n *** Were sorry this current username is already taken. *** \n \t\t\t\t-- Please try another one --");
        }
        // Return the created User object (or null if the registration failed)
        return user;
    }

}
