package com.art.huakai.artshow.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.eventbus.TalentInfoChangeEvent;
import com.art.huakai.artshow.eventbus.TalentNotifyEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import org.apache.commons.lang3.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class ResumeHonourFragment extends BaseFragment {

    private static final int CODE_COMMIT = 10;

    @BindView(R.id.edt_introduce)
    EditText edtIntroduce;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.webview)
    WebView webView;

    private Unbinder unbinder;
    private ShowProgressDialog showProgressDialog;

    //变量表示  是否启用图片加载
    private boolean isLoadImage = false;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private String urlH5;
    private MyHandler myHandler;

    public ResumeHonourFragment() {
    }

    public static ResumeHonourFragment newInstance() {
        ResumeHonourFragment fragment = new ResumeHonourFragment();
        return fragment;
    }

    public static class MyHandler extends Handler {

        private WeakReference<ResumeHonourFragment> reference;

        public MyHandler(ResumeHonourFragment fragment) {
            reference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ResumeHonourFragment fragment = reference.get();
            if (fragment == null) return;
            switch (msg.what) {
                case CODE_COMMIT:
                    String richText = (String) msg.obj;
                    fragment.changeTalentHonor(richText);
                    break;
            }
        }
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        myHandler = new MyHandler(this);
        Map<String, String> params = new TreeMap<>();
        String id = TextUtils.isEmpty(TalentDetailInfo.getInstance().getId()) ? "" : TalentDetailInfo.getInstance().getId();
        params.put("id", id);
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        urlH5 = String.format(
                Constant.URL_TALENT_AWARDSDESCPT_WYSIWYG,
                id,
                LocalUserInfo.getInstance().getId(),
                LocalUserInfo.getInstance().getAccessToken(),
                sign
        );
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_resume_honor;
    }

    @Override
    public void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.resume_award_experience);
        tvSubtitle.setVisibility(View.VISIBLE);
        initWebViewSetting();
    }

    @Override
    public void setView() {

    }

    @OnClick(R.id.lly_back)
    public void back() {
        getActivity().finish();
    }

    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void confirmInfo() {
        webView.evaluateJavascript("javascript:getContent()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                String unescapeJava = StringEscapeUtils.unescapeJava(value);
                String scapeRichText = "";
                if (unescapeJava.startsWith("\"") && unescapeJava.startsWith("\"")) {
                    try {
                        scapeRichText = unescapeJava.substring(1, unescapeJava.length() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        scapeRichText = unescapeJava;
                    }
                } else {
                    scapeRichText = unescapeJava;
                }
                Message msg = myHandler.obtainMessage(CODE_COMMIT);
                msg.obj = scapeRichText;
                myHandler.sendMessage(msg);
            }
        });

    }

    /**
     * 修改简历个人介绍
     */
    public void changeTalentHonor(final String awardsDescpt) {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(getContext(), true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(getContext(), getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(awardsDescpt)) {
            Toast.makeText(getContext(), getString(R.string.tip_honor_input_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(TalentDetailInfo.getInstance().getId())) {
            params.put("id", TalentDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("awardsDescpt", awardsDescpt);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_EDIT_AWARDSDESCPT, params, 52, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.tip_honor_commit_suc));
                        JSONObject jsonObject = new JSONObject(obj);
                        String talentId = jsonObject.getString("id");
                        TalentDetailInfo.getInstance().setId(talentId);
                        TalentDetailInfo.getInstance().setAwardsDescpt(awardsDescpt);
                        EventBus.getDefault().post(new TalentInfoChangeEvent());
                        EventBus.getDefault().post(new TalentNotifyEvent(TalentNotifyEvent.NOTIFY_AWARD_DES));
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 设置webview相关属性
     */
    private void initWebViewSetting() {

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= 19) {//硬件加速器的使用
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        WebSettings webSettings = webView.getSettings();
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//允许弹出alert
        webSettings.setSupportZoom(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setBlockNetworkImage(true);
        isLoadImage = true;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //通过ssl
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.i(TAG, "onPageStarted" + null == url ? "" : url);
                // mProgressDialog.setVisibility(View.VISIBLE);
                if (!isLoadImage) {
                    webView.getSettings().setBlockNetworkImage(true);
                    isLoadImage = true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.i(TAG, "onPageFinished" + null == url ? "" : url);
                if (isLoadImage) {
                    webView.getSettings().setBlockNetworkImage(false);
                    isLoadImage = false;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                LogUtil.i(TAG, "onJsAlert");
                return super.onJsAlert(view, url, message, result);
            }

            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    showToast(getString(R.string.open_pic_fail));
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });
        webView.loadUrl(urlH5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else {
            showToast(getString(R.string.upload_pic_fail));
        }
    }
}
