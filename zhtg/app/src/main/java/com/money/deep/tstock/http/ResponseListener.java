package com.money.deep.tstock.http;

import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.money.deep.tstock.activity.LoginActivity;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.model.StatusInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.money.deep.tstock.util.SPUtils;

import org.json.JSONObject;


/**
 * Created by Administrator on 2016/9/20.
 */
public abstract class ResponseListener implements Response.Listener<JSONObject>,Response.ErrorListener{
    private String mRequestName;
    private long beginTime = 0;
    public ResponseListener(String requestName){
        beginTime = System.currentTimeMillis();
        if(requestName != null){
            mRequestName = requestName+":";
        }else{
            mRequestName = "";
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("请求时间",mRequestName+"请求耗时为"+(System.currentTimeMillis()-beginTime)+"ms");
        if(error instanceof NoConnectionError){
            Log.i("Error", mRequestName + "没有网络连接" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }else if(error instanceof ServerError){
            Log.i("Error", mRequestName + "服务器错误" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }else if(error instanceof ParseError){
            Log.i("Error",mRequestName + "服务器返回数据格式出错");
        }else if(error instanceof TimeoutError){
            Log.i("Error",mRequestName + "请求超时" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }else if(error instanceof NetworkError){
            Log.i("Error",mRequestName + "网络出错" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }else if(error instanceof AuthFailureError){
            Log.i("Error",mRequestName + "安全验证出错" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }else{
            Log.i("Error",mRequestName + "发生其他错误" + ";Messsage:" + error.getMessage() + ";Cause:" + error.getCause());
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        StatusInfo statusInfo = new Gson().fromJson(response.optString("StatusInfo"),
                new TypeToken<StatusInfo>(){}.getType());
        String status = statusInfo.getStatus();
        String return_code = statusInfo.getReturnCode();
        String status_code = statusInfo.getStatusCode();
        if(VolleyAquire.STATE_CODE_SUCCESS.equals(status_code)){
            onSuccess(response);
        }else if(VolleyAquire.STATE_CODE_NEEDLOGIN.equals(status_code)){

            Intent intent = new Intent();
            intent.setClass(MyApplication.mRunActivity, LoginActivity.class);
//            MyApplication.mRunActivity.startActivity(intent);
            MyApplication.mRunActivity.startActivityForResult(intent,0);
        }else{
            onError(response);
        }
    }

    public abstract void onSuccess(JSONObject s);
    public abstract void onError(JSONObject s);
}
