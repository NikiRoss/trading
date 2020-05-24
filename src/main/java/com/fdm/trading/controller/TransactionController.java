package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public String getTransactions(Model model, HttpSession session){
        Account account = (Account) session.getAttribute("account");
        List<Transaction> transList = this.transactionService.findAllTransactions(account.getAccountId());
        model.addAttribute("transList", transList);

        return "transactions";
    }
}
