package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.security.CustomSecurityUser;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthoritiesDao authoritiesDao;


    public User createNewUser(String firstName, String surname, String email, String username, String password, boolean enabled, String role){
        User user = new User();
        Account account = null;
        if(!role.equals("ROLE_ADMIN")){
            account = accountService.createAnAccount();
        }
        user.setEnabled(enabled);
        user.setAccount(account);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        Authorities a = new Authorities();
        a.setAuthority(role);
        a.setUser(user);
        userDao.save(user);
        authoritiesDao.save(a);
        return user;
    }

    public User createNewUserAlt(User user, String role){
        Account account = null;
        System.out.println("Creating new user");
        if(!role.equals("ROLE_ADMIN")){
            account = accountService.createAnAccount();
        }
        user.setAccount(account);
        user.setPassword(encoder.encode(user.getPassword()));
        Authorities a = new Authorities();
        a.setAuthority(role);
        a.setUser(user);
        userDao.save(user);
        authoritiesDao.save(a);
        return user;
    }


    public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
    public User findByUserId(long userId){
        return userDao.findByUserId(userId);
    }

    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser(String username) {
        User user = findByUsername(username);
        System.out.println(">>>>>>>>>>>>enableUser"+user.isEnabled());
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(">>>>>>>>>>>>disableUser"+user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }

    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username and or password was incorrect.");

        return new CustomSecurityUser(user);
    }

}
