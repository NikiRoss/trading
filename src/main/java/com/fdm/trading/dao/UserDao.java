package com.fdm.trading.dao;

import com.fdm.trading.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByUserId(long userId);
    List<User> findAll();

    @Override
    void delete(User user);
}
