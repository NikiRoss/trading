package com.fdm.trading.dao;

import com.fdm.trading.domain.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Long> {


    @Query("select * from User where username = :username")
    User findByUsername(String username);

    User findByEmail(String email);

    User findByUserId(long userId);

    User findByPassword(String password);

    List<User> findAll();

    @Override
    void delete(User user);
}
