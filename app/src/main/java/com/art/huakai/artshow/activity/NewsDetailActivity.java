package com.art.huakai.artshow.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.AdvertBean;
import com.art.huakai.artshow.entity.NewsDetail;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.AdvertJumpUtil;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class NewsDetailActivity extends BaseActivity {

    public static final String PARAMS_NEWS_ID = "PARAMS_NEWS_ID";
    public final int CODE_FILL_DATA = 10;
    public final int CODE_FILL_AD = 11;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView sdbLogo;
    @BindView(R.id.sdv_ad)
    SimpleDraweeView sdvAD;
    @BindView(R.id.tv_news_anchor)
    TextView tvNewsAnchor;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.tv_view_times)
    TextView tvEnrollViewTimes;
    @BindView(R.id.include_title)
    View viewTitle;
    @BindView(R.id.webview_rich)
    WebView webViewRich;

    private NewsDetail mNewsDetail;
    private ShowProgressDialog showProgressDialog;
    private ShareDialog shareDialog;
    private WbShareHandler mShareHandler;
    private AdvertBean mAdvert;
    private String mNewsId;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_FILL_DATA:
                    fillData();
                    break;
                case CODE_FILL_AD:
                    fillADData();
                    break;

            }
        }
    };
    private RequestCall requestCall2;
    private RequestCall requestCall1;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mNewsId = extras.getString(PARAMS_NEWS_ID);
        }
        getNewsDetail();
        getAD();
        mShareHandler = ShareDialog.regToWeibo(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.GONE);
        ivRightImg.setImageResource(R.mipmap.icon_share_white);
        initWebView();
    }

    @Override
    public void setView() {
        //处理状态栏遮挡问题
        int statusBarHeight = DeviceUtils.getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewTitle.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        viewTitle.bringToFront();
    }

    /**
     * 初始化webview相关
     */
    private void initWebView() {
        WebSettings settings = webViewRich.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webViewRich.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webViewRich.setWebChromeClient(new WebChromeClient());
        webViewRich.setWebViewClient(new WebViewClient());
        webViewRich.getSettings().setDefaultTextEncodingName("UTF-8");
        webViewRich.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewRich.getSettings().setMixedContentMode(webViewRich.getSettings()
                    .MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
    }

    /**
     * 解析富文本
     *
     * @param htmltext
     * @return
     */
    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }

    /**
     * 显示富文本
     *
     * @param content
     */
    public void setRichText(String content) {
        if (webViewRich == null) {
            return;
        }
        webViewRich.loadDataWithBaseURL(null, getNewContent(content), "text/html", "UTF-8", null);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 分享
     */
    @OnClick(R.id.fly_right_img)
    public void shareEnrollDetail() {
        if (mNewsDetail != null) {
            if (shareDialog == null) {
                String title = mNewsDetail == null ? getString(R.string.app_name) : mNewsDetail.getTitle();
                String shareLink = mNewsDetail == null ? getString(R.string.share_main_url) : mNewsDetail.getShareLink();
                shareDialog = ShareDialog.newInstence(title, shareLink);
                shareDialog.setShareHandler(mShareHandler);
            }
            shareDialog.show(getSupportFragmentManager(), "SHARE.DIALOG");
        }
    }

    /**
     * 跳转广告相应的内容
     */
    @OnClick(R.id.sdv_ad)
    public void jumpAD() {
        AdvertJumpUtil.invoke(this, this, mAdvert);
    }

    /**
     * 填充数据
     */
    public void fillData() {
        sdbLogo.setImageURI(mNewsDetail.getLogo());
        tvNewsTitle.setText(mNewsDetail.getTitle());
        tvNewsAnchor.setText(mNewsDetail.getAuthName());
        tvCreateTime.setText(DateUtil.transTime(String.valueOf(mNewsDetail.getCreateTime()), "yyyy年MM月dd日"));
        tvEnrollViewTimes.setText(
                String.format(getString(R.string.enroll_detail_read),
                        mNewsDetail.getViewTimes())
        );
        setRichText(getNewContent(mNewsDetail.getContent()));
    }

    //获取咨询详情
    public void getNewsDetail() {
        if (mNewsId == null) {
            showToast(getString(R.string.tip_data_error));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", mNewsId);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        requestCall1 = RequestUtil.request(true, Constant.URL_GET_NEWS_DETAIL, params, 31, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        mNewsDetail = GsonTools.parseData(obj, NewsDetail.class);
                        mHandler.sendEmptyMessage(CODE_FILL_DATA);
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

    //获取随机广告
    public void getAD() {
        requestCall2 = RequestUtil.request(true, Constant.URL_ADVERT, null, 13, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        mAdvert = GsonTools.parseData(obj, AdvertBean.class);
                        mHandler.sendEmptyMessage(CODE_FILL_AD);
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
            }
        });
    }

    /**
     * 填充广告
     */
    private void fillADData() {
        sdvAD.setImageURI(mAdvert.getLogo());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareDialog != null) {
            mShareHandler.doResultIntent(intent, shareDialog);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestCall1 != null) {
            requestCall1.cancel();
            requestCall1 = null;
        }
        if (requestCall2 != null) {
            requestCall2.cancel();
            requestCall2 = null;
        }
    }
}
