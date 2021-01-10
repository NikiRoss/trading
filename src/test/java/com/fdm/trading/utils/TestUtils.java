package com.fdm.trading.utils;

import com.fdm.trading.domain.User;

public class TestUtils {
    public static User getUser(long id, String username, String email, String password) {

        User user = new User();
        user.setUserId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
