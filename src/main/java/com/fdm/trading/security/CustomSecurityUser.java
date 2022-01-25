package com.fdm.trading.security;

import com.fdm.trading.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomSecurityUser extends User implements UserDetails {

    private static final long serialVersionUID = -4381938875186527688L;

    public CustomSecurityUser () {}

    public CustomSecurityUser(User user) {
        this.setUserAuthorities(user.getUserAuthorities());
        this.setUserId(user.getUserId());
        this.setPassword(user.getPassword());
        this.setUsername(user.getUsername());
        this.setSurname(user.getSurname());
        this.setAccount(user.getAccount());
        this.setFirstName(user.getFirstName());
        this.setEnabled(user.isEnabled());
        this.setEmail(user.getEmail());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getUserAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();

    }
}
