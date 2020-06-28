package com.fdm.trading.controller;

import com.fdm.trading.domain.*;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;

@Controller
public class StocksController {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AccountServiceImpl accountService;

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public String getStocks(Model model, HttpSession session){
        List<Stocks> stocksList = this.stockService.findAll();
        Account account = (Account) session.getAttribute("account");
        System.out.println(stocksList);
        System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
        System.out.println(";;;;;;;;"+account+";;;;;;");
        System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
        model.addAttribute("account", account);
        model.addAttribute("stocks", stocksList);
        model.addAttribute("bySharePrice", Comparator.comparing(Stocks::getSharePrice));
        model.addAttribute("byCompany", Comparator.comparing(Stocks::getCompany));

        return "stocks";
    }

    @RequestMapping(value = "/stocks/purchase/{id}", method = RequestMethod.GET)
    public String getPurchase(@PathVariable int id, Model model, HttpSession session){
        Stocks stocks = stockService.findByStockId(id);
        Account account = (Account) session.getAttribute("account");
        System.out.println(stocks.getCompany());
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", session.getAttribute("account"));
        StockListEntity stockListEntity = stockService.getStockListByAccountAndStock(account.getAccountId(), stocks.getStockId());

        if(stockListEntity==null){
            stockListEntity = new StockListEntity();
        }

        model.addAttribute("stockListEntity", stockListEntity);
        return "purchase";
    }

    @RequestMapping(value = "/stocks/purchase/{id}", method = RequestMethod.POST)
    public String postPurchase(@PathVariable int id, Model model, @ModelAttribute Transaction transaction, HttpSession session){

        Account sessionAccount = (Account) session.getAttribute("account");
        Account account = accountService.findByAccountId(sessionAccount.getAccountId());
        transactionService.createPurchaseTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("stocks", stocks);
        model.addAttribute("transaction", transaction);
        model.addAttribute("account", session.getAttribute("account"));
        return "purchaseSuccess";
    }

    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.GET)
    public String getSale(@PathVariable int id, Model model, HttpSession session){
        Stocks stocks = stockService.findByStockId(id);
        System.out.println(stocks.getCompany());
        Account account = (Account) session.getAttribute("account");
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        return "sale";
    }

    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.POST)
    public String postSale(@PathVariable int id, Model model, @ModelAttribute Transaction transaction, HttpSession session){
        Account account = (Account) session.getAttribute("account");
        transactionService.createSaleTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("messageEnabled", true);
        session.setAttribute("message", transactionService.getMessage());
        model.addAttribute("message", transactionService.getMessage());
        System.out.println(">>>>>>" + transactionService.getMessage());
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        model.addAttribute("transaction", transaction);
        return "sale";
    }


}
