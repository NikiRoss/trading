package com.fdm.trading.dao;

import com.fdm.trading.domain.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StocksDao extends CrudRepository<Stocks, Long> {

    Stocks findByStockId(long stockId);
    Stocks findByCompany(String company);
    Stocks findByTicker(String ticker);
}
