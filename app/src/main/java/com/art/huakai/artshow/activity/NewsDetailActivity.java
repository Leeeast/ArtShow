package com.art.huakai.artshow.activity;

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
import com.art.huakai.artshow.dialog.PageLoadingDialog;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.entity.AdvertBean;
import com.art.huakai.artshow.entity.NewsDetail;
import com.art.huakai.artshow.listener.PageLoadingListener;
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
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class NewsDetailActivity extends BaseActivity implements PageLoadingListener {

    public static final int CODE_FILL_DATA = 10;
    public static final int CODE_FILL_AD = 11;
    public static final String PARAMS_NEWS_ID = "PARAMS_NEWS_ID";
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
    private PageLoadingDialog pageLoadingDialog;
    private ShareDialog shareDialog;
    private WbShareHandler mShareHandler;
    private AdvertBean mAdvert;
    private String mNewsId;
    private MyHandler myHandler;
    private MyRunnable myRunnable;

    private static class MyHandler extends Handler {

        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<NewsDetailActivity> mActivty;

        public MyHandler(NewsDetailActivity activity) {
            mActivty = new WeakReference<NewsDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewsDetailActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                //执行业务逻辑
                switch (msg.what) {
                    case CODE_FILL_DATA:
                        activity.fillData();
                        break;
                    case CODE_FILL_AD:
                        activity.fillADData();
                        break;
                }
            }
        }
    }

    private static class MyRunnable implements Runnable {
        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<NewsDetailActivity> mActivty;

        public MyRunnable(NewsDetailActivity activity) {
            mActivty = new WeakReference<NewsDetailActivity>(activity);
        }

        @Override
        public void run() {
            NewsDetailActivity newsDetailActivity = mActivty.get();
            if (newsDetailActivity != null && newsDetailActivity.pageLoadingDialog.isShowing()) {
                newsDetailActivity.pageLoadingDialog.dismiss();
            }
        }
    }

    private RequestCall requestCall2;
    private RequestCall requestCall1;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initData() {
        myHandler = new MyHandler(this);
        myRunnable = new MyRunnable(this);
        pageLoadingDialog = new PageLoadingDialog(this);
        pageLoadingDialog.setPageLoadingListener(this);
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
        ivRightImg.setImageResource(R.mipmap.icon_share_gray);
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
        if (sdbLogo == null) return;
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
        if (!pageLoadingDialog.isShowing() && !this.isFinishing()) {
            pageLoadingDialog.show();
        }
        requestCall1 = RequestUtil.request(true, Constant.URL_GET_NEWS_DETAIL, params, 31, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    myHandler.postDelayed(
                            myRunnable
                            , 300);
                    try {
                        mNewsDetail = GsonTools.parseData(obj, NewsDetail.class);
                        myHandler.sendEmptyMessage(CODE_FILL_DATA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                    pageLoadingDialog.showErrorLoading();
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                pageLoadingDialog.showErrorLoading();
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
                        myHandler.sendEmptyMessage(CODE_FILL_AD);
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

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void onRetry() {
        getNewsDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK && shareDialog != null) {
                Tencent.handleResultData(data, shareDialog);
            }
        }
    }
}
