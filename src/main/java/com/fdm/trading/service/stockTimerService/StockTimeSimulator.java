package com.fdm.trading.service.stockTimerService;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class StockTimeSimulator {}//implements Runnable {

/*
    @Autowired
    StockServiceImpl stockService;

    public void setValues(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = dateFormat.parse("18:25:00");
            endTime = dateFormat.parse("18:27:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timer openTimer = new Timer();
        Timer closeTimer = new Timer();

        openTimer.schedule(setOpeningValues(), startTime);
        closeTimer.schedule(setClosingValues(), endTime);
    }

    private void setOpeningValues() {

        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks){
            stock.setOpeningValue(stock.getSharePrice());
        }
}
    private TimerTask setClosingValues() {
        List<Stocks>  stocks = this.findAll();
        for(Stocks stock: stocks){
            stock.setClosingValue(stock.getSharePrice());
        }
    }

    @Override
    public void run() {
        System.out.println("running task for open and close");
        setOpeningValues();
        setClosingValues();
    }
}*/
