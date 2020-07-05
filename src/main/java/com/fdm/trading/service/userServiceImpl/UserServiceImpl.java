package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.dao.RoleDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.UserRole;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


/*    public User createNewUser(String firstName, String surname, String email, String username, String password){
        User user = new User();
        Account account = accountService.createAnAccount();
        user.setEnabled(false);
        user.setAccount(account);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(password);
        userDao.save(user);
        return user;
    }*/

    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());

        if (localUser != null) {
            System.out.println("User with username "+user.getUsername()+" already exist. Nothing will be done. ");
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            user.setAccount(accountService.createAnAccount());

            localUser = userDao.save(user);
        }

        return localUser;
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


    public boolean checkUserExists(String username, String email) {
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }
        return false;
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


    public boolean validateUser(User user, String password){
        System.out.println(user.getPassword());
        if (user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

}
