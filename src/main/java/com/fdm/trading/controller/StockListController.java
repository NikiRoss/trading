package com.fdm.trading.controller;

import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class StockListController {

    private final StocksDao stocksDao;

    @Autowired
    StockServiceImpl stockService;


    public StockListController(StocksDao stocksDao) {
        this.stocksDao = stocksDao;
    }


    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public String getStocks(Model model){
        List<Stocks> stocksList = this.stockService.findAll();
        System.out.println(stocksList);
        model.addAttribute("stocks", stocksList);
        return "stocks";
    }


}
