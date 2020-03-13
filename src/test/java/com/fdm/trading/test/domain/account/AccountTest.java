package com.fdm.trading.test.domain.account;

import com.fdm.trading.domain.Account;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void create_An_Account(){
        Account a = accountService.createAnAccount();
        long val1 = a.getAccountId();
        assertNotNull(val1);
        accountService.deleteAccountById(val1);
    }

    @Test
    public void find_Account_By_Id(){
        Account a = accountService.findByAccountId(1);
        String val1 = a.getAccountNumber();
        assertEquals(val1, "441739");
    }

    @Test
    public void find_Account_By_AccountNumber(){
        Account a = accountService.findByAccountNumber("441739");
        long val1 = a.getAccountId();
        assertEquals(val1, 1);
    }


    @Test
    public void ReturnAStockFromList(){

        
        assertNotNull(accountService.retrieveStockFromList(1));
        System.out.println(accountService.retrieveStockFromList(1).indexOf(0));

    }

}
