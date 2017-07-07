package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.money.deep.tstock.R;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.util.ActivityStackControlUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/21.
 */
public class AgreeActivity extends Activity {
    @Bind(R.id.tiaokuan_tv)
    TextView tiaokuanTv;
    @Bind(R.id.user_center_icon_view)
    LinearLayout userCenterIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        ActivityStackControlUtil.add(this);
        initData();
        ButterKnife.bind(this);
    }

    public void initData() {
        DataManager.agreement(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String tiaokuan_str = response.optString("TEXT");
                System.out.println("tiaokuan_str:" + tiaokuan_str);
                if (tiaokuan_str != null) {
                    tiaokuanTv.setText(tiaokuan_str);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @OnClick(R.id.user_center_icon_view)
    public void onClick() {
        finish();
    }
}
