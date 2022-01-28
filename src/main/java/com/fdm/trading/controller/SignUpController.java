package com.fdm.trading.controller;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import com.fdm.trading.events.OnRegistrationCompleteEvent;
import com.fdm.trading.events.RegistrationListener;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.service.tokenService.VerificationTokenServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import com.fdm.trading.utils.mail.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Controller
public class SignUpController {


    @Autowired
    UserServiceImpl userService;

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    VerificationTokenServiceImpl tokenService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthoritiesDao authoritiesDao;

    @Autowired
    private RegistrationListener listner;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;

    @GetMapping("/signup")
    public String getSignUp(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("message", "Please sign up!");
        return "signup";
    }

    @PostMapping("signup/user/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) {
        System.out.println("IN HERE!");

        try {
            User registered = userService.createNewUser(result, user, "ROLE_USER", null);

            String appUrl = request.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));
            System.out.println("Event publisher being called");

        } catch (RuntimeException | NameFormatException | UnsecurePasswordException | UserAlreadyExistException ex) {
            listner.sendExceptionEmail(ex, LocalDateTime.now());
            ModelAndView modelAndView = new ModelAndView("error", "user", user);
            modelAndView.addObject("message",  ex.getMessage());
            return modelAndView;
        }

        return new ModelAndView("authenticateRegister", "user", user);
    }


    @GetMapping("/signup/user/registration/registrationConfirm/{token}")
    public String verificationSuccess(@PathVariable String token){
        VerificationToken dbToken = tokenService.findByToken(token);
        User user = dbToken.getUser();
        //Remove if doesn't work
        User dbUser = userService.findByUsername(user.getUsername());
        dbUser.setEnabled(true);

        //dbUser.setEnabled(true);
        userService.save(dbUser);
        return "successRegister";
    }



}
