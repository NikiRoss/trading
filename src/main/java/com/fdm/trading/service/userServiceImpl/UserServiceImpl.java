package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.UserRole;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountServiceImpl accountService;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User findByUserId(long userId) {
        return userDao.findByUserId(userId);
    }

    @Override
    public boolean checkUserExists(String username, String email) {
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (null != findByUsername(email)) {
            return true;
        }
        return false;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public List<User> findUserList() {
        return userDao.findAll();
    }

    @Override
    public void enableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    @Override
    public void disableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }

    @Override
    public void removeUser(User user) {
        userDao.delete(user);
    }

    @Override
    public Account createNewAccount(User user) {
        Account a = accountService.createAnAccount();
        user.setAccount(a);
        userDao.save(user);
        return a;
    }

    public Account findByAccountId(long accountId) {
        return accountService.findByAccountId(accountId);
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }
}
