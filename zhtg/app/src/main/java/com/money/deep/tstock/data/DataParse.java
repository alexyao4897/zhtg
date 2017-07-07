package com.money.deep.tstock.data;

import android.util.SparseArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParse {
    private ArrayList<MinutesBean> datas = new ArrayList<MinutesBean>();
    private ArrayList<KLineBean> kDatas = new ArrayList<KLineBean>();
    private float baseValue;
    private float permaxmin;
    private float volmax;
    private String code = "sz002081";
    private SparseArray<String> xValuesLabel=new SparseArray<String>();

    public void parseMinutes(JSONObject object) {
        JSONArray jsonArray = object.optJSONObject("data").optJSONObject(code).optJSONObject("data").optJSONArray("data");
        String date = object.optJSONObject("data").optJSONObject(code).optJSONObject("data").optString("date");
        if (date.length() == 0) {
            return;
        }
/*数据解析依照自己需求来定，如果服务器直接返回百分比数据，则不需要客户端进行计算*/
        baseValue = (float) object.optJSONObject("data").optJSONObject(code).optJSONObject("qt").optJSONArray(code).optDouble(4);
        System.out.println("baseValue:"+baseValue);
        int count = jsonArray.length();
        for (int i = 0; i < count; i++) {
            String[] t = jsonArray.optString(i).split(" ");/*  "0930 9.50 4707",*/
            MinutesBean minutesData = new MinutesBean();
            System.out.println("minutesData.time:"+t[0]);
            minutesData.time = t[0].substring(0, 2) + ":" + t[0].substring(2);
            System.out.println("minutesData.time:"+minutesData.time);
            minutesData.cjprice = Float.parseFloat(t[1]);
            if (i != 0) {
                String[] pre_t = jsonArray.optString(i - 1).split(" ");
                minutesData.cjnum = Integer.parseInt(t[2]) - Integer.parseInt(pre_t[2]);
                minutesData.total = minutesData.cjnum * minutesData.cjprice + datas.get(i - 1).total;
                minutesData.avprice = (minutesData.total) / Integer.parseInt(t[2]);
            } else {
                minutesData.cjnum = Integer.parseInt(t[2]);
                minutesData.avprice = minutesData.cjprice;
                minutesData.total = minutesData.cjnum * minutesData.cjprice;
            }
            minutesData.cha = minutesData.cjprice - baseValue;
            minutesData.per = (minutesData.cha / baseValue);
            double cha = minutesData.cjprice - baseValue;
            if (Math.abs(cha) > permaxmin) {
                permaxmin = (float) Math.abs(cha);
            }
            volmax = Math.max(minutesData.cjnum, volmax);
            datas.add(minutesData);
        }

        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }

    public void parseMinutes(String string,String cur_price,String diff){
        float cur_val = Float.parseFloat(cur_price);
        float diff_value = Float.parseFloat(diff);
        baseValue = cur_val + diff_value*7/10;
        String sub_str = string.trim().substring(1, string.length() - 2);
        try{
            JSONObject jsonObject = new JSONObject(sub_str);
            String array_str = jsonObject.getJSONArray("Data").getString(0);
            JSONArray jsonArray = new JSONArray(array_str);
            //时间20160831100700,价格,成交额,成交量
            for(int i = 0; i < jsonArray.length(); i++){
                MinutesBean minutesBean = new MinutesBean();
                JSONArray jsonArray1 = new JSONArray(jsonArray.getString(i));
                for(int j = 0;j < jsonArray1.length();j++){
                    String time = jsonArray1.getString(0);
                    String time_sub = time.substring(8, time.length() - 2);
                    String time_last = time_sub.substring(0,2)+":"+time_sub.substring(2);
                    minutesBean.time = time_last;
                    minutesBean.cjprice = (float)jsonArray1.getDouble(1)/100.0f;
                    minutesBean.cjnum = (float)jsonArray1.getDouble(2);
                    minutesBean.total = (float)jsonArray1.getDouble(3);
                    minutesBean.avprice = (float)jsonArray1.getDouble(4);

                    minutesBean.cha = minutesBean.cjprice - baseValue;
                    minutesBean.per = (minutesBean.cha / baseValue);
                    double cha = minutesBean.cjprice - baseValue;
                    if (Math.abs(cha) > permaxmin) {
                        permaxmin = (float) Math.abs(cha);
                    }
                    volmax = Math.max(minutesBean.cjnum, volmax);

//                    System.out.println("time:"+minutesBean.time);
//                    System.out.println("cjprice:"+minutesBean.cjprice);
//                    System.out.println("cjnum:"+minutesBean.cjnum);
//                    System.out.println("total:"+minutesBean.total);
                }
                datas.add(minutesBean);
            }
            if (permaxmin == 0) {
                permaxmin = baseValue * 0.02f;
            }
            System.out.println("datas:" + +datas.size()+ datas.toString());
        }catch (Exception e){

        }
    }


    public void parseKLineOffline(JSONObject obj) {
        ArrayList<KLineBean> kLineBeans = new ArrayList<KLineBean>();
        JSONObject data = obj.optJSONObject("data").optJSONObject(code);
        JSONArray list = data.optJSONArray("day");
        if (list != null) {
            int count = list.length();
            for (int i = 0; i < count; i++) {
                JSONArray dayData = list.optJSONArray(i);
                KLineBean kLineData = new KLineBean();
                kLineData.date = dayData.optString(0);
                kLineData.open = (float) dayData.optDouble(1);
                kLineData.close = (float) dayData.optDouble(2);
                kLineData.high = (float) dayData.optDouble(3);
                kLineData.low = (float) dayData.optDouble(4);
                kLineData.vol = (float) dayData.optDouble(5);
                volmax = Math.max(kLineData.vol, volmax);
                xValuesLabel.put(i, kLineData.date);
                kLineBeans.add(kLineData);
            }
        }
        kDatas.addAll(kLineBeans);
    }

    public void parseKLine(JSONObject jsonObject,int num){
/*        ArrayList<KLineBean> kLineBeans = new ArrayList<>();*/
        JSONArray list = jsonObject.optJSONArray("record");
        if(list != null && list.length()> num ){
            if(num == 0){
                for(int i = 0;i < list.length();i++){
                    JSONArray dayData = list.optJSONArray(i);
                    KLineBean kLineData = new KLineBean();
                    kLineData.date = dayData.optString(0);
                    kLineData.open = (float) dayData.optDouble(1);
                    kLineData.high = (float) dayData.optDouble(2);
                    kLineData.close = (float) dayData.optDouble(3);
                    kLineData.low = (float) dayData.optDouble(4);
                    kLineData.vol = (float) dayData.optDouble(5);
                    volmax = Math.max(kLineData.vol, volmax);
                    xValuesLabel.put(i, kLineData.date);
                    kDatas.add(kLineData);
                }
            }else{
                for(int i = list.length()-num;i < list.length();i++){
                    JSONArray dayData = list.optJSONArray(i);
                    KLineBean kLineData = new KLineBean();
                    kLineData.date = dayData.optString(0);
                    kLineData.open = (float) dayData.optDouble(1);
                    kLineData.high = (float) dayData.optDouble(2);
                    kLineData.close = (float) dayData.optDouble(3);
                    kLineData.low = (float) dayData.optDouble(4);
                    kLineData.vol = (float) dayData.optDouble(5);
                    volmax = Math.max(kLineData.vol, volmax);
                    xValuesLabel.put(i, kLineData.date);
                    kDatas.add(kLineData);
                }
            }
        }
/*        kDatas.addAll(kLineBeans);*/
    }

    public float getMin() {
        return baseValue - permaxmin;
    }

    public float getMax() {
        return baseValue + permaxmin;
    }

    public float getPercentMax() {
        return permaxmin / baseValue;
    }

    public float getPercentMin() {
        return -getPercentMax();
    }


    public float getVolmax() {
        return volmax;
    }


    public ArrayList<MinutesBean> getDatas() {
        return datas;
    }

    public ArrayList<KLineBean> getKLineDatas() {
        return kDatas;
    }
    public SparseArray<String> getXValuesLabel() {
        return xValuesLabel;
    }
}
