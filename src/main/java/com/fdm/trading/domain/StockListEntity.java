package com.fdm.trading.domain;

import javax.persistence.*;

@Entity
public class StockListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="stockList_id", nullable = false, unique = true)
    private long stockListId;

    @Column(name="accountId")
    private long accountId;

    @Column(name = "stockId")
    private long stockId;

    @Column(name = "volume")
    private long volume;

    public long getStockListId() {
        return stockListId;
    }

    public void setStockListId(long stockListId) {
        this.stockListId = stockListId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StockListEntity{" +
                "stockListId=" + stockListId +
                ", accountId=" + accountId +
                ", stockId=" + stockId +
                '}';
    }
}
