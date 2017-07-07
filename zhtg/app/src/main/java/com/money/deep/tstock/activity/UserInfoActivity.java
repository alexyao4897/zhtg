package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.money.deep.tstock.R;
import com.money.deep.tstock.util.ActivityStackControlUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/19.
 */
public class UserInfoActivity extends Activity {
    @Bind(R.id.left_btn_view)
    LinearLayout leftBtnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.left_btn_view)
    public void onClick() {
        finish();
    }
}
