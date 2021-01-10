package com.fdm.trading.controller;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.events.RegistrationListener;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequestMapping("/admin")
@Controller
@Secured("ADMIN")
public class AdminController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private RegistrationListener listner;


    @GetMapping(value = "/disable/{userId}")
    public String disableUser(Model model, @PathVariable String userId, Principal p){
        System.out.println("toggling user");
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userlist", userList);
        User admin = userService.findByUsername(p.getName());
        User newAdmin = new User();
        model.addAttribute("admin", admin);
        model.addAttribute("newAdmin", newAdmin);
        Stocks stocks = new Stocks();
        model.addAttribute("stocks", stocks);
        User user = userService.findByUserId(Long.valueOf(userId));
        if(user.isEnabled()){
            System.out.println("user is enabled");
            userService.disableUser(user.getUsername());
        } else {
            System.out.println("user is NOT enabled");
            userService.enableUser(user.getUsername());
        }
        return "admin";
    }


    @PostMapping(value = "/newadmin")
    public String addUser(@ModelAttribute("newAdmin") User user, BindingResult result, Model model, Principal p){
        System.out.println("Adding ADMIN...");
        User admin = userService.findByUsername(p.getName());
        Stocks stocks = new Stocks();
        model.addAttribute("admin", admin);
        model.addAttribute("user", user);
        model.addAttribute("stocks", stocks);
        try {
            userService.createNewUser(result, user, "ROLE_ADMIN");
        }catch (NameFormatException | UnsecurePasswordException | UserAlreadyExistException nfe){
            listner.sendExceptionEmail(nfe, LocalDateTime.now());
            return "admin";
        }
        return "admin";
    }

    @PostMapping(value = "/addstock")
    public String addStock(@ModelAttribute("stocks") Stocks stocks, Model model, Principal p){
        User admin = userService.findByUsername(p.getName());
        model.addAttribute("company", stocks.getCompany());
        model.addAttribute("ticker", stocks.getTicker());
        model.addAttribute("sharePrice", stocks.getSharePrice());
        model.addAttribute("volume", stocks.getVolume());
        User newAdmin = new User();
        model.addAttribute("admin", admin);
        model.addAttribute("newAdmin", newAdmin);
        stockService.createNewStock2(stocks);
        return "admin";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model, Principal p){
        User user = userService.findByUsername(p.getName());
        User newAdmin = new User();
        Stocks stocks = new Stocks();
        model.addAttribute("newAdmin", newAdmin);
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userlist", userList);
        model.addAttribute("admin", user);
        model.addAttribute("stocks", stocks);
        model.addAttribute("byUsername", Comparator.comparing(User::getUsername));
        model.addAttribute("byFirstName", Comparator.comparing(User::getFirstName));
        model.addAttribute("bySurname", Comparator.comparing(User::getSurname));

        return "admin";
    }

}
