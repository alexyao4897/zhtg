package com.money.deep.tstock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.money.deep.tstock.R;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SplashActivity extends Activity {
    @Bind(R.id.rl_logo)
    RelativeLayout rlLogo;
    private static final long WAIT_TIME=2*1000;//启动页面时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        loadAnimation();
    }

    private void loadAnimation(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splash_in);
        anim.setDuration(WAIT_TIME);
        rlLogo.startAnimation(anim);
        mhandler.removeMessages(-1);
        mhandler.sendEmptyMessageDelayed(-1,WAIT_TIME);
    }
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String iFirstInstall = SPUtils.get(SplashActivity.this,"iFirstInstall","").toString();
            if(iFirstInstall.equals("yes")){
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,GuideActivity.class);
                startActivity(intent);
                finish();
            }

        }
    };

}
