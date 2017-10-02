package com.art.huakai.artshow.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

public class WebActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ProgressBar pbWebLoading;
    private WebView mWebView;
    private String url;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra("url");
        if (url == null || url.isEmpty()) {
            url = "http://139.224.47.213:8080/showonline_api/swagger-ui.html#!/2015425165316162138230456208512550921475/offlineUsingPOST_15";
        }
    }

    @Override
    public void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        pbWebLoading = (ProgressBar) findViewById(R.id.pb_web_loading);
        mWebView = (WebView) findViewById(R.id.download_webview);

        findViewById(R.id.lly_back).setOnClickListener(this);
    }

    @Override
    public void setView() {
        initWebView();
    }


    private void initWebView() {
        WebSettings mWebSettings = mWebView.getSettings();
        // 设置 缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setAppCacheEnabled(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDisplayZoomControls(false);// 去掉缩放按钮
        mWebSettings.setBuiltInZoomControls(true);// 允许缩放
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setDefaultTextEncodingName("UTF-8");

        // 去掉滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setKeepScreenOn(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pbWebLoading.setVisibility(View.GONE);
                } else {
                    pbWebLoading.setVisibility(View.VISIBLE);
                    pbWebLoading.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    tvTitle.setText(title);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
        }
    }
}
