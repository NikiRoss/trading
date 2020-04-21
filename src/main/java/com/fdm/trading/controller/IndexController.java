package com.fdm.trading.controller;

import com.fdm.trading.domain.User;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showLogin(Model model){
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String userHome(Model model, @ModelAttribute User user){
        User newUser = userService.findByUsername(user.getUsername());
        model.addAttribute("newUser", newUser);
        model.addAttribute("account", newUser.getAccount());
        System.out.println(newUser.toString());

        if (userService.validateUser(newUser, user.getPassword())){
            return "userHome";
        }
        return "error";
    }
}
