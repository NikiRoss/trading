package com.fdm.trading.service.transactionService;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.TransactionDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionDao transDao;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private StockServiceImpl stockService;
    @Autowired
    private StockListEntityDao stockListEntityDao;

    @Override
    public Transaction findByTransactionId(long transactionId) {
        return transDao.findByTransactionId(transactionId);
    }

    @Override
    @Transactional
    public Transaction createPurchaseTransaction(int stockId, int accountId, long volume) {
        Stocks stocks = stockService.findByStockId(stockId);	// TODO check isEmpty() first
        Account account = accountService.findByAccountId(accountId);
        Date date = new Date();

        Transaction transaction = new Transaction();
        double balance = account.getAccountBalance();
        double totalCost = volume * stocks.getSharePrice();
        if (totalCost > balance){
            System.out.println("You do not have enough funds to complete this transaction");

        }else if (volume > stocks.getVolume()){
            {
                System.out.println("There are only: " + stocks.getVolume() + " available, please amend your purchase and try again");
            }
        }

        else{
            double balanceAfterDeduction = balance - totalCost;
            long volumeAfterDeduction = stocks.getVolume() - volume;
            account.setAccountBalance(balanceAfterDeduction);
            stocks.setVolume(volumeAfterDeduction);

            account.getStocksList().add(stocks);
            stocks.setAccount(account);
            accountService.save(account);
            stockService.save(stocks);

            transaction.setPrice(totalCost);
            transaction.setVolume(volume);
            transaction.setAccount(account);
            transaction.setDate(date);
            transaction.setPurchase(true);
            transaction.setStocks(stocks);
            this.save(transaction);
        }
        return transaction;
    }

    private void save(Transaction transaction) {
        transDao.save(transaction);
    }

    public Transaction createSaleTransaction(int stockId, int accountId, long volume){
        Stocks stocks = stockService.findByStockId(stockId);	// TODO check isEmpty() first
        Account account = accountService.findByAccountId(accountId);
        Date date = new Date();

        Transaction transaction = new Transaction();
        double totalAmount = volume * stocks.getSharePrice();
            transaction.setPrice(totalAmount);
            transaction.setVolume(volume);
            transaction.setAccount(account);
            transaction.setDate(date);
            transaction.setPurchase(false);
            this.save(transaction);

            double balanceAfterSale = account.getAccountBalance() + totalAmount;
            long volumeAfterSale = stocks.getVolume() + volume;
            account.setAccountBalance(balanceAfterSale);
            stocks.setVolume(volumeAfterSale);

            account.getStocksList().remove(stocks);
            accountService.save(account);
            StockListEntity stockListEntity = new StockListEntity();
            stockListEntity.setAccountId(accountId);
            stockListEntity.setStockId(stockId);
            stockListEntityDao.save(stockListEntity);

        return transaction;

    }

    public List<Transaction> listOfAccountTransactions(long accountId){
        return transDao.findByAccount_AccountId(accountId);
    }

    public List<Transaction> listOfHeldStocks(long stockId, long accountId){
        return transDao.findByStocks_StockIdAndAccount_AccountId(stockId, accountId);
    }

}
