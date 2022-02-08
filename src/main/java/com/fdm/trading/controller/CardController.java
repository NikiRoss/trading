package com.fdm.trading.controller;

import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CardController {

    @Autowired
    CardServiceImpl cardService;

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/user/addCard")
    public String addCard(@ModelAttribute("card")CreditCard card, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        cardService.registerCreditCard(card.getCardNo(), card.getExpiry(), card.getCvv(), user.getFirstName() + " " + user.getSurname(), user);
        return "cardRegisterSuccess";
    }

    @PostMapping("user/setActiveCard")
    public ModelAndView setActiveCard(@ModelAttribute("dbUser") User user, Principal p) {
        User dbUser = userService.findByUsername(p.getName());
        Account account = dbUser.getAccount();
        CreditCard card = new CreditCard();
        card.setNameOnCard(dbUser.getUsername() + dbUser.getSurname());

        List<CreditCard> creditCards = cardService.getListOfCardsForUser(dbUser);
        User user2 = dbUser;
        userService.save(dbUser);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("cardList", creditCards);
        modelMap.put("account", account);
        modelMap.put("card", card);
        modelMap.put("user", user);
        modelMap.put("dbUser", dbUser);
        return new ModelAndView("redirect:/", modelMap);
    }

    @GetMapping("user/toggle/card/{cardId}")
    public ModelAndView setActiveCard(@PathVariable String cardId, Principal p) {

        cardService.toggleCardStatus(Long.valueOf(cardId));
        User user = userService.findByUsername(p.getName());
        Map<String, Object> modelMap = new HashMap<>();
        List<CreditCard> creditCards = cardService.getListOfCardsForUser(user);
        Account account = user.getAccount();
        CreditCard card = new CreditCard();
        card.setNameOnCard(user.getUsername() + user.getSurname());
        modelMap.put("cardList", creditCards);
        modelMap.put("account", account);
        modelMap.put("card", card);
        modelMap.put("user", user);
        modelMap.put("dbUser", user);
        return new ModelAndView("redirect:/account", modelMap);
    }

}
