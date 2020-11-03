package com.fdm.trading.events;

import com.fdm.trading.domain.User;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String ADMIN_EMAIL = "admin@tradingapp.com";
 
    @Autowired
    private UserServiceImpl userService;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Value("${support.email}")
    private String supportEmail;
 
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        System.out.println("Event received - confirming registration");
        this.confirmRegistration(event);
        System.out.println("confirm registration done");
    }

 
    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
        System.out.println("message "+email.getText() + " sent to "+email.getTo()[0]);
    }

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm/" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setFrom("admin@trading.com");
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(supportEmail);
        return email;
    }

    private SimpleMailMessage errorExceptionAlertEmail(Exception exception, LocalDateTime timestamp){
        //StringWriter writer = new StringWriter();
        //writer = exception.getStackTrace();
        StackTraceElement[] st = exception.getStackTrace();
        final String message = exception.getMessage();
        final SimpleMailMessage email = new SimpleMailMessage();
        String exceptionClass = exception.getClass().getSimpleName();
        email.setFrom("app@tradingapp.com");
        email.setTo(ADMIN_EMAIL);
        email.setSubject(exceptionClass);
        List<String> stringList = new ArrayList<>();
        for (int i=0; i<st.length;i++){
            stringList.add(st[i].toString());
        }
        email.setText(exceptionClass +" thrown at "+ timestamp+" with message: "+ message +"\r\n\nStacktrace:\n"+ stringList.toString());
        return email;

    }

    public void sendExceptionEmail(Exception exception, LocalDateTime timestamp){
        SimpleMailMessage mail = errorExceptionAlertEmail(exception, timestamp);
        mailSender.send(mail);
    }
}