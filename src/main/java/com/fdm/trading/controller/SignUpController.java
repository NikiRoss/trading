package com.fdm.trading.controller;

import com.fdm.trading.domain.User;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class SignUpController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/signup")
    public String getSignUp(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping(value = "/signup/adduser")
    public String addUser(Model model, @ModelAttribute("user") User user){
        System.out.println("Adding user...");
        model.addAttribute("user", user);
        userService.createNewUserAlt(user, "ROLE_USER");
        System.out.println(user.toString());
        return "login";

    }
    @RequestMapping(value = "/foo", method = RequestMethod.POST)
    public String foo(){
        System.out.println("foo");
        return "login";
    }



}