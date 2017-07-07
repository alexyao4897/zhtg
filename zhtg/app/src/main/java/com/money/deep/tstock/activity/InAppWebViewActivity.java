package com.money.deep.tstock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.money.deep.tstock.R;
import com.money.deep.tstock.http.DataManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/29.
 */
public class InAppWebViewActivity extends Activity {
    @Bind(R.id.user_center_icon_view)
    LinearLayout userCenterIconView;
    @Bind(R.id.wv_webview)
    WebView wvWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inappwebview);
        ButterKnife.bind(this);
        WebSettings webSettings = wvWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮


        //设置webview兼容手机屏幕
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(true); // 支持缩放

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 支持js
        webSettings.setJavaScriptEnabled(true);
        String url = DataManager.getBannerUrl();
        System.out.println("banner url:"+url);
        wvWebview.loadUrl(url);
        wvWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setSupportMultipleWindows(false);
    }

    @OnClick(R.id.user_center_icon_view)
    public void onClick() {
        finish();
    }
}
