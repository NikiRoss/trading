package com.fdm.trading.domain;

import javax.persistence.*;

@Entity
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_id", nullable = false, unique = true)
    private long stockId;

    @Column(name = "company", nullable = false, unique = true)
    private String company;

    @Column(name = "ticker", nullable = false, unique = true)
    private String ticker;

    @Column(name = "share_price", nullable = false)
    private double sharePrice;

    @Column(name = "last_trade", nullable = false)
    private long lastTrade;

    @Column(name = "volume", nullable = false)
    private long volume;

    @Column(name = "opening_value", nullable = false)
    private double openingValue;

    @Column(name = "closing_value", nullable = false)
    private double closingValue;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public long getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(long lastTrade) {
        this.lastTrade = lastTrade;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getOpeningValue() {
        return openingValue;
    }

    public void setOpeningValue(double openingValue) {
        this.openingValue = openingValue;
    }

    public double getClosingValue() {
        return closingValue;
    }

    public void setClosingValue(double closingValue) {
        this.closingValue = closingValue;
    }
}
