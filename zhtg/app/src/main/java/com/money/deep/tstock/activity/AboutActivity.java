package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/28.
 */
public class AboutActivity extends Activity {
    @Bind(R.id.version_text)
    TextView versionText;
    @Bind(R.id.user_center_icon_view)
    LinearLayout userCenterIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new);
        ButterKnife.bind(this);
        versionText.setText("版本号V" + MyApplication.getVersionName(this));
    }

    @OnClick(R.id.user_center_icon_view)
    public void onClick() {
        finish();
    }
}
