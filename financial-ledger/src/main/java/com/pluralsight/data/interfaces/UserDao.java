package com.pluralsight.data.interfaces;

import com.pluralsight.models.User;

/**
 * Interface defining operations for user authentication and registration.
 */
public interface UserDao {

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username The username of the user to authenticate.
     * @param password The password associated with the username.
     * @return A User object representing the authenticated user, or null if authentication fails.
     */
    User userLogin(String username, String password);

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The username of the new user to register.
     * @param password The password of the new user to register.
     * @return A User object representing the newly registered user, or null if registration fails.
     */
    User userRegister(String username, String password);
}
