package com.fdm.trading.dao;

import com.fdm.trading.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, Long> {

    Account findAccountByAccountId(long accountId);
    Account findAccountByAccountNumber(String accountNumber);
}
