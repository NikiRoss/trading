package com.fdm.trading.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue
    public long cardId;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "cvv")
    private int cvv;

    @Column(name = "name_on_card")
    private String nameOnCard;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardId=" + cardId +
                ", cardNo='" + cardNo + '\'' +
                ", expiry='" + expiry + '\'' +
                ", cvv=" + cvv +
                ", nameOnCard='" + nameOnCard + '\'' +
                '}';
    }
}
