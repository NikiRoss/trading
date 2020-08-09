package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.accountServiceImpl.AccountServiceImpl;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    StockServiceImpl stockService;


    @RequestMapping("/")
    public String root(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "index";
    }
    @RequestMapping("/test")
    @Secured("ADMIN")
    public String test(){
        return "test";
    }

    @RequestMapping("/index")
    @Secured("USER_HOME")
    public String index(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println(user.toString());

        System.out.println("GOT INDEX");
        Account sessionAccount = user.getAccount();
        Account account = accountService.findByAccountId(sessionAccount.getAccountId());
        model.addAttribute("newUser", user);
        model.addAttribute("stocks", stocksTicker());
        model.addAttribute("account", account);
        model.addAttribute("principal", principal);
        return "userHome";
    }

 /*   @RequestMapping("/userHome")
    public String userHome(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        Account account = user.getAccount();
        model.addAttribute("account", account);

        return "userHome";
    }*/

    @RequestMapping(value = "/userHome", method = RequestMethod.POST)
    public String loginPost(@ModelAttribute Principal principal, Model model, HttpSession session) {
        User newUser = userService.findByUsername(principal.getName());
        List<User> users = userService.findUserList();

        model.addAttribute("newUser", newUser);

        session.setAttribute("newUser", newUser);
        model.addAttribute("stocks", stocksTicker());
        session.setAttribute("account", newUser.getAccount());
        model.addAttribute("account", newUser.getAccount());
        System.out.println("newUser------>" + newUser);

        return "userHome";
    }

    @RequestMapping(value = "/userHome", method = RequestMethod.GET)
    public String userHome(Model model, HttpSession session) {
        System.out.println("in userHome - method: GET");
        Account sessionAccount = (Account) session.getAttribute("account");
        Account account = accountService.findByAccountId(sessionAccount.getAccountId());
        model.addAttribute("newUser", session.getAttribute("newUser"));
        model.addAttribute("stocks", stocksTicker());
        model.addAttribute("account", account);
        return "userHome";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorMessage(Model model){
        model.addAttribute("loginError", true);
        return "index";
    }

    private List<Stocks> stocksTicker(){
        return stockService.findAll();
    }

}
