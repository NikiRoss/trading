package com.fdm.trading.dao;

import com.fdm.trading.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TransactionDao extends CrudRepository<Transaction, Long> {

    Transaction findByTransactionId(long transactionId);

    List<Transaction> findByAccount_AccountId(long accountId);

    List<Transaction> findByStocks_StockIdAndAccount_AccountId(long stockId, long accountId);



}
