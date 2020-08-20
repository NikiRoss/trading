package com.fdm.trading.test.domain.user;

import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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


    @Test
    public void create_User(){
        userService.createNewUser("niki", "ross", "e", "niki84", "password238", true, "ROLE_USER");
    }

    @Test
    public void create_Admin(){
        userService.createNewUser("M", "H", "e", "MH90", "password", true, "ROLE_ADMIN");
    }


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


}
