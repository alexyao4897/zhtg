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
public class KnowActivity extends Activity {
    @Bind(R.id.user_center_icon_view)
    LinearLayout userCenterIconView;
    @Bind(R.id.about_tv)
    TextView aboutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        DataManager.about(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String about_str = response.optString("TEXT");
                if(about_str!=null){
                    aboutTv.setText(about_str);
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
