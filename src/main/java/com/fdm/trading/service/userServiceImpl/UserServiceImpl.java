package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Role;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.PasswordEncryption;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountServiceImpl accountService;


    public User createNewUser(String firstName, String surname, String email, String username, String password, Role role){
        User user = new User();
        Account account = accountService.createAnAccount();
        user.setRole(role);
        user.setEnabled(false);
        user.setAccount(account);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setSurname(surname);
        user.setUsername(username);
        String encrypted = PasswordEncryption.hashPassword(password).get();
        user.setPassword(encrypted);
        userDao.save(user);
        return user;
    }


    public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }

    public boolean validateUser(User user, String password) {
        String key = user.getPassword();
        return PasswordEncryption.verifyPassword(password, key);
    }

}
