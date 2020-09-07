package com.fdm.trading.dao;

import com.fdm.trading.domain.User;
import com.fdm.trading.domain.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenDao extends CrudRepository<VerificationToken, Long> {

    VerificationToken findById(long id);

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
