package com.fdm.trading.controller;

import com.fdm.trading.domain.*;
import com.fdm.trading.exceptions.InsufficientFundsException;
import com.fdm.trading.exceptions.LimitedStockException;
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
import java.security.Principal;
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

    private Account getAccountFromPrincipal(Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        return user.getAccount();
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public String getStocks(Model model, Principal principal){
        List<Transaction> latestTransactions = transactionService.getLatestTransaction();
        List<Stocks> stocksList = this.stockService.findAll();
        Account account = getAccountFromPrincipal(principal);
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
    public String getPurchase(@PathVariable int id, Model model, Principal principal){
        Account account = getAccountFromPrincipal(principal);
        Stocks stocks = stockService.findByStockId(id);
        System.out.println(stocks.getCompany());
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        StockListEntity stockListEntity = stockService.getStockListByAccountAndStock(account.getAccountId(), stocks.getStockId());

        if(stockListEntity == null){
            stockListEntity = new StockListEntity();
        }

        model.addAttribute("stockListEntity", stockListEntity);
        return "purchase";
    }

    @RequestMapping(value = "/stocks/purchase/{id}", method = RequestMethod.POST)
    public String postPurchase(@PathVariable int id, Model model, @ModelAttribute Transaction transaction, Principal principal) throws LimitedStockException, InsufficientFundsException {
        Account account = getAccountFromPrincipal(principal);
        transactionService.createPurchaseTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("stocks", stocks);
        model.addAttribute("transaction", transaction);
        model.addAttribute("account", account);
        return "purchaseSuccess";
    }



    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.GET)
    public String getSale(@PathVariable int id, Model model, Principal principal){
        Account account = getAccountFromPrincipal(principal);
        Stocks stocks = stockService.findByStockId(id);
        System.out.println(stocks.getCompany());
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        return "sale";
    }

    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.POST)
    public String postSale(@PathVariable int id, Model model, @ModelAttribute Transaction transaction, Principal principal){
        Account account = getAccountFromPrincipal(principal);
        transactionService.createSaleTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("messageEnabled", true);
        model.addAttribute("message", transactionService.getMessage());
        System.out.println(">>>>>>" + transactionService.getMessage());
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        model.addAttribute("transaction", transaction);
        return "sale";
    }



}
