package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import java.util.List;
import java.util.Set;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    User saveUser(User user);

    List<User> findUserList();

    void enableUser(String username);

    void disableUser(String username);
}
