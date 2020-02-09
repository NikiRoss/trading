package com.fdm.trading.dao;

import com.fdm.trading.domain.Transaction;
import org.springframework.data.repository.CrudRepository;


public interface TransactionDao extends CrudRepository<Transaction, Long> {

    Transaction findByTransactionId(long transactionId);

}
