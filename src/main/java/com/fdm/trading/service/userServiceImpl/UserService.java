package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.UserRole;

import java.util.List;
import java.util.Set;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUserId(long userId);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    List<User> findUserList();

    void enableUser(String username);

    void disableUser(String username);

    void removeUser(User user);

    Account createNewAccount(User user);
}
