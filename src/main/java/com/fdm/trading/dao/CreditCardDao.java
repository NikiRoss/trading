package com.fdm.trading.dao;

import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CreditCardDao extends CrudRepository<CreditCard, Long> {

    CreditCard findByCardId(long cardId);

    CreditCard findCreditCardByNameOnCard(String name);

    CreditCard findByCardNo(String number);

    List<CreditCard> findByUser(User user);
}
