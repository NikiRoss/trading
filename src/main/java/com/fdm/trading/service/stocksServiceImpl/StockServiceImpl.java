package com.fdm.trading.service.stocksServiceImpl;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableScheduling
@Service
public class StockServiceImpl implements StocksService, Runnable{


    @Autowired
    private StocksDao stocksDao;

    @Autowired
    private StockListEntityDao sleDao;


    public void save(Stocks stocks){
        stocksDao.save(stocks);
    }

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

    public Stocks createNewStock2(Stocks stocks) {
        return stocksDao.save(stocks);
    }

    @Override
    public void removeStock(long stockId) {
        Stocks stock = stocksDao.findByStockId(stockId);
        stocksDao.delete(stock);
    }

    @Scheduled(fixedRate = 30000)
    @Override
    public void fluctuateStockPrice() {
        Iterable<Stocks> list = stocksDao.findAll();
        Random random1 = new Random();

        for(Stocks stocks:list){
            double sharePrice = stocks.getSharePrice();

            double num = random1.nextDouble()*5;
            if(sharePrice < 5){
                sharePrice += num;
            }
            if (random1.nextBoolean()) {
                sharePrice += num;

            }else{
                sharePrice -= num;
            }
            if(sharePrice < 0){
                sharePrice = -sharePrice;
            }
            stocks.setSharePrice(round(sharePrice));
            profitAndLoss(stocks);
            save(stocks);
        }
        System.out.println("Running scheduled task");
    }

    public Stocks profitAndLoss(Stocks stock){
        double opening = stock.getOpeningValue();
        double closing = stock.getClosingValue();
        String minus = "-";
        String plus = "+";
        double gains;

        if(closing > opening){
            gains = closing - opening;
            String y = String.valueOf(round(gains));
            String z = plus.concat(y);
            stock.setPnl(z);

        }else {
            gains = opening - closing;
            round(gains);
            String b = String.valueOf(round(gains));
            String x = minus.concat(b);
            stock.setPnl(x);
        }
        return stock;
    }

    /**
     * Revist idea of providing information RE db connection time
     * for a list to be returned with requested info by providing logging
     */


    public List<Stocks> findAll(){
        Iterable<Stocks> stocksIterable = stocksDao.findAll();
        List<Stocks> stocks = new ArrayList<Stocks>();
        for(Stocks stock:stocksIterable){
            stocks.add(stock);
        }
        return stocks;
    }


    public List<Stocks> returnStockList(long accountId){
        List<StockListEntity> stockListEntities = sleDao.findByAccountId(accountId);
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

    @Scheduled(cron = "0 47 20 * * *")
    private void setOpeningValues() {
        System.out.println(">>> Setting the opening stock prices!!!");
        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks){
            double opening = stock.getClosingValue();
            stock.setOpeningValue(round(opening));
            this.save(stock);
        }
    }

    @Scheduled(cron = "0 55 20 * * *")
    public void setClosingValues() {
        System.out.println(">>> Setting the closing stock prices!!!");
        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks) {
            double closing = stock.getSharePrice();
            stock.setClosingValue(round(closing));
            this.save(stock);
        }
    }
    public void setLastTrade(Stocks stocks, Date lastTrade){
        stocks.setLastTrade(lastTrade);
        stocksDao.save(stocks);
    }



    @Override
    public void run() {
        System.out.println("running task for open and close");
        setOpeningValues();
        setClosingValues();
    }

    public double round(double price){
       double result = Math.round(price * 100.0) / 100.0;
       return result;
    }

}

