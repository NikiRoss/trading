package com.fdm.trading.service.stocksServiceImpl;

import com.fdm.trading.domain.Stocks;

public interface StocksService {

    Stocks findByStockId(long stockId);

    Stocks findByCompany(String company);

    Stocks findByTicker(String ticker);

    void createNewStock(String company, String ticker, double sharePrice, long volume);

    void removeStock(long stockId);

    void fluctuateStockPrice();


}
