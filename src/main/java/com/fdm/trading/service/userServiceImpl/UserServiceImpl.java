package com.fdm.trading.service.userServiceImpl;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.dao.VerificationTokenDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import com.fdm.trading.events.RegistrationListener;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.security.CustomSecurityUser;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthoritiesDao authoritiesDao;

    @Autowired
    private VerificationTokenDao tokenDao;

    @Autowired
    private CardServiceImpl cardService;



    public User createNewUser(BindingResult result, User user, String role, CreditCard card) throws NameFormatException, UnsecurePasswordException, UserAlreadyExistException {
        List<CreditCard> cards = new ArrayList<>();
        if(hasErrors(result, user)) {
            return null;
        }
        if(userDao.findByUsername(user.getUsername())!=null){
            throw new UserAlreadyExistException("User " + user.getUsername() + " already exists, please try a different username");
        }
        Account account = null;
        if(!role.equals("ROLE_ADMIN")){
            account = accountService.createAnAccount();
        }else {
            user.setEnabled(true);
        }

        user.setAccount(account);
        user.setPassword(encoder.encode(user.getPassword()));
        Authorities a = new Authorities();
        a.setAuthority(role);
        cards.add(card);
        user.setCreditCard(cards);
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
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        logger.debug("{} enabled: {}",user.getUsername(), user.isEnabled());
        userDao.save(user);
    }

    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;

            User user = userDao.findByUsername(username);

        try {
            if (user == null)

                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword().toLowerCase(),
                        user.isEnabled(),
                        accountNonExpired,
                        credentialsNonExpired,
                        accountNonLocked,
                        user.getUserAuthorities());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new CustomSecurityUser(user);
    }


    public boolean inputValidator(String input) throws NameFormatException{
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher checkForDigit = digit.matcher(input);
        Matcher checkForSpecial = special.matcher(input);
        boolean hasDigit = checkForDigit.find();
        boolean hasSpecial = checkForSpecial.find();

            if (hasDigit){
                logger.info("Name {} contains digits ", input);
                throw new NameFormatException("username contains digits");

            }else if (hasSpecial){
                logger.info("Name {} contains special characters ", input);
                throw new NameFormatException("username contains special characters");
            }
        return true;
    }

    public boolean isUserPasswordSecure(User user) throws UnsecurePasswordException  {
            if (user.getPassword().toLowerCase().contains("password")) {

                throw new UnsecurePasswordException(user.getUsername() + " has a password which contains 'password'");
            }
            if (user.getPassword().toLowerCase().contains(user.getFirstName())) {
                throw new UnsecurePasswordException(user.getUsername() + " has a password which contains their first name");
            }
            if (user.getPassword().toLowerCase().contains(user.getSurname())) {
                throw new UnsecurePasswordException(user.getUsername() + " has a password which contains their surname");
            }
            return true;
    }


    public boolean hasErrors(BindingResult result, User user) throws NameFormatException, UnsecurePasswordException{
        return result.hasErrors() || !isUserPasswordSecure(user) || !inputValidator(user.getFirstName()) || !inputValidator(user.getSurname());
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenDao.findByToken(VerificationToken);
    }


    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenDao.save(myToken);
    }

    public Set<String> getUserAuthorities(User user) {
        Set<Authorities> userAuthorities = user.getUserAuthorities();
        Set<String> auths = new HashSet<>();
        for(Authorities auth: userAuthorities){
            auths.add(auth.getAuthority());
        }
        return auths;
    }


}
