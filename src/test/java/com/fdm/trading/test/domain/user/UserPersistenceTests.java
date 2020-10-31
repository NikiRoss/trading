package com.fdm.trading.test.domain.user;

import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
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

/*    @Test
    public void check_A_Username_Exists(){
        boolean check = userService.checkUsernameExists("NR84");
        assertTrue(check);
    }

    @Test
    public void check_A_User_Exists(){
        boolean check = userService.checkUserExists("NR84", "niki@email.com");
        assertTrue(check);
    }

    @Test
    public void check_A_User_Exists_Method_Returns_False(){
        boolean check = userService.checkUserExists("wrong", "credentials");
        assertFalse(check);
    }*/

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

/*    @Test
    public void testUsername_Validator(){
        List<User> usersList = userService.findAllUsers();
        User user = usersList.get(4);
        boolean validate = userService.inputValidator(user.getUsername());
        System.out.println(validate + " " + user.getUsername());
    }

    @Test
    public void testPasswordSecurity(){
        User user = new User();
        user.setSurname("ross");
        user.setFirstName("niki");
        user.setPassword("ross83995030");
        boolean res = userService.isUserPasswordSecure(user);
        System.out.println(">>>>> is password secure? " + res);
    }*/

    @Test
    public void sendMail(){
        List<User> usersList = userService.findAllUsers();
        User user = usersList.get(4);
        System.out.println(user.toString());
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("niki.ross49@gmail.com");
        message.setTo("niki.ross49@gmail.com");
        message.setSubject("Activate Account");
        message.setText("Hi " + user.getFirstName() + " please click below to activate your email");
        mailSender.send(message);
    }

    @Test
    public void createAdmin() throws NameFormatException, UnsecurePasswordException, UserAlreadyExistException {
        User admin = new User();
        admin.setUsername("username");
        admin.setPassword("ruby");
        admin.setFirstName("Niki");
        admin.setSurname("Ross");
        Authorities authorities = new Authorities();
        authorities.setAuthority("ROLE_USER");
        authorities.setUser(admin);
        Set<Authorities> authoritiesSet = new HashSet<>();
        authoritiesSet.add(authorities);
        admin.setUserAuthorities(authoritiesSet);
        admin.setEmail("email@emial.com");
        userService.createNewUserAlt(null, admin, "ROLE_USER");
    }

    @Test
    public void create_User_with_DOB(){
        String dob = userService.dateFormating("14/02/1984");

        System.out.println(dob.toString());
    }


}
