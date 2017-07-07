package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.money.deep.tstock.R;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.http.ResponseListener;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/27.
 */
public class AdviceActivity extends Activity {
    @Bind(R.id.left_btn_view)
    LinearLayout leftBtnView;
    @Bind(R.id.advice_tv)
    TextView adviceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        ButterKnife.bind(this);
        initData("1");

    }

    public void initData(String id){
        DataManager.help(id, new ResponseListener("/help") {
            @Override
            public void onSuccess(JSONObject s) {
                String text = s.optString("TEXT");
                adviceTv.setText(text);
            }

            @Override
            public void onError(JSONObject s) {

            }
        });
    }

    @OnClick(R.id.left_btn_view)
    public void onClick() {
        finish();
    }
}
