package com.fdm.trading.service.cardServiceImpl;

import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CardServiceImpl {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CreditCardDao cardDao;

    @Autowired
    private UserDao userDao;

    public List<CreditCard> registerCreditCard(String cardNo, String expiry, int cvv, String name, User user){
        CreditCard creditCard = new CreditCard();
        String shortenedCardNo = cardNo.substring(0, cardNo.length() - 4);
        String lastFour = cardNo.substring(cardNo.length() - 4);
        creditCard.setCardNo(encoder.encode(shortenedCardNo) + lastFour);
        creditCard.setExpiry(expiry);
        creditCard.setCvv(cvv);
        creditCard.setNameOnCard(name);
        creditCard.setUser(user);
        cardDao.save(creditCard);
        List<CreditCard> creditCards = user.getCreditCard() != null ? user.getCreditCard() : new ArrayList<>();
        creditCards.add(creditCard);
        user.setCreditCard(creditCards);
        userDao.save(user);
        return creditCards;
    }

    public CreditCard findCardByNameOnCard(String name){
        return cardDao.findCreditCardByNameOnCard(name);
    }

    public CreditCard findCardByLongNumber(String number){
        return cardDao.findByCardNo(number);
    }

    public List<CreditCard> getListOfCardsForUser(User user) {
        return cardDao.findByUser(user);
    }

    public CreditCard findByCardId(long cardId) {
        return cardDao.findByCardId(cardId);
    }

    public void toggleCardStatus(long cardId) {
        CreditCard creditCard = findByCardId(cardId);
        creditCard.setEnabled(!creditCard.isEnabled());
        cardDao.save(creditCard);
    }



}
