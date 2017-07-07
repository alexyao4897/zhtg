package com.money.deep.tstock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.component.RoundImageView;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.model.StatusInfo;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.SPUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/19.
 */
public class UserCenterActivity extends Activity {
    @Bind(R.id.avatar_img)
    RoundImageView avatarImg;
    @Bind(R.id.user_name_tv)
    TextView userNameTv;
    @Bind(R.id.quick_view)
    RelativeLayout quickView;
    //    @Bind(R.id.service_view)
//    RelativeLayout serviceView;
    @Bind(R.id.help_view)
    RelativeLayout helpView;
    @Bind(R.id.logout_btn)
    TextView logoutBtn;
    @Bind(R.id.left_btn_view)
    LinearLayout leftBtnView;
    @Bind(R.id.quick_iv)
    ImageView quickIv;
    //    @Bind(R.id.service_iv)
//    ImageView serviceIv;
//    @Bind(R.id.push_iv)
//    ImageView pushIv;
//    @Bind(R.id.push_view)
//    RelativeLayout pushView;
    @Bind(R.id.help_iv)
    ImageView helpIv;
    @Bind(R.id.about_iv)
    ImageView aboutIv;
    @Bind(R.id.about_view)
    RelativeLayout aboutView;
    @Bind(R.id.left_btn)
    ImageView leftBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        String nickName = SPUtils.get(UserCenterActivity.this, "NickName", "").toString();
        userNameTv.setText(nickName);
    }

    @OnClick({R.id.avatar_img, R.id.help_view, R.id.left_btn_view, R.id.logout_btn, R.id.about_view, R.id.quick_view,R.id.left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar_img:
                Intent userinfo_intent = new Intent();
                userinfo_intent.setClass(this, UserInfoActivity.class);
                startActivity(userinfo_intent);
                break;
            case R.id.help_view:
                Intent intent = new Intent();
                intent.setClass(this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.quick_view:
                Intent intent_help = new Intent();
                intent_help.setClass(this, KnowActivity.class);
                startActivity(intent_help);
                break;
            case R.id.left_btn_view:
            case R.id.left_btn:
                finish();
                break;
            case R.id.logout_btn:
                logout();
                break;
            case R.id.about_view:
                Intent intent_about = new Intent();
                intent_about.setClass(UserCenterActivity.this, AboutActivity.class);
                startActivity(intent_about);
                break;
        }
    }

    private void logout() {
        DataManager.logout(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                StatusInfo statusInfo = new Gson().fromJson(response.optString("StatusInfo"),
                        new TypeToken<StatusInfo>() {
                        }.getType());
                if (statusInfo.getStatusCode().equals("0") && statusInfo.getStatus().equals("1")) {
                    Toast.makeText(UserCenterActivity.this, statusInfo.getReturnCode(), Toast.LENGTH_SHORT).show();
                    SPUtils.put(UserCenterActivity.this, "Production", "");
                    MyApplication.login_stauts = false;
                    Intent intent = new Intent();
                    intent.setClass(UserCenterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserCenterActivity.this, statusInfo.getReturnCode(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @OnClick(R.id.about_view)
    public void onClick() {
    }

}
