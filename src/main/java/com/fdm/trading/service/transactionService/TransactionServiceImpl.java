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
            stocks.setLastTrade(totalCost);
            account.getStocksList().add(stocks);
            stocks.setAccount(account);
            accountService.save(account);
            stockService.save(stocks);
            List<StockListEntity> dbStockListEntity = stockListEntityDao.findByAccountIdAndStockId(accountId, stockId);

            if(dbStockListEntity.size()<1){
                StockListEntity stockListEntity = new StockListEntity();
                stockListEntity.setAccountId(accountId);
                stockListEntity.setStockId(stockId);
                stockListEntity.setVolume(volume);
                stockListEntityDao.save(stockListEntity);
            } else {
                StockListEntity entity = dbStockListEntity.get(0);
                entity.setVolume(entity.getVolume()+volume);
                stockListEntityDao.save(entity);
            }
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

    public Transaction createSaleTransaction(int stockId, int accountId, long volume){
        Stocks stocks = stockService.findByStockId(stockId);
        Account account = accountService.findByAccountId(accountId);
        Date date = new Date();
        List<StockListEntity> stockListEntities = stockListEntityDao.findByAccountIdAndStockId(accountId,stockId);
        for(StockListEntity stockListEntity:stockListEntities) {
            if(volume == stockListEntity.getVolume()){
                stockListEntityDao.delete(stockListEntity);
            }else if(volume < stockListEntity.getVolume()){
                stockListEntity.setVolume(stockListEntity.getVolume()-volume) ;
                stockListEntityDao.save(stockListEntity);
                break;
            }else {
                if (volume > stockListEntity.getVolume()){
                    System.out.println("You do not hold " + volume + " units of this stock, please revise your sale");
                }
            }
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

        return transaction;

    }

    public List<Transaction> findAllTransactions(long accountId){
       return transDao.findByAccount_AccountId(accountId);
    }
}
