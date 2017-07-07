package com.money.deep.tstock.util;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity 堆栈控制
 * <p/>
 * <p>detailed comment
 *
 * @author yuantao.wu 2015-3-4
 * @see
 * @since 1.0
 */
public class ActivityStackControlUtil {
    private static List<Activity> activityList = new ArrayList<Activity>();

    private static String TAG = ActivityStackControlUtil.class.getSimpleName();

    public static void remove(Activity activity) {

        activityList.remove(activity);

    }

    public static void add(Activity activity) {

        activityList.add(activity);

    }

    public static void exitApp() {

        for (Activity activity : activityList) {
            Log.i(TAG, "exitApp()执行了");
            System.out.println(activity.getLocalClassName());
            activity.finish();

        }
        activityList.clear();
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
