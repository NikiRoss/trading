package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SLEController {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/holdings", method = RequestMethod.GET)
    public String getAccountHoldings(Model model, Principal principal){
        User user = (User) userService.loadUserByUsername(principal.getName());
        Account account = user.getAccount();
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
