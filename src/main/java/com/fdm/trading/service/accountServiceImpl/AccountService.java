package com.fdm.trading.service.accountServiceImpl;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;

import java.util.List;

public interface AccountService {

    Account findByAccountId(long accountId);

    Account findByAccountNumber(String accountNumber);

    String accountNumberGenerator();

    Account createAnAccount();

    void deleteAccountById(long accountId);

    void deleteAccountByAccountNumber(long accountNumber);

    void save(Account a);

    public List<Stocks> retrieveStockFromList(long accountId);

}
