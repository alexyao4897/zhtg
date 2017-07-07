package com.money.deep.tstock.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.http.MyStringRequest;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.util.SPUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengxg on 2016/8/24.
 */
public class DataManager {
    public static int REQUEST_TRY_TIMES=1;//重试次数
    public static int REQUEST_TIME_OUT=10*1000;//超时时间设置
    private final static RetryPolicy retryPolicyDefault = new DefaultRetryPolicy(REQUEST_TIME_OUT, REQUEST_TRY_TIMES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    public static String REQUEST_TAG="zntg_request_tag";

    public static void post(final String url, final Map<String, String> param, final RetryPolicy retryPolicy, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,null, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return initHashMap(url, param);
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                if (retryPolicy == null) {
                    return retryPolicyDefault;
                }
                return retryPolicy;
            }
        };
        postRequest.setTag(REQUEST_TAG);
        MyApplication.getRequestQueue().add(postRequest);
    }

    public static void post(final String url, final RetryPolicy retryPolicy, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,null, listener, errorListener) {

            @Override
            public RetryPolicy getRetryPolicy() {
                if (retryPolicy == null) {
                    return retryPolicyDefault;
                }
                return retryPolicy;
            }
        };
        postRequest.setTag(REQUEST_TAG);
        MyApplication.getRequestQueue().add(postRequest);
    }

    public static void post(String url, Map<String, String> param, Response.Listener listener, Response.ErrorListener errorListener) {
        post(url, param, retryPolicyDefault, listener, errorListener);
    }

    public static void postAll(String url,String param,Response.Listener listener, Response.ErrorListener errorListener){
        url = url+initLocalStrParams()+param;
        System.out.println("url:"+url);
        post(url, retryPolicyDefault, listener, errorListener);
    }

    public static void post_checkLogin(String url,String param,ResponseListener listener){
        url = url+initLocalStrParams()+param;
        System.out.println("url:"+url);
        post(url, retryPolicyDefault, listener, listener);
    }




    public static Map initHashMap(String url, Map<String, String> param) {
        if (param == null) {
            System.out.println("param = null");
            param = new HashMap<String, String>(6);
            param.put("scid","1");
            param.put("osid","3");
        }else{
            param.put("scid","1");
            param.put("osid","3");
            param.put("Anonymous", SPUtils.get(MyApplication.getContext(), "Anonymous", "").toString());
            param.put("AnonymUserId", SPUtils.get(MyApplication.getContext(), "AnonymUserId", "").toString());
            param.put("Production", SPUtils.get(MyApplication.getContext(), "Production", "").toString());
            System.out.println("param no null");
        }
        return param;
    }



    public static void getKLineData(String param,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://api.finance.ifeng.com/akdaily/?";
        String params = "&code="+param+"&type=last";
        postAll(url, params, listener, errorListener);
//        JsonObjectRequest kLineRequest = new JsonObjectRequest(Request.Method.POST,url,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(kLineRequest);
    }

    public static void getKWeekLine(String param,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://api.finance.ifeng.com/akweekly/?";
        String params = "&code="+param+"&type=last";
        postAll(url, params, listener, errorListener);
//        JsonObjectRequest weekLineRequest = new JsonObjectRequest(Request.Method.POST,url,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(weekLineRequest);
    }

    public static  void getKMonthLine(String param,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://api.finance.ifeng.com/akmonthly/?";
        String params = "&code="+param+"&type=last";
        postAll(url, params, listener, errorListener);
//        JsonObjectRequest monthLineRequest = new JsonObjectRequest(Request.Method.POST,url,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(monthLineRequest);
    }

    public static void getStockList(String stock,String curr_page,String date,String predict_type,String page_size,
                                    Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {
        String url = "http://deep.money/api/predict_day.ashx?";
        String params = "&stock=" + stock + "&cur_page=" + curr_page + "&day=" + date + "&predict_type=" + predict_type + "&page_size=" + page_size;
        postAll(url, params, listener, errorListener);
//        System.out.println("url+++++++++:"+url+params);
//        JsonObjectRequest stockRequest = new JsonObjectRequest(Request.Method.POST,url+params,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(stockRequest);
    }

    public static void getNewStockList(String botid,String return_count,String day,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/predict/rank?";
        String params = "&bot_id="+botid+"&return_count="+return_count+"&day="+day;
        postAll(url, params, listener, errorListener);
//        System.out.println("getNewStockList url:"+url);
//        JsonObjectRequest stockListRequest = new JsonObjectRequest(Request.Method.POST,url,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(stockListRequest);
    }


    public static void searchStock(String key,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/search?";
        String str_encode = "";
        try {
            str_encode = URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String params = "&key="+str_encode;
        postAll(url, params, listener, errorListener);
//        System.out.println("search url:"+url+"key="+ str_encode);
//        JsonObjectRequest searchStockRequest = new JsonObjectRequest(Request.Method.POST, url+ ,null,listener,errorListener);
//        MyApplication.getRequestQueue().add(searchStockRequest);
    }

    //http://deepmoney.chinacloudsites.cn//api/stock/predict/specday?day=20160907&stocks=sh603188&country=1

//    http://deepmoney.chinacloudsites.cn/api/stock/predict/rankall?&limit_count=3&day=20160913



    public static void sendPhoneCode(String phone,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/login/sendphonecode?";
        String params = "&Phone="+phone;
        postAll(url, params, listener, errorListener);
    }

    public static  void loginbyphone(String phone,String code,Response.Listener listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/login/loginbyphone?";
        String params = "&Phone="+phone+"&code="+code;
        postAll(url,params,listener,errorListener);
    }

    public static void logout(Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/login/logout?";
        postAll(url,"",listener,errorListener);
    }

    public static Map<String,String> initlocalParams(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("scid","1");
        map.put("osid","3");
        map.put("Anonymous", SPUtils.get(MyApplication.getContext(), "Anonymous", "").toString());
        map.put("AnonymUserId", SPUtils.get(MyApplication.getContext(), "AnonymUserId", "").toString());
        map.put("Production", SPUtils.get(MyApplication.getContext(), "Production", "").toString());
        return map;
    }

    public static String initLocalStrParams(){
        String Anonymous = SPUtils.get(MyApplication.getContext(), "Anonymous", "").toString();
        String AnonymUserId =  SPUtils.get(MyApplication.getContext(), "AnonymUserId", "").toString();
        String Production = SPUtils.get(MyApplication.getContext(), "Production", "").toString();
        String str = "osid=4&scid=1"+"&Anonymous="+Anonymous+"&AnonymUserId="+AnonymUserId+"&Production="+Production;
        return str;
    }



    public static boolean responseState(JSONObject s){
        boolean state = false;
        String state_code = s.optString("Status");
        String state_msg = s.optString("ReturnCode");
        if(!state_code.equals("") && !state_msg.equals("")){
            if(state_code.equals("1") || state_msg.equals("执行成功")){
                state = true;
            }
        }
        return state;
    }

    //HomeFragment需要的接口
    public static void launchInfo(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/launch?";
//        postAll(url, "", listener, errorListener);
        String params=initLocalStrParams();
        System.out.println("url+params:" + url + params);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET,url+params,null,listener,errorListener);
        postRequest.setTag(REQUEST_TAG);
        MyApplication.getRequestQueue().add(postRequest);
    }

    public static void check(Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/login/check?";
        postAll(url, "", listener, errorListener);
    }

//    public static void rankAll(String limit_count,String day,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
//        String url = "http://deepmoney.chinacloudsites.cn/api/stock/predict/rankall?";
//        String params = "&limit_count="+limit_count+"&day="+day;
//        postAll(url,params,listener,errorListener);
//    }

    public static void rankAll(String limit_count,String day,String sort_by,ResponseListener listener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/predict/rankall?";
        String params = "&limit_count="+limit_count+"&day="+day+"&sort_by="+sort_by;
        post_checkLogin(url,params,listener);
    }

    public static void getInfo(String param,Response.Listener<String> listener,Response.ErrorListener errorListener){
        String url = MyApplication.getContext().getString(R.string.url) + param;
        System.out.println("getInfo url:"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,listener,errorListener);
        stringRequest.setTag(REQUEST_TAG);
        MyApplication.getRequestQueue().add(stringRequest);
    }

    public static void emudealerList(String return_count,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/emudealer/list?";
        String params = "&return_count="+return_count;
        postAll(url, params, listener, errorListener);
    }

    public static void emudealerList(String return_count,ResponseListener listener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/emudealer/list?";
        String params = "&return_count="+return_count;
        post_checkLogin(url,params,listener);
    }

    //CrycFragment需要的接口
    public static void stockSpecDay(String stocks,String country,String day,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn//api/stock/predict/specday?";
        String params = "&stocks="+stocks+"&country="+country+"&day="+day;
        postAll(url, params, listener, errorListener);
    }

    //CaseDetailFragment需要的接口
    public static void emudealer(String id,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/emudealer/id/"+id+"?";
        postAll(url, "", listener, errorListener);
    }

    public static void emudealLog(String stock_bot_id,String currentPage,String pageSize,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/stock/emudeal/log?";
        String params = "&stock_bot_id="+stock_bot_id+"&currentPage="+currentPage+"&pageSize="+pageSize;
        postAll(url, params, listener, errorListener);
    }

//    public static void emudealLog(String stock_bot_id,String currentPage,String pageSize,ResponseListener listener){
//        String url = "http://deepmoney.chinacloudsites.cn/api/stock/emudeal/log?";
//        String params = "&stock_bot_id="+stock_bot_id+"&currentPage="+currentPage+"&pageSize="+pageSize;
//        post_checkLogin(url,params,listener);
//    }

    //http://deepmoney.chinacloudsites.cn/api/app/agreement
    public static void agreement(Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/app/agreement?";
        postAll(url,"",listener,errorListener);
    }
    //http://deepmoney.chinacloudsites.cn/api/app/about
    public static void about(Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        String url = "http://deepmoney.chinacloudsites.cn/api/app/about?";
        postAll(url,"",listener,errorListener);
    }

    //http://deepmoney.chinacloudsites.cn/api/app/help?id=1
    public static void help(String id,ResponseListener listener){
        String url = "http://deepmoney.chinacloudsites.cn/api/app/help?";
        String params = "&id="+id;
        post_checkLogin(url, params, listener);
    }

//    http://webstock.quote.hermes.hexun.com/a/minute?code=szse000563&start=20160830000000&number=600
    public static void minuteRequest(String code,String typeStr,String start,String num, Response.Listener<String> listener,Response.ErrorListener errorListener){
        String url = "http://webstock.quote.hermes.hexun.com/a/minute?";
        String params = "code="+typeStr+code+"&start="+start+"&number="+num;
        System.out.println("url+params:"+url+params);
        MyStringRequest stringRequest = new MyStringRequest(url+params,listener,errorListener);
        stringRequest.setTag(REQUEST_TAG);
        MyApplication.getRequestQueue().add(stringRequest);
    }

    public static String getBannerUrl(){
        String url = "http://deepmoneyweb.chinacloudsites.cn?";
        url = url+initLocalStrParams();
        return url;
    }

}
