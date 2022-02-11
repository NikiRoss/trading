package com.fdm.trading.service.transactionService;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.exceptions.InsufficientFundsException;
import com.fdm.trading.exceptions.InsufficientHoldingsForSaleException;
import com.fdm.trading.exceptions.LimitedStockException;

public interface TransactionService {

    Transaction findByTransactionId(long transactionId);
    Transaction createPurchaseTransaction(int stockId, int accountId, long volume) throws Exception;
    public Transaction createSaleTransaction(int stockId, int accountId, long volume) throws Exception;

}
