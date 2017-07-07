package com.money.deep.tstock.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.fragment.HomeFragment;
import com.money.deep.tstock.listener.FragmentListener;
import com.money.deep.tstock.R;
import com.money.deep.tstock.fragment.CaseDetailFragment;
import com.money.deep.tstock.fragment.CrycFragment;
import com.money.deep.tstock.fragment.GmFragment;
import com.money.deep.tstock.fragment.UserCenterFragment;
import com.money.deep.tstock.fragment.SpcyFragment;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.Exit;
import com.money.deep.tstock.util.SPUtils;


public class MainActivity extends FragmentActivity {

    private LinearLayout spcy_linear,rcyc_linear,gm_linear,login_linear;
    private ImageView spcy_img,rcyc_img,gm_img,login_img;
    private TextView spcy_tv,rcyc_tv,gm_tv,login_tv;
    FragmentManager fm = null;
    FragmentTransaction  ft = null;
    private Fragment mCurFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStackControlUtil.add(this);
        init();
        initView();
        initEvent();
        initData();
        setSelect(0);

    }

    private void init() {
        fm = this.getSupportFragmentManager();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        SPUtils.put(this,"density",density);
    }

    private void setSelect(int i){
        ft = fm.beginTransaction();
        if (null != mCurFragment) {
            ft.remove(mCurFragment);
            mCurFragment = null;
        }
        switch (i){
            case 0:
                login_linear.setClickable(false);
                spcy_linear.setClickable(true);
                rcyc_linear.setClickable(true);
                gm_linear.setClickable(true);
                mCurFragment = HomeFragment.getInstance();
                ft.add(R.id.fl_content, mCurFragment);
                ((HomeFragment)mCurFragment).setHomeToPredictListener(mHomeToPredictListener);
                ((HomeFragment)mCurFragment).setHomeToNextDayListener(homeToNextDayListener);
                ((HomeFragment)mCurFragment).setHomeToCaseViewListener(homeToCaseViewListener);
                ft.show(mCurFragment);
                break;
            case 1:
                spcy_linear.setClickable(false);
                login_linear.setClickable(true);
                rcyc_linear.setClickable(true);
                gm_linear.setClickable(true);
                mCurFragment = SpcyFragment.getInstace();
                ft.add(R.id.fl_content,mCurFragment);
                ft.show(mCurFragment);
                break;
            case 2:
                rcyc_linear.setClickable(false);
                spcy_linear.setClickable(true);
                login_linear.setClickable(true);
                gm_linear.setClickable(true);
                Fragment tmp = fm.findFragmentByTag("CrycFragment");
                if (null == tmp) {
                    mCurFragment = CrycFragment.getInstance();
                    ft.add(R.id.fl_content,mCurFragment, "CrycFragment");
                }
                ft.show(mCurFragment);
                break;
            case 3:
                gm_linear.setClickable(false);
                rcyc_linear.setClickable(true);
                spcy_linear.setClickable(true);
                login_linear.setClickable(true);
                mCurFragment = GmFragment.getInstance();
                ft.add(R.id.fl_content, mCurFragment);
                ((GmFragment)mCurFragment).setGMListener(mGMListener);
                ft.show(mCurFragment);
                break;
        }
        System.out.println("mCurFragment:"+mCurFragment);
        ft.commitAllowingStateLoss();

    }


    private void initData(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                ft = fm.beginTransaction();
                mCurFragment = HomeFragment.getInstance();
                ft.remove(GmFragment.getInstance());
                ft.add(R.id.fl_content, mCurFragment);
                ft.show(mCurFragment);
                ft.commitAllowingStateLoss();
                login_img.setBackgroundResource(R.drawable.home_button_select);
                login_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                gm_img.setBackgroundResource(R.drawable.case_button);
                gm_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                login_linear.setClickable(false);
                gm_linear.setClickable(true);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.mRunActivity = this;
//        setSelect(0);
        System.out.println("activity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView(){
        spcy_linear = (LinearLayout)findViewById(R.id.spcy_btn);
        rcyc_linear = (LinearLayout)findViewById(R.id.cryc_btn);
        gm_linear = (LinearLayout)findViewById(R.id.gm_btn);
        login_linear = (LinearLayout)findViewById(R.id.login_btn);
        spcy_img = (ImageView)findViewById(R.id.spcy_img);
        rcyc_img = (ImageView)findViewById(R.id.cryc_img);
        gm_img = (ImageView)findViewById(R.id.gm_img);
        login_img = (ImageView)findViewById(R.id.login_img);
        spcy_tv = (TextView)findViewById(R.id.spcy_tv);
        rcyc_tv = (TextView)findViewById(R.id.cryc_tv);
        gm_tv = (TextView)findViewById(R.id.gm_tv);
        login_tv = (TextView)findViewById(R.id.login_tv);
    }

    private void initEvent(){

        login_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spcy_img.setBackgroundResource(R.drawable.spcy_button);
                rcyc_img.setBackgroundResource(R.drawable.cryc_button);
                gm_img.setBackgroundResource(R.drawable.case_button);
                spcy_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                rcyc_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                gm_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                login_img.setBackgroundResource(R.drawable.home_button_select);
                login_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                setSelect(0);
            }
        });

        spcy_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spcy_img.setBackgroundResource(R.drawable.spcy_button_select);
                rcyc_img.setBackgroundResource(R.drawable.cryc_button);
                gm_img.setBackgroundResource(R.drawable.case_button);
                spcy_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                rcyc_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                gm_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                login_img.setBackgroundResource(R.drawable.home_button);
                login_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                setSelect(1);
            }
        });

        rcyc_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spcy_img.setBackgroundResource(R.drawable.spcy_button);
                rcyc_img.setBackgroundResource(R.drawable.cryc_button_select);
                gm_img.setBackgroundResource(R.drawable.case_button);
                spcy_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                rcyc_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                gm_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                login_img.setBackgroundResource(R.drawable.home_button);
                login_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                setSelect(2);
            }
        });


        gm_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spcy_img.setBackgroundResource(R.drawable.spcy_button);
                rcyc_img.setBackgroundResource(R.drawable.cryc_button);
                gm_img.setBackgroundResource(R.drawable.case_button_select);
                spcy_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                rcyc_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                gm_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                login_img.setBackgroundResource(R.drawable.home_button);
                login_tv.setTextColor(getResources().getColor(R.color.bottom_text_color));
                setSelect(3);
            }
        });


    }

    ////////////////
    private FragmentListener.GMListener mGMListener = new FragmentListener.GMListener() {
        @Override
        public void onClickItem(int position) {
            ft = fm.beginTransaction();
            CaseDetailFragment caseDetailFragment = CaseDetailFragment.getInstance();
//            ft.add(R.id.fl_content, caseDetailFragment);
            GmFragment gmFragment = GmFragment.getInstance();
            mCurFragment = CaseDetailFragment.getInstance();
            ((CaseDetailFragment)mCurFragment).setCaseDetailListener(mCaseDetailListener);
            ft.remove(gmFragment);
            Bundle bundle = new Bundle();
            bundle.putString("position",position+"");
            caseDetailFragment.setArguments(bundle);
            ft.add(R.id.fl_content, caseDetailFragment, "CaseDetailFragment");
            ft.show(caseDetailFragment);
            ft.commitAllowingStateLoss();
        }
    };

    private FragmentListener.CaseDetailListener mCaseDetailListener = new FragmentListener.CaseDetailListener(){
        @Override
        public void onLeftBtnClick() {
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
            ft = fm.beginTransaction();
            mCurFragment = GmFragment.getInstance();
            ft.remove(CaseDetailFragment.getInstance());
//            gmFragment = GmFragment.getInstance();
            ft.add(R.id.fl_content, GmFragment.getInstance());
            ft.show(GmFragment.getInstance());
            ft.commitAllowingStateLoss();
        }
    };

    private FragmentListener.HomeToPredictListener mHomeToPredictListener = new FragmentListener.HomeToPredictListener(){
        @Override
        public void goPredictClick() {
            setSelect(2);
            spcy_img.setBackgroundResource(R.drawable.spcy_button);
            rcyc_img.setBackgroundResource(R.drawable.cryc_button_select);
            gm_img.setBackgroundResource(R.drawable.case_button);
            spcy_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            rcyc_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
            gm_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            login_img.setBackgroundResource(R.drawable.login_button);
            login_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
        }
    };

    private FragmentListener.HomeToNextDayListener homeToNextDayListener = new FragmentListener.HomeToNextDayListener() {
        @Override
        public void goNextDayView() {
            setSelect(1);
            spcy_img.setBackgroundResource(R.drawable.spcy_button_select);
            rcyc_img.setBackgroundResource(R.drawable.cryc_button);
            gm_img.setBackgroundResource(R.drawable.case_button);
            spcy_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
            rcyc_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            gm_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            login_img.setBackgroundResource(R.drawable.login_button);
            login_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));

        }
    };

    private FragmentListener.HomeToCaseViewListener homeToCaseViewListener = new FragmentListener.HomeToCaseViewListener() {
        @Override
        public void goCaseView() {
            setSelect(3);
            spcy_img.setBackgroundResource(R.drawable.spcy_button);
            rcyc_img.setBackgroundResource(R.drawable.cryc_button);
            gm_img.setBackgroundResource(R.drawable.case_button_select);
            spcy_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            rcyc_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
            gm_tv.setTextColor(getResources().getColor(R.color.subview_text_red_color));
            login_img.setBackgroundResource(R.drawable.login_button);
            login_tv.setTextColor(getResources().getColor(R.color.bottom_view_text_color));
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            pressAgainExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /******
     * 退出程序
     */
    Exit exit = new Exit();
    private void pressAgainExit() {
        if (exit.isExit()) {
            ActivityStackControlUtil.exitApp();
        } else {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exit.doExitInOneSecond();
        }
    }
}
