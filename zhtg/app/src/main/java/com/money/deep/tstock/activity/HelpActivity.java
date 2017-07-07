package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.money.deep.tstock.R;
import com.money.deep.tstock.util.ActivityStackControlUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/19.
 */
public class HelpActivity extends Activity {
    @Bind(R.id.left_btn_view)
    LinearLayout leftBtnView;
    @Bind(R.id.suggestion_view)
    EditText suggestionView;
    @Bind(R.id.contact_view)
    EditText contactView;
    @Bind(R.id.submit_btn)
    TextView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_btn_view, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn_view:
                finish();
                break;
            case R.id.submit_btn:
                break;
        }
    }
}
