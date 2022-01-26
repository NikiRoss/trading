package com.fdm.trading.test.domain.user;

import com.fdm.trading.dao.AccountDao;
import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import com.fdm.trading.utils.mail.EmailConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPersistenceTests {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private CardServiceImpl cardService;

    private final static String ROLE_USER = "ROLE_USER";



    @Test
    public void find_A_User_By_Username(){
        User val1 = userDao.findByUsername("NR84");
        String val2 = val1.getUsername();
        assertEquals("NR84", val2);
    }

    @Test
    public void find_A_User_By_UserID(){
        User val1 = userDao.findByUserId(12);
        long val2 = val1.getUserId();
        assertEquals(12, val2);
    }

    @Test
    public void enable_A_User(){
        userService.enableUser("NR84");
        User user = userService.findByUsername("NR84");
        assertTrue(user.isEnabled());
    }

    @Test
    public void disable_A_User(){
        userService.disableUser("NR84");
        User user = userService.findByUsername("NR84");
        assertFalse(user.isEnabled());
    }

    @Test
    public void check_Account_Exists(){
        Account a = accountService.findByAccountId(21);
        String val1 = a.getAccountNumber();
        assertEquals(val1, "200129");
    }

    @Test
    public void findUserDetails(){
        User u = new User();
        u = userService.findByUserId(95);
        System.out.println(u.toString());
    }

    //TODO - may delete
//    @Test
//    public void send_Mail_On_Register(){
//        List<User> usersList = userService.findAllUsers();
//        User user = usersList.get(4);
//        System.out.println(user.toString());
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(this.emailConfig.getHost());
//        mailSender.setPort(this.emailConfig.getPort());
//        mailSender.setUsername(this.emailConfig.getUsername());
//        mailSender.setPassword(this.emailConfig.getPassword());
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("niki.ross49@gmail.com");
//        message.setTo("niki.ross49@gmail.com");
//        message.setSubject("Activate Account");
//        message.setText("Hi " + user.getFirstName() + " please click below to activate your email");
//        mailSender.send(message);
//    }

    @Test
    public void createAdmin() throws NameFormatException, UnsecurePasswordException, UserAlreadyExistException {
        User user = new User();
        user.setUsername("nikiross84");
        user.setPassword("testpass");
        user.setFirstName("Niki");
        user.setSurname("Ross");
        Authorities authorities = new Authorities();
        authorities.setAuthority("ROLE_USER");
        authorities.setUser(user);
        Set<Authorities> authoritiesSet = new HashSet<>();
        authoritiesSet.add(authorities);
        user.setUserAuthorities(authoritiesSet);
        user.setEmail("email@emial.com");
        userService.createNewUser(null, user, "ROLE_USER");
    }

    @Test
    public void create_A_User() throws NameFormatException, UnsecurePasswordException, UserAlreadyExistException {
        User user = new User();
        Authorities auth = new Authorities();
        auth.setAuthority("ROLE_USER");
        auth.setUser(user);
        Set<Authorities> authSet = new HashSet<>();
        authSet.add(auth);
        user.setEmail("niki.ross49@gmail.com");
        user.setFirstName("Niki");
        user.setSurname("Ross");
        user.setPassword("testpass");
        user.setEnabled(false);
        user.setUsername("nikiross84");
        Set<CreditCard> card = cardService.registerCreditCard("1234567890009876", "01/22", 890, "Mr N Ross");
        user.setCreditCard(card);
        user.setUserAuthorities(authSet);
        User createdUser = userService.createNewUser(null, user, ROLE_USER);
        User dbUser = userService.findByUserId(createdUser.getUserId());
        //Asserts
        assertNotNull(dbUser);
        assertEquals(dbUser.getUserAuthorities().iterator().next().getAuthority(), ROLE_USER);
    }

    @Test
    public void password_Security_Validation(){
        /**
         * catch any security issues that method 'is user password secure'
         * is designed to catch ie use of the word "password" or
         * inclusion of the users name in their password
         */
    }

    @Test
    public void test_Valid_Name_On_SignUp() throws NameFormatException {
        String name = "Bob";
        boolean validate = userService.inputValidator(name);
        assertTrue(validate);
    }

    @Test
    public void test_Exception_Handling_On_SignUp_Numbers() throws NameFormatException {
        String name = "5050";
        boolean validate = userService.inputValidator(name);
        assertFalse(validate);
    }

    @Test
    public void test_Exception_Handling_On_SignUp_Special() throws NameFormatException {
        String name = "!@@";
        boolean validate = userService.inputValidator(name);
        assertFalse(validate);
    }
}
