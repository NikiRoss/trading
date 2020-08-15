package com.fdm.trading.security;

import com.fdm.trading.domain.User;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Authorities implements GrantedAuthority {

    private static final long serialVersionUID = -8123526131047887755L;

    private Long id;
    private String authority;
    private User user;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public Authorities() {
        super();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
