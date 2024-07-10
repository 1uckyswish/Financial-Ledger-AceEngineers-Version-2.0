package com.pluralsight.models;

/**
 * User model
 */
public class User {
    private int userId;
    private String username;

    /**
     *  Constructor for User
     * @param userId
     * @param username
     */
    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * Getters and setters
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * toString
     * @return
     */
    @Override
    public String toString() {
        return "userId=" + userId +
                ", username='" + username;
    }
}
