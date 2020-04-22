package com.fdm.trading.test.domain.transactions;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import com.fdm.trading.service.stocksServiceImpl.StocksService;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionTest {

    @Autowired
    AccountService accountService;
    @Autowired
    StocksService stocksService;
    @Autowired
    TransactionServiceImpl transService;

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
    public void Create_A_Purchase_Transaction(){
        transService.createPurchaseTransaction(2, 1, 20);
    }

    @Test
    public void ReturnListOfTransactions(){
        List<Transaction> transactionList = transService.listOfAccountTransactions(2);
        int result = transactionList.size();
        System.out.println(transactionList.size());
        assertTrue(result > 0);
    }

    @Test
    public void ReturnListOfStocks(){
        List<Transaction> transactionList = transService.listOfHeldStocks(2, 1);
        int result = transactionList.size();
        System.out.println(transactionList.size());
        assertTrue(result > 0);
    }

    @Test
    public void sale_Transaction(){
        transService.createSaleTransaction(2, 1,20);
    }


}
