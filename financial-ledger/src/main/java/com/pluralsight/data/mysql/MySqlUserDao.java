package com.pluralsight.data.mysql;

import com.pluralsight.User;
import com.pluralsight.data.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    @Override
    public User userRegister(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?);";
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
            System.out.println("Sorry Username Already Exists");
        }
        return user;
    }
}
