package com.fdm.trading.domain;

import javax.persistence.*;

@Entity
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="stocks_gen")
    @SequenceGenerator(name="stocks_gen", sequenceName="STOCKS_SEQ", allocationSize=1)
    @Column(name = "stock_id", nullable = false, updatable = false, unique = true)
    private long stockId;


    @Column(name = "company", unique = true)
    private String company;

    @Column(name = "ticker", unique = true)
    private String ticker;

    @Column(name = "share_price")
    private double sharePrice;

    @Column(name = "last_trade")
    private double lastTrade;

    @Column(name = "volume")
    private long volume;

    @Column(name = "opening_value")
    private double openingValue;

    @Column(name = "closing_value")
    private double closingValue;

    @Column(name = "profit_loss")
    private String pnl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_account")
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

    public double getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(double lastTrade) {
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

    public String getPnl() {
        return pnl;
    }

    public void setPnl(String pnl) {
        this.pnl = pnl;
    }
}
