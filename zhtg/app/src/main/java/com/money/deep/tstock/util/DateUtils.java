package com.money.deep.tstock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengxg on 2016/9/9.
 */
public class DateUtils {
    public static Date getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date getBeforeYear(Date date){
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(date); //设置时间为当前时间
        ca.add(Calendar.YEAR, -1); //年份减1
        Date beforeyear = ca.getTime();
        return beforeyear;
    }

    public static String getBeforeMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat format =  new SimpleDateFormat("MM");
        String month = format.format(c.getTime());
        return month;
    }

    public static String getYear(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date_str = sf.format(date);
        String year_str = date_str.substring(0,4);
        return year_str;
    }
}
