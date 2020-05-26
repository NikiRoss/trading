package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/userHome", method = RequestMethod.POST)
    public String signupPost(@ModelAttribute User user, Model model, HttpSession session) {
        System.out.println(user.getUsername());
        User newUser = userService.findByUsername(user.getUsername());
        List<User> users = userService.findUserList();

        model.addAttribute("newUser", newUser);
        model.addAttribute("validated", userService.validateUser(newUser, user.getPassword()));
        session.setAttribute("newUser", newUser);
        session.setAttribute("account", newUser.getAccount());
        model.addAttribute("account", newUser.getAccount());
        System.out.println("newUser------>" + newUser);
        System.out.println("user>>>>>>>>>>>>>>>>>>>>>>>" + user);
        if (userService.validateUser(newUser, user.getPassword())) {
            return "userHome";
        }
        return "signup";
    }

    @RequestMapping(value = "/userHome", method = RequestMethod.GET)
    public String userHome(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("newUser", session.getAttribute("newUser"));
        model.addAttribute("account", account);
        return "userHome";
    }

}
