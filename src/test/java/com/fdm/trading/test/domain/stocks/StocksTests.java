package com.fdm.trading.test.domain.stocks;


import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
        stockService.createNewStock("Disney", "DSNY", 300.21, 800000);
        stockService.createNewStock("Tesla", "TSLA", 609.76, 800000);
        stockService.createNewStock("Ford", "FRD", 59.83, 800000);
    }

    @Test
    public void find_A_Stock_By_Id(){
        Stocks s = stockService.findByStockId(6);
        String val1 = s.getCompany();
        assertEquals(val1, "Disney");
    }

    @Test
    public void find_A_Stock_By_Company(){
        Stocks s = stockService.findByCompany("Tesla");
        assertEquals(s.getStockId(), 7);
    }

    @Test
    public void find_All_Stocks(){
        List<Stocks> stocksList = stockService.findAll();
        int size = stocksList.size();
        assertEquals(size, 3);
    }
}
