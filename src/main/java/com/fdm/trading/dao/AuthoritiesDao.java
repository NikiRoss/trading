package com.fdm.trading.dao;

import com.fdm.trading.security.Authorities;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesDao extends CrudRepository<Authorities, Long> {

}
