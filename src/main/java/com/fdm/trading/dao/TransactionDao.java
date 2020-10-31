package com.fdm.trading.dao;

import com.fdm.trading.domain.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TransactionDao extends CrudRepository<Transaction, Long> {

    Transaction findByTransactionId(long transactionId);

    List<Transaction> findByAccount_AccountId(long accountId);

    List<Transaction> findByStocks_StockIdAndAccount_AccountId(long stockId, long accountId);

    List<Transaction> findAll(Sort date);

    // @Query("SELECT t FROM Transaction t WHERE t.fk_stockid = ? ORDER BY date desc limit 1;")
    //Transaction findByStocks_StockId(long stockId);


}
