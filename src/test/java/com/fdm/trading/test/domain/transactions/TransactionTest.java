package com.fdm.trading.test.domain.transactions;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import com.fdm.trading.service.stocksServiceImpl.StocksService;
import com.fdm.trading.service.transactionService.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionTest {

    @Autowired
    AccountService accountService;
    @Autowired
    StocksService stocksService;
    @Autowired
    TransactionService transService;

    @Test
    public void PersistTrans(){
       transService.createPurchaseTransaction(1, 1, 10);
    }

    @Test
    public void fail_A_Transaction_If_No_Stocks_Available(){
        Stocks stocks = new Stocks();
        stocks = stocksService.findByStockId(1);
        double result1 = stocks.getVolume();
        transService.createPurchaseTransaction(1, 1, 900);

        assertEquals(result1, 10, 0);
    }

    @Test
    public void fail_A_Transaction_If_No_Funds(){
        Account account = new Account();
        account = accountService.findByAccountId(1);
        double result1 = account.getAccountBalance();
        transService.createPurchaseTransaction(1, 1, 900);

        assertEquals(result1, 150, 0);
    }

}
