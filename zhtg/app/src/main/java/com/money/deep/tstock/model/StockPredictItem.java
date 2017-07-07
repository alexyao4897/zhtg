package com.money.deep.tstock.model;

/**
 * Created by fengxg on 2016/9/8.
 */
public class StockPredictItem {
    private String StockId;
    private String StockCode;
    private String StockName;
    private String StockCountry;
    private String PredictDate;
    private String PredictTime;
    private String ProbSmooth;
    private String ProbRise;
    private String ProbFall;
    private boolean CanTrade;

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String stockId) {
        StockId = stockId;
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

    public String getPredictDate() {
        return PredictDate;
    }

    public void setPredictDate(String predictDate) {
        PredictDate = predictDate;
    }

    public String getPredictTime() {
        return PredictTime;
    }

    public void setPredictTime(String predictTime) {
        PredictTime = predictTime;
    }

    public boolean isCanTrade() {
        return CanTrade;
    }

    public void setCanTrade(boolean canTrade) {
        CanTrade = canTrade;
    }

    public String getProbFall() {
        return ProbFall;
    }

    public void setProbFall(String probFall) {
        ProbFall = probFall;
    }

    public String getProbRise() {
        return ProbRise;
    }

    public void setProbRise(String probRise) {
        ProbRise = probRise;
    }

    public String getProbSmooth() {
        return ProbSmooth;
    }

    public void setProbSmooth(String probSmooth) {
        ProbSmooth = probSmooth;
    }
}
