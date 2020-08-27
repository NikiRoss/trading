package com.fdm.trading.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="transaction_gen")
    @SequenceGenerator(name="transaction_gen", sequenceName="TRANSACTION_SEQ", allocationSize=1)
    @Column(name = "transaction_id", nullable = false, updatable = false, unique = true)
    private long transactionId;


    @Column(name = "volume", nullable = false)
    private long volume;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_trans_account")
    private Account account;

    @OneToOne
    @JoinColumn(name = "fk_stockId")
    private Stocks stocks;

    @Column(name = "purchase", nullable = false)
    private boolean purchase;



    public Transaction() {
        super();
    }


    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", volume=" + volume +
                ", price=" + price +
                ", date=" + date +
                ", purchase=" + purchase +
                '}';
    }
}
