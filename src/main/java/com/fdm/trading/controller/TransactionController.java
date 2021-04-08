package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
