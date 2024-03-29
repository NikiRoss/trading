package com.fdm.trading.controller;

import com.fdm.trading.domain.*;
import com.fdm.trading.events.RegistrationListener;
import com.fdm.trading.exceptions.InsufficientFundsException;
import com.fdm.trading.exceptions.InsufficientHoldingsForSaleException;
import com.fdm.trading.exceptions.LimitedStockException;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class StocksController {

    private static final Logger logger = LoggerFactory.getLogger(StocksController.class);

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    private RegistrationListener listner;

    private Account getAccountFromPrincipal(Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        return user.getAccount();
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public String getStocks(Model model, Principal principal){
        List<Stocks> stocksList = this.stockService.findAll();
        Account account = getAccountFromPrincipal(principal);
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
        try {
            transactionService.createPurchaseTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        } catch (Exception e) {

            listner.sendExceptionEmail(e, LocalDateTime.now());
            logger.warn(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("stocks", stocks);
        model.addAttribute("transaction", transaction);
        model.addAttribute("account", account);
        model.addAttribute("var", "purchased");
        CardValidationData cardValidationData = new CardValidationData();
        model.addAttribute("cardValidationData", cardValidationData);
        return "confirm.html";
    }



    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.GET)
    public String getSale(@PathVariable int id, Model model, Principal principal){
        Account account = getAccountFromPrincipal(principal);
        Stocks stocks = stockService.findByStockId(id);
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        return "sale";
    }

    @RequestMapping(value = "/stocks/sale/{id}", method = RequestMethod.POST)
    public String postSale(@PathVariable int id, Model model, @ModelAttribute Transaction transaction, Principal principal){
        Account account = getAccountFromPrincipal(principal);
        try {
            transactionService.createSaleTransaction(id, (int) account.getAccountId(), transaction.getVolume());
        } catch (Exception e) {

            listner.sendExceptionEmail(e, LocalDateTime.now());
            logger.warn(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        Stocks stocks = stockService.findByStockId(id);
        model.addAttribute("messageEnabled", true);
        model.addAttribute("message", transactionService.getMessage());
        model.addAttribute("stocks", stocks);
        model.addAttribute("account", account);
        model.addAttribute("transaction", transaction);
        model.addAttribute("var", "sold");
        return "purchaseSuccess";
    }

    @PostMapping(value = "/stocks/purchase/validate")
    public String validateTransaction(@ModelAttribute Transaction trans1, Model model, @ModelAttribute Stocks stocks, @ModelAttribute("cardValidationData") CardValidationData cardValidationData, Principal principal) {
        Account account = getAccountFromPrincipal(principal);
        List<Transaction> transactions = transactionService.listOfAccountPurchases(account.getAccountId());

        Collections.sort(transactions, new Comparator<Transaction>() {
            public int compare(Transaction m1, Transaction m2) {
                return m2.getDate().compareTo(m1.getDate());
            }
        });
        Transaction transaction = transactions.get(0);
        model.addAttribute("stocks", transaction.getStocks());
        model.addAttribute("transaction", transaction);
        model.addAttribute("account", account);
        model.addAttribute("var", "purchased");
        model.addAttribute("cardValidationData", cardValidationData);

        try {
            transactionService.validateCardForTransaction(cardValidationData.getLastFourDigitsOfCard(), principal.getName());
        } catch (Exception e) {

            listner.sendExceptionEmail(e, LocalDateTime.now());
            logger.warn(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        return "purchaseSuccess";
    }



}
