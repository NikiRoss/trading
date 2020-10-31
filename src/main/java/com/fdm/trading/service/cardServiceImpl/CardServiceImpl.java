package com.fdm.trading.service.cardServiceImpl;

import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.domain.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CreditCardDao cardDao;

    public void registerCreditCard(String cardNo, String expiry, int cvv, String name){
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNo(encoder.encode(cardNo));
        creditCard.setExpiry(encoder.encode(expiry));
        creditCard.setCvv(cvv);
        creditCard.setNameOnCard(name);
        cardDao.save(creditCard);
    }

    public CreditCard findCardByNameOnCard(String name){
        return cardDao.findCreditCardByNameOnCard(name);
    }

    public CreditCard findCardByLongNumber(String number){
        return cardDao.findByCardNo(number);
    }
}
