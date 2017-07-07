package com.money.deep.tstock.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.model.UserEncrypted;
import com.money.deep.tstock.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by fengxg on 2016/8/24.
 */
public class MyApplication extends Application{

    private static Context context;
    private static RequestQueue requestQueue;
    private static String stockcode;
    public static Activity mRunActivity;
//    private static final String stockString = "sh000001,sz399001,";
    private static final String stockString = "";
    public static float density;
    public  static boolean login_stauts = false;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        ScreenUtils(context);
        requestQueue = Volley.newRequestQueue(context);
//        SPUtils.put(context, "Anonymous", "");
//        SPUtils.put(context, "AnonymUserId", "");
//        SPUtils.put(context, "Production", "");
        stockcode = SPUtils.get(context,"stockCodes","").toString();
        System.out.println("stockcode:"+stockcode.equals(""));
        System.out.println("stockcode:"+stockcode);
        if(stockcode.equals("")){
            setName(stockString);
            SPUtils.put(context,"stockCodes",stockcode);
        }
        lauchInfo();
    }

    public static Context getContext(){return context;}
    public static RequestQueue getRequestQueue(){return requestQueue;}

    public void setName(String name){
        this.stockcode = name;
    }

    public String getName(){
        return stockcode;
    }

    public static void addString(String string){
        ArrayList<String> array = new ArrayList<String>();
        String[] str = stockcode.split(",");
        for(int i = 0;i < str.length;i++){
            String string_item = str[i];
            array.add(string_item);
        }
        if(!array.contains(string)){
            stockcode += string+",";
        }
        System.out.println("add stockcode:" + stockcode);
        SPUtils.put(context, "stockCodes", stockcode);
    }

    //删除本地字符串中的这个字段
    public static void removeString(String string){
        ArrayList<String> array = getArray();
        stockcode = "";
        for(int i = 0;i<array.size();i++){
            if(string.equals(array.get(i))){
                array.remove(i);
                i--;
                continue;
            }else{
                stockcode += array.get(i)+",";
            }

        }
        System.out.println("stockcode:"+ stockcode);
        SPUtils.put(context,"stockCodes",stockcode);
    }

    public static ArrayList<String> getArray(){
        ArrayList<String> array = new ArrayList<String>();
        String[] str = stockcode.split(",");
        for(int i = 0;i < str.length;i++){
            String string = str[i];
            if(!array.contains(string)){
                array.add(string);
            }
        }
        return array;
    }

//    public void ScreenUtils(Context context){
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
////        screenWidth = dm.widthPixels;
////        screenHeight = dm.heightPixels;
//        density = dm.density;
//    }

//    public static float getDensity(){
//        return density;
//    }
private void lauchInfo() {

    DataManager.launchInfo(new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            UserEncrypted userEncrypted = new Gson().fromJson(response.optString("UserEncrypted"),
                    new TypeToken<UserEncrypted>() {
                    }.getType());
            String DefaultStockBotId = response.optString("DefaultStockBotId");
            if(DefaultStockBotId != null){
                SPUtils.put(context, "DefaultStockBotId", DefaultStockBotId);
            }

            String decode_str = "";

            if(userEncrypted != null){
                String cleanLicense = userEncrypted.getCleanLicense();
                if(cleanLicense!=null&&cleanLicense.equals("true")){
                    try {
                        decode_str = URLDecoder.decode(userEncrypted.getAnonymous(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SPUtils.put(context, "Anonymous", decode_str);
                    SPUtils.put(context, "AnonymUserId", userEncrypted.getAnonymUserId());
                    SPUtils.put(context, "Production", "");
                }else{
                    String production = SPUtils.get(context,"Production", "").toString();
                    if (!production.equals("")){
                        login_stauts = true;
                    }
                    if (userEncrypted.getAnonymous() != null && !userEncrypted.getAnonymous().equals("")){
                        try {
                            decode_str = URLDecoder.decode(userEncrypted.getAnonymous(), "UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SPUtils.put(context, "Anonymous", decode_str);
                    }
                    if (userEncrypted.getAnonymUserId() != null && !userEncrypted.getAnonymUserId().equals("")) {
                        SPUtils.put(context, "AnonymUserId", userEncrypted.getAnonymUserId());
                    }
                }

            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
}

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
