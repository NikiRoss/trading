package com.fdm.trading.controller;

import com.fdm.trading.dao.AuthoritiesDao;
import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import com.fdm.trading.events.OnRegistrationCompleteEvent;
import com.fdm.trading.exceptions.NameFormatException;
import com.fdm.trading.exceptions.UnsecurePasswordException;
import com.fdm.trading.exceptions.UserAlreadyExistException;
import com.fdm.trading.security.Authorities;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


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

/*    @PostMapping(value = "/signup/adduser")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) throws NameFormatException, UnsecurePasswordException {
        System.out.println("Adding user...");
        model.addAttribute("user", user);
       
        String exceptionMessage;
        try {
            userService.createNewUserAlt(result, user, "ROLE_USER");
        }catch (NameFormatException nfe){
            exceptionMessage = "user "+user.getFirstName()+" "+ user.getSurname() +" has numeric or special character";
            model.addAttribute("message", exceptionMessage);
            model.addAttribute("user", new User());
            return "signup";
        } catch (UnsecurePasswordException upe) {
            //need to update error message to reflect '@Min' constraint applied to password field
            exceptionMessage = "user " + user.getFirstName() + " " + user.getSurname() + " password contains either 'password' or reference to first or surname";
            model.addAttribute("message", exceptionMessage);
            model.addAttribute("user", new User());
            return "signup";
        } catch (UserAlreadyExistException ue) {
            //need to update error message to reflect '@Min' constraint applied to password field
            exceptionMessage = "user " + user.getUsername() + " already exists";
            model.addAttribute("message", exceptionMessage);
            model.addAttribute("user", new User());
            return "signup";
        }
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

    }*/

    @PostMapping("signup/user/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) {
        System.out.println("IN HERE!");

        try {
            User registered = new User();
            registered.setUsername(user.getUsername());
            registered.setFirstName(user.getFirstName());
            registered.setSurname(user.getSurname());
            registered.setEmail(user.getEmail());
            registered.setPassword(passwordEncoder.encode(user.getPassword()));
            registered.setEnabled(false);
            System.out.println("User details set");

            Set<Authorities> authoritiesSet = new HashSet<>();
            Authorities authority = new Authorities();
            authority.setUser(registered);
            authority.setAuthority(authority.getAuthority());
            authoritiesSet.add(authority);
            System.out.println("Authorities set");

            registered.setUserAuthorities(authoritiesSet);
            System.out.println("User authorities set");

            userService.save(registered);
            System.out.println("New user saved");

            authoritiesDao.save(authority);
            String appUrl = request.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));
            System.out.println("Event publisher being called");

        } catch (RuntimeException ex) {
            return new ModelAndView("error", "user", user);
        }
        return new ModelAndView("successRegister", "user", user);
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiry().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        user.setEnabled(true);
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/signup/user/registration/registrationConfirm/{token}")
    public String verificationSuccess(@PathVariable String token){
        VerificationToken dbToken = tokenService.findByToken(token);
        User user = dbToken.getUser();
        //Remove if doesn't work
        user.setEnabled(true);
        //User dbUser = userService.findByUsername(user.getUsername());
        //dbUser.setEnabled(true);
        userService.save(user);
        return "successRegister";
    }

}
