package com.fdm.trading.service.cardServiceImpl;

import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.dao.UserDao;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CreditCardDao cardDao;

    @Autowired
    private UserDao userDao;

    public CreditCard registerCreditCard(String cardNo, String expiry, int cvv, String name){
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNo(encoder.encode(cardNo));
        creditCard.setExpiry(encoder.encode(expiry));
        creditCard.setCvv(cvv);
        creditCard.setNameOnCard(name);
        cardDao.save(creditCard);

        return creditCard;
    }

    public CreditCard findCardByNameOnCard(String name){
        return cardDao.findCreditCardByNameOnCard(name);
    }

    public CreditCard findCardByLongNumber(String number){
        return cardDao.findByCardNo(number);
    }

    public CreditCard validateCardForTransaction(String enteredLastFour, String username) throws Exception {
        User user = userDao.findByUsername(username);
        CreditCard card = user.getCreditCard();
        String userLastFour = card.getCardNo().substring(card.getCardNo().length() - 4);

        if (!userLastFour.equals(enteredLastFour)) {
            throw new Exception();
        }
        return card;
    }
}
