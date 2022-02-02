package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    CardServiceImpl cardService;


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
    public ModelAndView success(Model model, Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Account account = user.getAccount();
        CreditCard card = new CreditCard();
        card.setNameOnCard(user.getUsername() + user.getSurname());
        Map<String, Object> modelMap = new HashMap<>();

        List<CreditCard> creditCards = cardService.getListOfCardsForUser(user);

        Set<String> userAuths = userService.getUserAuthorities(user);
        if(userAuths.contains("ROLE_ADMIN")) {
            modelMap.put("newAdmin", new User());
            modelMap.put("stocks", new Stocks());
            modelMap.put("userList", userService.findAllUsers());
            return new ModelAndView("redirect:/admin/users", modelMap);
        }
        User dbUser = userService.findByUsername(principal.getName());
        modelMap.put("cardList", creditCards);
        modelMap.put("dbUser", dbUser);
        modelMap.put("user", user);
        modelMap.put("account", account);
        modelMap.put("card", card);
        return new ModelAndView("account", modelMap);
    }


    private List<Stocks> stocksTicker(){
        return stockService.findAll();
    }

}
