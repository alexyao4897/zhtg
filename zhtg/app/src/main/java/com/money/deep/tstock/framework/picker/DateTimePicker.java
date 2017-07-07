package com.money.deep.tstock.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.money.deep.tstock.framework.util.DateUtils;
import com.money.deep.tstock.framework.widget.WheelView;
import com.money.deep.tstock.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fengxg on 2016/3/30.
 */
public class DateTimePicker extends WheelPicker{

    private ArrayList<String> monthdays = new ArrayList<String>();
    private OnDatePickListener onDatePickListener;
    private String hourLabel = "时", minuteLabel = "分";
    private String selectedHour = "", selectedMinute = "";
    private int selectMonthDay = 0;
    private String selectedDay = "";
    private String monthday = "";
    private Activity ctx;
    private String submit_date;
    String current_year;
    String current_month;
    String current_day;

    public DateTimePicker(Activity activity) {
        super(activity);
        this.ctx = activity;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        current_year = date.substring(0, 4);
        current_month = date.substring(5, 7);
        current_day = date.substring(8, 10);
        if(Integer.parseInt(current_month)>2 && Integer.parseInt(current_month)<11){
            for(int i = Integer.parseInt(current_month)-2;i < Integer.parseInt(current_month)+2; i++){
                String month = String.valueOf(i);
                int maxdays = DateUtils.calculateDaysInMonth(Integer.parseInt(current_year), Integer.parseInt(month));
                for(int j = 1;j < maxdays + 1; j++){
                    String day = DateUtils.fillZero(j);
                    if(i == Integer.parseInt(current_month) && j== Integer.parseInt(current_day)){
                        monthdays.add("今天");
                    }else{
                        monthdays.add(month+"月"+day+"日");
                    }
                }
            }
        }else if(Integer.parseInt(current_month) == 1 || Integer.parseInt(current_month) == 2){
            for(int i = 1; i < Integer.parseInt(current_month)+2; i++){
                String month = String.valueOf(i);
                int maxdays = DateUtils.calculateDaysInMonth(Integer.parseInt(current_year), Integer.parseInt(month));
                for(int j = 1;j < maxdays + 1; j++){
                    String day = DateUtils.fillZero(j);
                    if(i == Integer.parseInt(current_month) && j== Integer.parseInt(current_day)){
                        monthdays.add("今天");
                    }else{
                        monthdays.add(month+"月"+day+"日");
                    }
                }
            }
        }else if(Integer.parseInt(current_month) == 11 || Integer.parseInt(current_month) == 12){
            for(int i = 10; i <= 12; i++){
                String month = String.valueOf(i);
                int maxdays = DateUtils.calculateDaysInMonth(Integer.parseInt(current_year), Integer.parseInt(month));
                for(int j = 1;j < maxdays + 1; j++){
                    String day = DateUtils.fillZero(j);
                    if(i == Integer.parseInt(current_month) && j== Integer.parseInt(current_day)){
                        monthdays.add("今天");
                    }else{
                        monthdays.add(month+"月"+day+"日");
                    }
                }
            }
        }

        selectedHour = DateUtils.fillZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        selectedMinute = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MINUTE));
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView view = new WheelView(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        view.setTextSize(textSize);
        view.setTextColor(textColorNormal, textColorFocus);
        view.setLineVisible(lineVisible);
        view.setLineColor(lineColor);
        view.setOffset(offset);
        layout.addView(view);

        submit_date = SPUtils.get(ctx, "submit_monthday", "").toString();
        if(submit_date==""){
            view.setItems(monthdays, getTodayItem());
        }else{
            view.setItems(monthdays, submit_date);
            if(!submit_date.equals("今天")){
                String[] date_str = submit_date.split("月");
                String month = date_str[0];
                String day = date_str[1].replace("日","");
                if(current_month.indexOf("0")==0){
                    current_month = current_month.replace("0","");
                }
                if(month.equals(current_month)&&day.equals(current_day)){
                    view.setItems(monthdays, "今天");
                }
            }

        }


        view.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectMonthDay = selectedIndex;
            }
        });

        WheelView hourView = new WheelView(activity);
        hourView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourView.setTextSize(textSize);
        hourView.setTextColor(textColorNormal, textColorFocus);
        hourView.setLineVisible(lineVisible);
        hourView.setLineColor(lineColor);
        layout.addView(hourView);
        TextView hourTextView = new TextView(activity);
        hourTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourTextView.setTextSize(textSize);
        hourTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(hourLabel)) {
            hourTextView.setText(hourLabel);
        }
        layout.addView(hourTextView);

        WheelView minuteView = new WheelView(activity);
        minuteView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteView.setTextSize(textSize);
        minuteView.setTextColor(textColorNormal, textColorFocus);
        minuteView.setLineVisible(lineVisible);
        minuteView.setLineColor(lineColor);
        minuteView.setOffset(offset);
        layout.addView(minuteView);
        TextView minuteTextView = new TextView(activity);
        minuteTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteTextView.setTextSize(textSize);
        minuteTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(minuteLabel)) {
            minuteTextView.setText(minuteLabel);
        }
        layout.addView(minuteTextView);
        ArrayList<String> hours = new ArrayList<String>();

        for (int i = 0; i < 24; i++) {
            hours.add(DateUtils.fillZero(i));
        }

        hourView.setItems(hours, selectedHour);
        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            minutes.add(DateUtils.fillZero(i));
        }
        minuteView.setItems(minutes, selectedMinute);
        hourView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedHour = item;
            }
        });
        minuteView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedMinute = item;
            }
        });

        return layout;
    }


    @Override
    protected void onSubmit() {
        if (onDatePickListener != null) {
            monthday = getSelectedMonthDay();
            SPUtils.put(ctx, "submit_monthday", monthday);
            ((onMonthDayTimePickListener) onDatePickListener).onDatePicked(monthday, selectedHour, selectedMinute);

        }
    }


    public void setSelectHour(String selectHour){
        this.selectedHour = selectHour;
    }

    public void setSelectedMinute(String selectedMinute){
        this.selectedMinute = selectedMinute;
    }

    public void setSelecteDay(String selecteDay){
        this.selectedDay = selecteDay;
    }


    public String getSelectedMonthDay() {
        return monthdays.get(selectMonthDay);
    }

    public int getTodayItem(){
        for(int i=0;i<monthdays.size();i++){
            if(monthdays.get(i).equals("今天")){
                selectMonthDay = i;
                return selectMonthDay;
            }
        }
        return selectMonthDay;
    }

    protected interface OnDatePickListener {

    }

    public void setOnDatePickListener(OnDatePickListener listener) {
        this.onDatePickListener = listener;
    }

    public interface onMonthDayTimePickListener extends OnDatePickListener{
        void onDatePicked(String monthday, String hour, String minute);
    }
}
