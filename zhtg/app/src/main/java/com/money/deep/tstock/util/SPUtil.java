package com.money.deep.tstock.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fengxg on 2016/9/8.
 */
public class SPUtil {
    public static final String DATABASE = "Database";
    public static final String RESULT = "result";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SPUtil(Context context){
        sp = context.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void putString(String string){
        editor.putString(RESULT,string);
        editor.commit();
    }

    public String getString(){
        String string = sp.getString(RESULT,"12");
        return string;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }


}
