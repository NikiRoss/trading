package com.fdm.trading.service.transactionService;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;

public interface TransactionService {

    Transaction findByTransactionId(long transactionId);
    Transaction createPurchaseTransaction(Stocks s, Account a, long amount);

}
