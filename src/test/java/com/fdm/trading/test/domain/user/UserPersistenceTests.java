package com.fdm.trading.test.domain.user;

import com.fdm.trading.dao.RoleDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.Role;
import com.fdm.trading.security.UserRole;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
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
    private RoleDao roleDao;

    @Test
    public void create_A_New_User(){
        //userService.createNewUser("Ian", "Ross", "ian.ross@email.com", "ianR", "Password1");
        User user = userService.findByUsername("ianR");
        String pwd = user.getPassword();

        assertEquals(pwd, "Password1");
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

    @Test
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
    public void create_New_User_With_Spring_Security(){
        User u = new User();
        u.setFirstName("Martyn");
        u.setSurname("Hunter");
        u.setUsername("Marty90");
        u.setPassword("password1");
        u.setEmail("m.hunter@email.com");
        u.setEnabled(true);
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("BASIC");
        roleDao.save(role1);
        roleDao.save(role2);
        UserRole ur = new UserRole();
        ur.setRole(role1);
        UserRole ur1 = new UserRole();
        ur1.setRole(role2);
        Set<UserRole> roleSet = new HashSet<>();
        roleSet.add(ur1);
        roleSet.add(ur);
        u.setUserRoles(roleSet);
        userService.createUser(u, roleSet);
    }

    @Test
    public void createRoles(){
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("BASIC");
        roleDao.save(role1);
        roleDao.save(role2);
    }

}
