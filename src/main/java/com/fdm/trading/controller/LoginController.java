package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    StockServiceImpl stockService;


    /*@GetMapping("/index")
    public String home() {
        return "index";
    }*/

    @GetMapping("/")
    public String root() {
        return "redirect:/account";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }


    @GetMapping("/account")
    public String success(Model model, Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Account account = user.getAccount();
        CreditCard card = new CreditCard();
        card.setNameOnCard(user.getUsername() + user.getSurname());
        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("card", card);
        return "account";
    }

    @GetMapping("/newtemp")
    public String newTemp(Model model, @ModelAttribute User user, Principal principal) {
        user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println(user.toString());
        return "newtemp";
    }


    private List<Stocks> stocksTicker(){
        return stockService.findAll();
    }

}
