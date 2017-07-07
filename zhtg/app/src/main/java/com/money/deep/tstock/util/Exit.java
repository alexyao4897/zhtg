package com.money.deep.tstock.util;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 退出
 *
 * @author wuyt3
 *         CUCWooVillage
 *         com.ailk.cucwoovillage.utils
 *         2014-5-13	下午3:20:06
 */
public class Exit {
    private boolean isExit = false;

    private Runnable task = new Runnable() {

        public void run() {

            isExit = false;
        }

    };

    public void doExitInOneSecond() {

        isExit = true;

        HandlerThread thread = new HandlerThread("doTask");

        thread.start();

        new Handler(thread.getLooper()).postDelayed(task, 1000);
    }

    public boolean isExit() {

        return isExit;

    }

    public void setExit(boolean isExit) {

        this.isExit = isExit;

    }

}
