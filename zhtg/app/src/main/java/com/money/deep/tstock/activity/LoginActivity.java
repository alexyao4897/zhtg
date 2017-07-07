package com.money.deep.tstock.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.model.StatusInfo;
import com.money.deep.tstock.model.UserEncrypted;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/10.
 */
public class LoginActivity extends Activity {
    @Bind(R.id.login_left_iv)
    ImageView loginLeftIv;
    @Bind(R.id.phone_editview)
    EditText phoneEditview;
    @Bind(R.id.getcode_tv)
    TextView getcodeTv;
    @Bind(R.id.verifycode_editview)
    EditText verifycodeEditview;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.login_btn)
    TextView loginBtn;
    @Bind(R.id.login_left_view)
    RelativeLayout loginLeftView;
    @Bind(R.id.phone_iv)
    ImageView phoneIv;
    @Bind(R.id.verifycode_iv)
    ImageView verifycodeIv;
    @Bind(R.id.agree_tv)
    TextView agreeTv;

    private TimeCount time;
    private String verifycode = "026616";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        MyApplication.login_stauts = false;
        SpannableStringBuilder style = new SpannableStringBuilder("点击登录将自动注册，且表示你同意智能投顾服务协议");
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, style.length()-8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.emudeal_name_bottom_linecolor)), style.length()-8, style.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreeTv.setText(style);
//        lauchInfo();
        time = new TimeCount(60000, 1000);
    }

//    private FragmentListener.LoginToMainListener fragmentListener = null;
//    public void setToMainListener(FragmentListener.LoginToMainListener fragmentListener){
//        this.fragmentListener = fragmentListener;
//    }

    @OnClick({R.id.login_left_iv, R.id.getcode_tv, R.id.login_btn, R.id.login_left_view, R.id.phone_editview,R.id.agree_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_left_iv:
            case R.id.login_left_view:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.getcode_tv:
                String phone_str = phoneEditview.getText().toString();
                if (phone_str != null && !phone_str.equals("") && isMobileNO(phone_str)) {
                    time.start();
                    getPhoneCode(phone_str);

                } else if (phone_str.equals("")) {
                    Toast.makeText(this, "请输入你的手机号", Toast.LENGTH_SHORT).show();
                } else if (!isMobileNO(phone_str)) {
                    Toast.makeText(this, "输入的手机号格式不正确", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.phone_editview:
                getcodeTv.setBackgroundResource(R.drawable.red_loginbtn_rect);
                break;
            case R.id.login_btn:
                String phone_string = phoneEditview.getText().toString();
                String code_string = verifycodeEditview.getText().toString();
                if (phone_string != null && !phone_string.equals("") && isMobileNO(phone_string)) {
                    if (code_string != null && !code_string.equals("")) {
                        loginByPhone(phone_string, code_string);
                    } else {
                        Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    }
                } else if (phone_string.equals("")) {
                    Toast.makeText(this, "请输入你的手机号", Toast.LENGTH_SHORT).show();
                } else if (!isMobileNO(phone_string)) {
                    Toast.makeText(this, "输入的手机号格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.agree_tv:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,AgreeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getPhoneCode(String phone) {
        DataManager.sendPhoneCode(phone, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("getCode response:" + response);
//                verifycode =
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void loginByPhone(String phone, String code) {
        DataManager.loginbyphone(phone, code, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                StatusInfo statusInfo = new Gson().fromJson(response.optString("StatusInfo"),
                        new TypeToken<StatusInfo>() {
                        }.getType());
                UserEncrypted userEncrypted = new Gson().fromJson(response.optString("UserEncrypted"),
                        new TypeToken<UserEncrypted>() {
                        }.getType());
                String NickName = response.optString("NickName");
                if(NickName!=null){
                    SPUtils.put(LoginActivity.this, "NickName",NickName);
                }
                String status_code = statusInfo.getStatusCode();
                if(status_code.equals("0")){
                    if (userEncrypted != null) {
                        String decode_str = "";
                        String production_decode = "";
                        try {
                            decode_str = URLDecoder.decode(userEncrypted.getAnonymous(), "UTF-8");
                            production_decode = URLDecoder.decode(userEncrypted.getProduction(), "UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SPUtils.put(LoginActivity.this, "Anonymous", decode_str);
                        if (userEncrypted.getAnonymUserId() != null&&!userEncrypted.getAnonymUserId().equals("")) {
                            SPUtils.put(LoginActivity.this, "AnonymUserId", userEncrypted.getAnonymUserId());
                        }
                        if(!production_decode.equals("")){
                            MyApplication.login_stauts = true;
                            SPUtils.put(LoginActivity.this, "Production", production_decode);
//                            Intent intent = new Intent();
//                            intent.putExtra("id", 1);
//                            intent.setClass(LoginActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }

                    }
                }


                DataManager.check(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        StatusInfo statusInfo = new Gson().fromJson(response.optString("StatusInfo"),
                                new TypeToken<StatusInfo>() {
                                }.getType());
                        if (statusInfo.getReturnCode().equals("已登录状态")) {
//                            Toast.makeText(LoginActivity.this, statusInfo.getReturnCode(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("id", 1);
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            MyApplication.login_stauts = true;
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @OnClick(R.id.agree_tv)
    public void onClick() {
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getcodeTv.setText("获取验证码");
            getcodeTv.setBackgroundResource(R.drawable.red_loginbtn_rect);
            getcodeTv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getcodeTv.setClickable(false);//防止重复点击
            getcodeTv.setBackgroundResource(R.drawable.gray_loginbtn_rect);
            getcodeTv.setText("(" + millisUntilFinished / 1000 + "s)" + "重新获取");
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private void lauchInfo() {

        DataManager.launchInfo(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserEncrypted userEncrypted = new Gson().fromJson(response.optString("UserEncrypted"),
                        new TypeToken<UserEncrypted>() {
                        }.getType());
                String decode_str = "";
                try {
                    decode_str = URLDecoder.decode(userEncrypted.getAnonymous(), "UTF-8");
                    System.out.println("decode:" + decode_str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SPUtils.put(LoginActivity.this, "Anonymous", decode_str);
                SPUtils.put(LoginActivity.this, "AnonymUserId", userEncrypted.getAnonymUserId());
                SPUtils.put(LoginActivity.this, "Production", userEncrypted.getProduction());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
