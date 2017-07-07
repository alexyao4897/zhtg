package com.money.deep.tstock.model;

/**
 * Created by fengxg on 2016/9/8.
 */
public class StockItem {
    private String StockId;
    private String StockName;
    private String StockCode;
    private String StockCountry;
    private boolean isZixuan = false;

    public boolean isZixuan() {
        return isZixuan;
    }

    public void setZixuan(boolean zixuan) {
        isZixuan = zixuan;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String stockId) {
        StockId = stockId;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public String getStockCountry() {
        return StockCountry;
    }

    public void setStockCountry(String stockCountry) {
        StockCountry = stockCountry;
    }
}
