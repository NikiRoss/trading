package com.fdm.trading.controller;

import com.fdm.trading.domain.User;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import com.fdm.trading.utils.mail.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class SignUpController {


    @Autowired
    UserServiceImpl userService;

    @Autowired
    EmailConfig emailConfig;

    @GetMapping("/signup")
    public String getSignUp(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("message", "Please sign up!");
        return "signup";
    }

    @PostMapping(value = "/signup/adduser")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) throws NameFormatException {
        System.out.println("Adding user...");
        model.addAttribute("user", user);
        if(userService.inputValidator(user.getFirstName()) == false || userService.inputValidator(user.getSurname()) == false){
            System.out.println("Please do not use numeric or special character");
            throw new NameFormatException("user "+user.getFirstName()+" "+ user.getSurname() +" has numeric or special character");

        }
        if(result.hasErrors()){
            model.addAttribute("message", result.getFieldError().getDefaultMessage());
            model.addAttribute("user", new User());
            return "signup";
        }
        userService.createNewUserAlt(user, "ROLE_USER");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("niki.ross49@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Activate Account");
        message.setText("Hi " + user.getFirstName() + " please click below to activate your email");
        mailSender.send(message);
        return "login";

    }

}
