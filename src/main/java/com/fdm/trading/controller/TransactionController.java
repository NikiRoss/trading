package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CardValidationData;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.transactionService.TransactionService;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public String getTransactions(Model model, Principal principal){
        User user = (User) userService.loadUserByUsername(principal.getName());
        Account account = user.getAccount();
        List<Transaction> transList = this.transactionService.findAllTransactions(account.getAccountId());
        if (transList == null){
            transList = new ArrayList<Transaction>();
        }
        model.addAttribute("transList", transList);

        return "transactions";
    }

    @GetMapping(value = "/stocks/purchase/confirm.html")
    public String getConfirmTransaction(Model model) {
        CardValidationData cardValidationData = new CardValidationData();
        model.addAttribute("cardValidationData", cardValidationData);
        return "confirm.html";
    }

}

