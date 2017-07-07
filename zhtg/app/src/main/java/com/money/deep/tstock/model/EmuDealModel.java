package com.money.deep.tstock.model;

/**
 * Created by fengxg on 2016/9/18.
 */
public class EmuDealModel {
    String StockDealerId;
    String StockCode;
    String StockId;
    String Shares;
    String ActionPrice;
    String ActionTime;
    String StockName;
    boolean IsSale;
    boolean IsOpenClose;
    String ActionDate;
    String ActionAmount;
    String DealLogId;

    public String getActionAmount() {
        return ActionAmount;
    }

    public void setActionAmount(String actionAmount) {
        ActionAmount = actionAmount;
    }

    public String getStockDealerId() {
        return StockDealerId;
    }

    public void setStockDealerId(String stockDealerId) {
        StockDealerId = stockDealerId;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String stockId) {
        StockId = stockId;
    }

    public String getShares() {
        return Shares;
    }

    public void setShares(String shares) {
        Shares = shares;
    }

    public String getActionPrice() {
        return ActionPrice;
    }

    public void setActionPrice(String actionPrice) {
        ActionPrice = actionPrice;
    }

    public String getActionTime() {
        return ActionTime;
    }

    public void setActionTime(String actionTime) {
        ActionTime = actionTime;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public boolean isSale() {
        return IsSale;
    }

    public void setIsSale(boolean isSale) {
        IsSale = isSale;
    }

    public boolean isOpenClose() {
        return IsOpenClose;
    }

    public void setIsOpenClose(boolean isOpenClose) {
        IsOpenClose = isOpenClose;
    }

    public String getActionDate() {
        return ActionDate;
    }

    public void setActionDate(String actionDate) {
        ActionDate = actionDate;
    }

    public String getDealLogId() {
        return DealLogId;
    }

    public void setDealLogId(String dealLogId) {
        DealLogId = dealLogId;
    }
}
