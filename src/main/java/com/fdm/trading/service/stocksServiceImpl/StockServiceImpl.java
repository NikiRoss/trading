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

    private TimerTask timerTask;

    @Autowired
    private StocksDao stocksDao;

    @Autowired
    private StockListEntityDao sleDao;

    private LocalDateTime openingTime;

    private LocalDateTime closingTime;

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

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public void setOpeningTime(LocalDateTime openingTime){
        this.openingTime = openingTime;
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

    @Scheduled(fixedRate = 100000)
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
            save(stocks);
            System.out.println(stocks.getSharePrice());
        }
        System.out.println("Running scheduled task");
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


    public double stocksProfitAndLoss(long stockId){
        Stocks s = stocksDao.findByStockId(stockId);
        double pl = 0;

        if(s.getClosingValue() > s.getSharePrice()){
            pl = s.getClosingValue() - s.getSharePrice();
            System.out.println(s.getCompany() + " is up by £" + pl + " for the day");
        }else if(s.getClosingValue() < s.getSharePrice()){
            pl = s.getSharePrice() - s.getClosingValue();
            System.out.println(s.getCompany() + " is down by £" + pl + " for the day");
        }
        return pl;
    }

    @Scheduled(cron = "0 52 21 * * *")
    private void setOpeningValues() {
        System.out.println(">>> Setting the opening stock prices!!!");
        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks){
            double opening = stock.getClosingValue();
            stock.setOpeningValue(round(opening));
            this.save(stock);
        }
    }

    @Scheduled(cron = "0 53 21 * * *")
    public void setClosingValues() {
        System.out.println(">>> Setting the closing stock prices!!!");
        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks) {
            double closing = stock.getSharePrice();
            stock.setClosingValue(round(closing));
            this.save(stock);
        }
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

