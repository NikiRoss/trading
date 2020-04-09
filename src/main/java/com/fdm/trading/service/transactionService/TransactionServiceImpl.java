package com.fdm.trading.service.transactionService;

import com.fdm.trading.dao.TransactionDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.accountServiceImpl.AccountService;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                System.out.println("There are only: " + stocks.getVolume() + "available, please amend your purchase and try again");
            }
        }

        else{
            transaction.setPrice(totalCost);
            transaction.setVolume(volume);
            transaction.setAccount(account);
            transaction.setDate(date);
            transaction.setPurchase(true);
            account.getTransactionList().add(transaction);
            this.save(transaction);

            double balanceAfterDeduction = balance - totalCost;
            long volumeAfterDeduction = stocks.getVolume() - volume;
            account.setAccountBalance(balanceAfterDeduction);
            stocks.setVolume(volumeAfterDeduction);

            account.getStocksList().add(stocks);
            accountService.save(account);
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
            account.getTransactionList().add(transaction);
            this.save(transaction);

            double balanceAfterSale = account.getAccountBalance() + totalAmount;
            long volumeAfterSale = stocks.getVolume() + volume;
            account.setAccountBalance(balanceAfterSale);
            stocks.setVolume(volumeAfterSale);

            account.getStocksList().remove(stocks);
            accountService.save(account);

        return transaction;

    }


}
