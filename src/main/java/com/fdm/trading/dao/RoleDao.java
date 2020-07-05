package com.fdm.trading.dao;

import org.springframework.data.repository.CrudRepository;

import com.fdm.trading.security.Role;

public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
