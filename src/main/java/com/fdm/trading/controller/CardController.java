package com.fdm.trading.controller;

import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class CardController {

    @Autowired
    CardServiceImpl cardService;

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/user/addCard")
    public String addCard(@ModelAttribute("card")CreditCard card, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Set<CreditCard> cards = user.getCreditCard() != null ? user.getCreditCard() : new HashSet<>();
        cards.add(card);
        user.setCreditCard(cards);

        cardService.registerCreditCard(card.getCardNo(), card.getExpiry(), card.getCvv(), card.getNameOnCard(), user);
        userService.save(user);
        return "cardRegisterSuccess";
    }
}
