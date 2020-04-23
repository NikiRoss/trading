package com.fdm.trading.service.stocksServiceImpl;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StocksService{

    @Autowired
    private StocksDao stocksDao;

    @Autowired
    private StockListEntityDao stockListEntityDao;

    @Override
    public Stocks findByStockId(long stockId) {
        return stocksDao.findByStockId(stockId);
    }

    @Override
    public Stocks findByCompany(String company) {
        return stocksDao.findByCompany(company);
    }

    @Override
    public Stocks findByTicker(String ticker) {
        return stocksDao.findByTicker(ticker);
    }

    @Override
    public void createNewStock(String company, String ticker, double sharePrice, long volume) {
        Stocks stocks = new Stocks();
        stocks.setCompany(company);
        stocks.setTicker(ticker);
        stocks.setSharePrice(sharePrice);
        stocks.setVolume(volume);
        stocksDao.save(stocks);
    }

    @Override
    public void removeStock(long stockId) {
        Stocks stock = stocksDao.findByStockId(stockId);
        stocksDao.delete(stock);
    }

    @Override
    public void fluctuateStockPrice() {
        
    }

    public List<Stocks> findAll(){
        Iterable<Stocks> stocksIterable = stocksDao.findAll();
        List<Stocks> stocks = new ArrayList<Stocks>();
        for(Stocks stock:stocksIterable){
            stocks.add(stock);
        }
        return stocks;
    }

    public void save(Stocks stocks){
        stocksDao.save(stocks);
    }
    public List<Stocks> returnStockList(long accountId){
        List<StockListEntity> stockListEntities = stockListEntityDao.findByAccountId(accountId);
        List<Stocks> stocksForAccount = new ArrayList<>();
        for (StockListEntity sle:stockListEntities){
            stocksForAccount.add(stocksDao.findByStockId(sle.getStockId()));
        }
        return stocksForAccount;

    }
}

