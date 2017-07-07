package com.money.deep.tstock.model;

import java.io.Serializable;

/**
 * Created by fengxg on 2016/8/25.
 */
public class ShareEntry implements Serializable{
    private String name;
    private String high;
    private String low;
    private String current;
    private String open;
    private String close;
    private String code;
    private String q_code;

    public String getQ_code() {
        return q_code;
    }

    public void setQ_code(String q_code) {
        this.q_code = q_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
