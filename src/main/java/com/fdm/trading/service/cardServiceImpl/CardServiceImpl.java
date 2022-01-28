package com.fdm.trading.service.cardServiceImpl;

import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CardServiceImpl {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CreditCardDao cardDao;

    @Autowired
    private UserDao userDao;

    public Set<CreditCard> registerCreditCard(String cardNo, String expiry, int cvv, String name, User user){
        CreditCard creditCard = new CreditCard();
        String shortenedCardNo = cardNo.substring(0, cardNo.length() - 4);
        String lastFour = cardNo.substring(cardNo.length() - 4);
        creditCard.setCardNo(encoder.encode(shortenedCardNo) + lastFour);
        creditCard.setExpiry(expiry);
        creditCard.setCvv(cvv);
        creditCard.setNameOnCard(name);
        cardDao.save(creditCard);
        Set<CreditCard> creditCards = user.getCreditCard() != null ? user.getCreditCard() : new HashSet<>();
        creditCards.add(creditCard);
        user.setCreditCard(creditCards);
        Set<CreditCard> cards = new HashSet<>();
        cards.add(creditCard);
        return cards;
    }

    public CreditCard findCardByNameOnCard(String name){
        return cardDao.findCreditCardByNameOnCard(name);
    }

    public CreditCard findCardByLongNumber(String number){
        return cardDao.findByCardNo(number);
    }



}
