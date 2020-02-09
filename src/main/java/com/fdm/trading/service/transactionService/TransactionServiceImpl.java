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
    public Transaction createPurchaseTransaction(Stocks s, Account a, long volume) {
        Transaction t = new Transaction();
        double balance = a.getAccountBalance();
        double totalCost = volume * s.getSharePrice();
        if (totalCost > balance){
            System.out.println("No Funds");
        } else{
            t.setPrice(totalCost);
            t.setVolume(volume);
            double balanceAfterDeduction = balance - totalCost;
            long volumeAfterDeduction = s.getVolume() - volume;
            a.setAccountBalance(balanceAfterDeduction);
            s.setVolume(volumeAfterDeduction);
            //a.getStocksList().add(s);
            //a.getTransactionList().add(t);
            accountService.save(a);
            transDao.save(t);
            stockService.save(s);
            accountService.save(a);

        }
        return t;
    }


}
