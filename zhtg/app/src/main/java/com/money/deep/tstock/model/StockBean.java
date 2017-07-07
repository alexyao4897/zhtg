package com.money.deep.tstock.model;

/**
 * Created by fengxg on 2016/9/7.
 */
public class StockBean {
    private String Stock;
    private String ProbRise;
    private String ProbSmooth;
    private String ProbFall;

//    public String getStock() {
//        String stock_name = "";
//        if(!TextUtils.isEmpty(Stock)&&Stock.length()>6){
//            stock_name =  Stock.substring(2);
//        }
//        return stock_name;
//    }
        public String getStock() {
            return Stock;
        }

    public void setStock(String stock) {
        Stock = stock;
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

    public String getProbFall() {
        return ProbFall;
    }

    public void setProbFall(String probFall) {
        ProbFall = probFall;
    }
}
