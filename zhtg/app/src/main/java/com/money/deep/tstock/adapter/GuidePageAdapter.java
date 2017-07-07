package com.money.deep.tstock.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.money.deep.tstock.R;
import com.money.deep.tstock.activity.MainActivity;
import com.money.deep.tstock.util.SPUtils;

import java.util.List;

/**
 * Created by fengxg on 2016/9/27.
 */
public class GuidePageAdapter extends PagerAdapter{
    private List<View> views;
    private Activity activity;
    public GuidePageAdapter(List<View> views,Activity activity){
        this.views = views;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        if(views != null){
            return views.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager)container).addView(views.get(position),0);
        if(position == views.size() - 1){
            views.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtils.put(activity,"iFirstInstall","yes");
                    activity.startActivity(new Intent(activity,MainActivity.class));
                    activity.finish();
                }
            });
        }
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }
}
