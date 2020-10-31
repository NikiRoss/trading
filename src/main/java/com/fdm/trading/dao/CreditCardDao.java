package com.fdm.trading.dao;

import com.fdm.trading.domain.CreditCard;
import org.springframework.data.repository.CrudRepository;

public interface CreditCardDao extends CrudRepository<CreditCard, Long> {

    CreditCard findByCardId(long cardId);

    CreditCard findCreditCardByNameOnCard(String name);

    CreditCard findByCardNo(String number);
}
