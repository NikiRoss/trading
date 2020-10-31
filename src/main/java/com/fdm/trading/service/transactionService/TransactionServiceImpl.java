package com.fdm.trading.service.transactionService;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.TransactionDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private String message;

    @Override
    public Transaction findByTransactionId(long transactionId) {
        return transDao.findByTransactionId(transactionId);
    }

    private void save(Transaction transaction) {
        transDao.save(transaction);
    }

    public List<Transaction> listOfAccountTransactions(long accountId){
        return transDao.findByAccount_AccountId(accountId);
    }

    public List<Transaction> listOfHeldStocks(long stockId, long accountId){
        return transDao.findByStocks_StockIdAndAccount_AccountId(stockId, accountId);
    }

    public List<Transaction> listOfAccountPurchases(long accountId){
        List<Transaction> purchases = transDao.findByAccount_AccountId(accountId);
        List<Transaction> purchaseFilter = new ArrayList<>();
        for (Transaction trans:purchases){
            if (trans.isPurchase()){
                purchaseFilter.add(trans);
                System.out.println(purchaseFilter.toString());
            }
        }
        return purchaseFilter;
    }

    public List<Transaction> listOfAccountSales(long accountId){
        List<Transaction> sales = transDao.findByAccount_AccountId(accountId);
        List<Transaction> purchaseFilter = new ArrayList<>();
        for (Transaction trans:sales){
            if (!trans.isPurchase()){
                purchaseFilter.add(trans);
                System.out.println(purchaseFilter.toString());
            }
        }
        return purchaseFilter;
    }

    @Override
    @Transactional
    public Transaction createPurchaseTransaction(int stockId, int accountId, long volume) {
        Stocks stocks = stockService.findByStockId(stockId);
        Account account = accountService.findByAccountId(accountId);
        Transaction transaction = new Transaction();
        Date date = new Date();

        double balance = account.getAccountBalance();
        double totalCost = volume * stocks.getSharePrice();
        if (totalCost > balance){
            System.out.println("You do not have enough funds to complete this transaction");
        }else if (volume > stocks.getVolume() && volume > 0){
            {
                System.out.println("There are only: " + stocks.getVolume() + " available, please amend your purchase and try again");
            }
        }
        else{
            double balanceAfterDeduction = balance - totalCost;
            long volumeAfterDeduction = stocks.getVolume() - volume;
            account.setAccountBalance(balanceAfterDeduction);
            stocks.setVolume(volumeAfterDeduction);
            stocks.setLastTrade(date);
            account.getStocksList().add(stocks);
            stocks.setAccount(account);
            accountService.save(account);
            stockService.save(stocks);
            StockListEntity dbStockListEntity = stockListEntityDao.findByAccountIdAndStockId(accountId, stockId);

            if (dbStockListEntity==null){
                dbStockListEntity = new StockListEntity();
                dbStockListEntity.setAccountId(accountId);
                dbStockListEntity.setStockId(stockId);
                dbStockListEntity.setVolume(volume);
            }
            dbStockListEntity.setVolume(dbStockListEntity.getVolume()+volume);
            stockListEntityDao.save(dbStockListEntity);
            transaction.setPrice(totalCost);
            transaction.setVolume(volume);
            transaction.setAccount(account);
            transaction.setDate(date);
            transaction.setPurchase(true);
            transaction.setStocks(stocks);
            transDao.save(transaction);
        }
        return transaction;
    }

    public Transaction createSaleTransaction(int stockId, int accountId, long volume){
        Stocks stocks = stockService.findByStockId(stockId);
        Account account = accountService.findByAccountId(accountId);
        Date date = new Date();
        StockListEntity stockListEntity = stockListEntityDao.findByAccountIdAndStockId(accountId,stockId);
        if (volume <= stockListEntity.getVolume()) {
            if (volume == stockListEntity.getVolume()) {
                stockListEntityDao.delete(stockListEntity);
            } if (volume < stockListEntity.getVolume() && volume > 0) {
                stockListEntity.setVolume(stockListEntity.getVolume() - volume);
                stockListEntityDao.save(stockListEntity);
            } if (volume > stockListEntity.getVolume()) {
                System.out.println("You do not own this amount of stock to sell!!!");
            } else {
                System.out.println("You do not hold " + volume + " units of this stock, please revise your sale");
            }

            Transaction transaction = new Transaction();
            double totalAmount = volume * stocks.getSharePrice();
            transaction.setPrice(totalAmount);
            transaction.setVolume(volume);
            transaction.setAccount(account);
            transaction.setDate(date);
            transaction.setPurchase(false);
            transaction.setStocks(stocks);
            this.save(transaction);

            double balanceAfterSale = account.getAccountBalance() + totalAmount;
            long volumeAfterSale = stocks.getVolume() + volume;
            account.setAccountBalance(balanceAfterSale);
            stocks.setVolume(volumeAfterSale);
            account.getStocksList().remove(stocks);
            accountService.save(account);
            stockService.save(stocks);
            message = "Sale successful!!";
            return transaction;
        } else {
            message = "You do not own this amount of stock to sell!!!";
            return new Transaction();
        }
    }

    public List<Transaction> findAllTransactions(long accountId){
       return transDao.findByAccount_AccountId(accountId);
    }

    public String getMessage(){
        return message;
    }

    public List<Transaction> findAll(){
        List<Transaction> transList = new ArrayList<>();
        for(Transaction transaction:transDao.findAll()){
            transList.add(transaction);
        }
        return transList;
    }

    public List<Transaction> getLatestTransaction(){
        List<Stocks> stocksList = stockService.findAll();
        List<Transaction> latestTransactions = new ArrayList<>();
        List<String> props = new ArrayList<>();
        props.add("date");
        for (Stocks stock:stocksList){
            List<Transaction> transactions = transDao.findAll( Sort.by(Sort.Direction.DESC, "date"));
            Transaction transaction = transactions.get(0);
            System.out.println(transaction.getDate());
            stockService.setLastTrade(stock, transaction.getDate());
            latestTransactions.add(transaction);
        }
        return latestTransactions;
    }




}
