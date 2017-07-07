package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.money.deep.tstock.R;
import com.money.deep.tstock.adapter.GuidePageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fengxg on 2016/9/27.
 */
public class GuideActivity extends Activity {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private List<View> views;
    private GuidePageAdapter guidePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.what_new_1, null));
        views.add(inflater.inflate(R.layout.what_new_2, null));
        views.add(inflater.inflate(R.layout.what_new_3, null));

        guidePageAdapter = new GuidePageAdapter(views, this);
        viewpager.setAdapter(guidePageAdapter);
    }
}
