package com.fdm.trading.controller;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import com.fdm.trading.events.OnRegistrationCompleteEvent;
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
            User registered = userService.createNewUser(result, user, "ROLE_USER");

            String appUrl = request.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));
            System.out.println("Event publisher being called");

        } catch (RuntimeException | NameFormatException | UnsecurePasswordException | UserAlreadyExistException ex) {
            ModelAndView modelAndView = new ModelAndView("signup", "user", user);
            modelAndView.addObject("ErrorMessage",  "EXCEPTIONNN");
            return modelAndView;
        }

        return new ModelAndView("authenticateRegister", "user", user);
    }

/*    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (tokenService.tokenIsInvalid(verificationToken)) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if (tokenService.tokenHasExpired(verificationToken, cal)) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        user.setEnabled(true);
        userService.save(user);
        return "redirect:/login";
    }*/

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
