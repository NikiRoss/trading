package com.fdm.trading.test.domain.transactions;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.exceptions.InsufficientFundsException;
import com.fdm.trading.exceptions.LimitedStockException;
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

    private static final int ZERO = 0;


    @Test(expected = LimitedStockException.class)
    public void fail_A_Transaction_If_No_Stocks_Available() throws LimitedStockException, InsufficientFundsException {
        Stocks stocks = stocksService.findByStockId(6);
        stocks.setVolume(10);
        transService.createPurchaseTransaction(6, 2, 90);
    }

    @Test(expected = InsufficientFundsException.class)
    public void fail_A_Transaction_If_Not_Enough_Funds_Available() throws LimitedStockException, InsufficientFundsException {
        Account account = accountService.findByAccountId(3);
        account.setAccountBalance(ZERO);
        transService.createPurchaseTransaction(8, 41, 3);
        assertEquals(account.getAccountBalance(), ZERO, ZERO);
    }

    @Test
    public void create_A_Purchase_Transaction() throws LimitedStockException, InsufficientFundsException {
        transService.createPurchaseTransaction(6, 2, 9);
        List<Transaction> transactions = transService.listOfHeldStocks(6, 2);
        assertTrue(transactions.get(ZERO).getVolume() == 9);
    }

    @Test
    public void create_A_sale_Transaction(){
        transService.createSaleTransaction(7, 2,47);
        List<Transaction> transactions = transService.getLatestTransaction();
        assertTrue(transactions.get(ZERO).getVolume() == 9);
    }

    @Test
    public void ReturnListOfTransactions(){
        List<Transaction> transactionList = transService.listOfAccountTransactions(2);
        int result = transactionList.size();
        assertTrue(result > ZERO);
    }

    @Test
    public void ReturnListOfStocks(){
        List<Transaction> transactionList = transService.listOfHeldStocks(2, 1);
        int result = transactionList.size();
        assertTrue(result > ZERO);
    }

    @Test
    public void ReturnListOfPurchases(){
        List<Transaction> transactionList = transService.listOfAccountPurchases(2);
        assertTrue(transactionList.size() > ZERO);
    }

    @Test
    public void ReturnListOfSales(){
        List<Transaction> transactionList = transService.listOfAccountSales(2);
        assertTrue(transactionList.size() > ZERO);
    }
}
