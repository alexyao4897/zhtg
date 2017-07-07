package com.money.deep.tstock.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.money.deep.tstock.R;
import com.money.deep.tstock.activity.LoginActivity;
//import com.example.test.mpchartapplication.alpay.AlixId;
//import com.example.test.mpchartapplication.alpay.BaseHelper;
//import com.example.test.mpchartapplication.alpay.MobileSecurePayer;
//import com.example.test.mpchartapplication.alpay.ResultChecker;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.model.StatusInfo;
import com.money.deep.tstock.model.UserEncrypted;
import com.money.deep.tstock.pay.PayResult;
import com.money.deep.tstock.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URLDecoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/10.
 */
public class UserCenterFragment extends Fragment {
    @Bind(R.id.login_btn)
    TextView loginBtn;
    @Bind(R.id.logout_btn)
    TextView logoutBtn;

    private static UserCenterFragment INSTANCE;
    @Bind(R.id.pay_btn)
    TextView payBtn;
    private ProgressDialog mProgress = null;
    private static final int SDK_PAY_FLAG = 1;
//    String orderInfo = "partner=\"2088101076512489\"&seller=\"evan@byban.net\"&out_trade_no=\"45530\"&subject=\"停车缴费\"&body=\"停车缴费\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fapi.motouching.com%2Fnotify%2Faliclient\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"60m\"&sign=\"RMM6T9itgpiTkmvCWBfuiGplf6OJeQVlHGL7e0g5GZrBjpMdhQCh3SlUbIs0YBopXkQO2JylATcQdZCEClwzLeEMGTXb1OED%2B4fDEg80aWtiSt%2BU6uC1w3rHNXoLYdopFbFmVe5Kpx9opa7tq64ecBNiIC%2BTk%2B%2B4NucBNeInmcQ%3D\"&sign_type=\"RSA\"";
    String orderInfo = "partner=\"2088101076512489\"&seller=\"evan@byban.net\"&out_trade_no=\"46003\"&subject=\"潮流时尚钢带手镯手表\"&body=\"潮流时尚钢带手镯手表\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fapi.motouching.com%2Fnotify%2Faliclient\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"60m\"&sign=\"TX8vJXZWSl8xpvD9WEHB2xcId%2FQ0C4b7gdfQQCGVzyEy0R%2FxUDQSVcJ6F4cM7G8dYHse5XUQIEKfsOFNu3oyLvwSXiDjYbwDYiiDGf9PyKp0ucRhvPV8w%2BlLYHZwSRh%2FSZDY3x7nnyurqNZh%2F5Ow1Yd2uMGI92Heb8rrRvMBBWk%3D\"&sign_type=\"RSA\"";
    public UserCenterFragment() {
        super();
    }

    public static UserCenterFragment getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new UserCenterFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.login_btn, R.id.logout_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Login();
                break;
            case R.id.logout_btn:
                logout();
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
                if (statusInfo.getReturnCode().equals("注销成功") && statusInfo.getStatus().equals("1")) {
                    Toast.makeText(getActivity(), statusInfo.getReturnCode(), Toast.LENGTH_SHORT).show();
                    SPUtils.put(getActivity(), "Anonymous", "");
                    SPUtils.put(getActivity(), "AnonymUserId", "");
                    SPUtils.put(getActivity(), "Production", "");
                } else {
                    Toast.makeText(getActivity(), statusInfo.getReturnCode(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void Login() {

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
                SPUtils.put(getActivity(), "Anonymous", decode_str);
                SPUtils.put(getActivity(), "AnonymUserId", userEncrypted.getAnonymUserId());
                SPUtils.put(getActivity(), "Production", userEncrypted.getProduction());
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @OnClick(R.id.pay_btn)
    public void onClick() {
//        getSDKVersion();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void getSDKVersion() {
        PayTask payTask = new PayTask(getActivity());
        String version = payTask.getVersion();
        Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }



}
