package com.fdm.trading.controller;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RequestMapping("/admin")
@Controller
@Secured("ADMIN")
public class AdminController {

    @Autowired
    private UserServiceImpl userService;


    @RequestMapping(value = "disable/{userId}", method = RequestMethod.POST)
    public String disableUser(Model model, @PathVariable String userId, Principal p){
        User user = userService.findByUserId(Long.valueOf(userId));
        if(user.isEnabled()){
            userService.disableUser(user.getUsername());
        }
        return "admin";


    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String disableUser2(Model model, Principal p){
        User user = userService.findByUsername(p.getName());

        List<User> userList = userService.findAllUsers();
        model.addAttribute("userlist", userList);
        model.addAttribute("admin", user);
        model.addAttribute("byUsername", Comparator.comparing(User::getUsername));
        model.addAttribute("byFirstName", Comparator.comparing(User::getFirstName));
        model.addAttribute("bySurname", Comparator.comparing(User::getSurname));

        return "admin";
    }

}
