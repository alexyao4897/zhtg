package com.money.deep.tstock.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ScreenUtils {
    private static int screenWidth;
    private static int screenHeight;
    private static float density;
    public ScreenUtils(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
    }

    public static float getDensity(){
        return density;
    }
}
