package com.fdm.trading.controller;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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


    @GetMapping(value = "disable/{userId}")
    public String disableUser(Model model, @PathVariable String userId, Principal p){
        System.out.println("toggling user");
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userlist", userList);
        User admin = userService.findByUsername(p.getName());
        model.addAttribute("admin", admin);
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
    public String addUser(@ModelAttribute("newAdmin") User user, Model model, Principal p){
        System.out.println("Adding ADMIN...");
        User admin = userService.findByUsername(p.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("user", user);
        userService.createNewUserAlt(user, "ROLE_ADMIN");
        System.out.println(user.toString());
        return "admin";
    }

    @PostMapping(value = "/addstock")
    public String addStock(Model model, Principal p){
        Stocks stocks = new Stocks();

        System.out.println("Adding Stock...");
        User admin = userService.findByUsername(p.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("stocks", stocks);
        stockService.createNewStock2(stocks);
        System.out.println(stocks.toString());
        return "admin";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String disableUser2(Model model, Principal p){
        User user = userService.findByUsername(p.getName());
        User newAdmin = new User();
        model.addAttribute("newAdmin", newAdmin);
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userlist", userList);
        model.addAttribute("admin", user);
        model.addAttribute("byUsername", Comparator.comparing(User::getUsername));
        model.addAttribute("byFirstName", Comparator.comparing(User::getFirstName));
        model.addAttribute("bySurname", Comparator.comparing(User::getSurname));

        return "admin";
    }

}
