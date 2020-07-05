package com.fdm.trading.test.domain.account;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private StockServiceImpl stockService;

    @Test
    public void create_An_Account(){
        Account a = accountService.createAnAccount();
        long val1 = a.getAccountId();
        assertNotNull(val1);
        accountService.deleteAccountById(val1);
    }

    @Test
    public void find_Account_By_Id(){
        Account a = accountService.findByAccountId(3);
        String val1 = a.getAccountNumber();
        assertEquals(val1, "669020");
    }

    @Test
    public void find_Account_By_AccountNumber(){
        Account a = accountService.findByAccountNumber("669020");
        long val1 = a.getAccountId();
        assertEquals(val1, 3);
    }

    @Test
    public void return_Account_Balance(){
        Account a =accountService.findByAccountId(1);
        double val = a.getAccountBalance();
        assertEquals(val, 88891, 0);
    }


    @Test
    public void return_Held_Stock_For_An_Account(){
        List<StockListEntity> accountHoldings = stockService.getStockList(1);
        assertNotNull(accountHoldings);
    }

    @Test
    public void generate_Random_Account_Number(){
        String val1 = accountService.accountNumberGenerator();
        String val2 = accountService.accountNumberGenerator();
        System.out.println(val1);
        System.out.println(val2);
        assertNotEquals(val1, val2);
    }



}
