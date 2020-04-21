package com.fdm.trading.controller;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StocksController {

    @Autowired
    StockServiceImpl stockService;

    @RequestMapping("/stocks")
    public String stocks(Model model){
        Iterable<Stocks> stocksList = stockService.findAll();
        model.addAttribute("stocksList", stocksList);
        return "stocks";
    }


}
