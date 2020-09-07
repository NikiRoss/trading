package com.fdm.trading.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PaymentDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "card_num")
    private String cardNumber;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "name_on_card")
    private String nameOnCard;

    @Column(name = "scc")
    private int scc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public int getScc() {
        return scc;
    }

    public void setScc(int scc) {
        this.scc = scc;
    }
}
