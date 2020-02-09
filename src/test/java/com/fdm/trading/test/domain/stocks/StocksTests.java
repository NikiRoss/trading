package com.fdm.trading.test.domain.stocks;


import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StocksTests {

    @Autowired
    private StocksDao stocksDao;
    @Autowired
    private StockServiceImpl stockService;

    @Test
    public void create_A_New_Stock(){
        Stocks s = new Stocks();
        s.setCompany("Netflix");
        s.setTicker("NFLX");
        s.setSharePrice(455.09);
        s.setClosingValue(476.32);
        s.setOpeningValue(454.03);
        s.setLastTrade(130554);
        s.setVolume(89970404);
        stockService.save(s);
    }

    @Test
    public void find_A_Stock_By_Id(){
        Stocks s = stockService.findByStockId(2);
        String val1 = s.getCompany();
        assertEquals(val1, "Netflix");
    }

    @Test
    public void find_A_Stock_By_Company(){
        Stocks s = stockService.findByCompany("Netflix");
        assertEquals(s.getStockId(), 2);
    }
}
