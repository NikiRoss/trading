package com.fdm.trading.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="account_gen")
    @SequenceGenerator(name="account_gen", sequenceName="ACCOUNT_SEQ", allocationSize=1)
    @Column(name = "account_id", nullable = false, updatable = false, unique = true)
    private long accountId;

    @Column(name = "account_no", nullable = false, updatable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_balance")
    private double accountBalance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Stocks> stocksList;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transaction> transactionList;



    public Account() {
        this.accountBalance = 1000000.00;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<Stocks> getStocksList() {
        return stocksList;
    }

    public void setStocksList(List<Stocks> stocksList) {
        this.stocksList = stocksList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountBalance=" + accountBalance +
                ", stocksList=" + stocksList +
                ", transactionList=" + transactionList +
                '}';
    }
}
