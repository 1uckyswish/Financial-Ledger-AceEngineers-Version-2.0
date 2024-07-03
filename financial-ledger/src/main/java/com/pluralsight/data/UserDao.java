package com.pluralsight.data;

import com.pluralsight.User;

public interface UserDao {
    User userLogin(String username, String password);
    User userRegister(String username, String password);
}
