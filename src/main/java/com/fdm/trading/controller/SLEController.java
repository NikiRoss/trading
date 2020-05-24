package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SLEController {

    @Autowired
    StockServiceImpl stockService;

    @RequestMapping(value = "/holdings", method = RequestMethod.GET)
    public String getAccountHoldings(Model model, HttpSession session){
        Account account = (Account) session.getAttribute("account");
        List<Stocks> stocksHeld = new ArrayList<Stocks>();

        List<StockListEntity> stockList = this.stockService.getStockList(account.getAccountId());
        for(StockListEntity sle:stockList){
            Stocks stocks = stockService.findByStockId(sle.getStockId());
            System.out.println("1."+stocks);
            System.out.println("2."+sle);
            stocks.setVolume(sle.getVolume());
            stocksHeld.add(stocks);
        }
        model.addAttribute("stocksHeld", stocksHeld);

        return "holdings";
    }
}
