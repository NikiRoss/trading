package com.fdm.trading.service.accountServiceImpl;

import com.fdm.trading.dao.AccountDao;
import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.TransactionDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.transactionService.TransactionService;
import com.fdm.trading.service.userServiceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;


    @Override
    public Account findByAccountId(long accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountDao.findAccountByAccountNumber(accountNumber);
    }

    @Override
    public String accountNumberGenerator() {
        Random ran = new Random();
        int val1 = ran.nextInt(999999);
        return String.format("%06d", val1);
    }

    @Override
    public Account createAnAccount() {
        Account a = new Account();
        a.setAccountNumber(accountNumberGenerator());
        accountDao.save(a);
        return a;
    }

    @Override
    public void deleteAccountById(long accountId) {
        Account a = accountDao.findAccountByAccountId(accountId);
        accountDao.delete(a);
    }

    @Override
    public void deleteAccountByAccountNumber(long accountNumber) {
        Account a = accountDao.findAccountByAccountId(accountNumber);
        accountDao.delete(a);
    }

    @Override
    public void save(Account a) {
        accountDao.save(a);
    }

    public List<Stocks> retrieveStockFromList(long accountId){
        Account a = accountDao.findAccountByAccountId(accountId);
        return a.getStocksList();
    }




}
