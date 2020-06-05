package com.fdm.trading.service.stocksServiceImpl;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@EnableScheduling
@Service
public class StockServiceImpl implements StocksService{

    private TimerTask timerTask;

    @Autowired
    private StocksDao stocksDao;

    @Autowired
    private StockListEntityDao sleDao;

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

    @Scheduled(fixedRate = 60000)
    @Override
    public void fluctuateStockPrice() {
        Iterable<Stocks> list = stocksDao.findAll();
        Random random1 = new Random();

        for(Stocks stocks:list){
            double sharePrice = stocks.getSharePrice();
            double num = random1.nextDouble()*5;
            if (random1.nextBoolean()) {
                sharePrice += num;

            }else{
                sharePrice -= num;
            }
            stocks.setSharePrice(sharePrice);
            save(stocks);
        }

        System.out.println("Running scheduled task");

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

    public List<StockListEntity> getStockList(long accountId){
        return sleDao.findByAccountId(accountId);
    }

    public StockListEntity getStockListByAccountAndStock(long accountId, long stockId){
        return sleDao.findByAccountIdAndStockId(accountId, stockId);
    }

}

