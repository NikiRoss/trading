package com.fdm.trading.domain;

import com.fdm.trading.security.Authorities;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Scope("session")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, unique = true)
    private long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "DOB")
    private String dob;

    @OneToOne
    private Account account;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CreditCard> creditCard;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="user")
    private Set<Authorities> userAuthorities = new HashSet<>();

    public User(){}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authorities> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(Set<Authorities> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public Set<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Set<CreditCard> creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dob=" + dob +
                ", account=" + account +
                ", enabled=" + enabled +
                ", userAuthorities=" + userAuthorities +
                '}';
    }
}
