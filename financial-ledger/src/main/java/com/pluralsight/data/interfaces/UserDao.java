package com.pluralsight.data.interfaces;

import com.pluralsight.models.User;

public interface UserDao {
    User userLogin(String username, String password);
    User userRegister(String username, String password);
}
