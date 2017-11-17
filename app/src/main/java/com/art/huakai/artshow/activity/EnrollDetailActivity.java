package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EnrollJoinAdapter;
import com.art.huakai.artshow.adapter.EnrolledAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.PageLoadingDialog;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.entity.AdvertBean;
import com.art.huakai.artshow.entity.EnrollDetailInfo;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.listener.PageLoadingListener;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.AdvertJumpUtil;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
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

public class EnrollDetailActivity extends BaseActivity implements PageLoadingListener {

    public static final String PARAMS_ENROLL_ID = "PARAMS_ENROLL_ID";
    public static final int CODE_FILL_DATA = 10;
    public static final int CODE_FILL_AD = 11;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.tv_enroll_main_title)
    TextView tvEnrollMainTitle;
    @BindView(R.id.tv_enroll_anchor)
    TextView tvEnrollAnchor;
    @BindView(R.id.tv_enroll_time)
    TextView tvEnrollTime;
    @BindView(R.id.tv_enroll_end_time)
    TextView tvEnrollEndTime;
    @BindView(R.id.tv_enroll_view_times)
    TextView tvEnrollViewTimes;
    @BindView(R.id.include_enroll)
    View checkAdoptAdopt;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerViewAdopt;
    @BindView(R.id.recyclerview_all)
    RecyclerView recyclerViewAll;
    @BindView(R.id.include_enroll_all)
    View checkAdoptAll;
    @BindView(R.id.rly_enroll_apply)
    RelativeLayout rLyEnrollApply;
    @BindView(R.id.tv_enroll_all_count)
    TextView tvEnrollAllCount;
    @BindView(R.id.sdv_ad)
    SimpleDraweeView sdvAD;
    @BindView(R.id.webview_rich)
    WebView webViewRich;

    private String mEnrollId;
    private EnrollDetailInfo mEnrollDetailInfo;
    private PageLoadingDialog pageLoading;
    private AdvertBean mAdvert;
    private MyHandler myHandler;
    private MyRunnable myRunnable;

    private static class MyHandler extends Handler {

        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<EnrollDetailActivity> mActivty;

        public MyHandler(EnrollDetailActivity activity) {
            mActivty = new WeakReference<EnrollDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EnrollDetailActivity activity = mActivty.get();
            if (activity == null)
                return;
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
        private final WeakReference<EnrollDetailActivity> mActivty;

        public MyRunnable(EnrollDetailActivity activity) {
            mActivty = new WeakReference<EnrollDetailActivity>(activity);
        }

        @Override
        public void run() {
            EnrollDetailActivity enrollDetailActivity = mActivty.get();
            if (enrollDetailActivity != null && enrollDetailActivity.pageLoading.isShowing()) {
                enrollDetailActivity.pageLoading.dismiss();
            }
        }
    }

    private ShareDialog shareDialog;
    private WbShareHandler mShareHandler;
    private RequestCall requestCallD;
    private RequestCall requestCallAD;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_enroll_detail;
    }

    @Override
    public void initData() {
        myHandler = new MyHandler(this);
        myRunnable = new MyRunnable(this);
        pageLoading = new PageLoadingDialog(this);
        pageLoading.setPageLoadingListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mEnrollId = extras.getString(PARAMS_ENROLL_ID);
        }
        getEnrollDetail();
        getAD();
        mShareHandler = ShareDialog.regToWeibo(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.enroll_detail_tetle);
        ivRightImg.setImageResource(R.mipmap.icon_share_gray);
        initWebView();
    }

    @Override
    public void setView() {
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
        if (mEnrollDetailInfo != null) {
            if (shareDialog == null) {
                String title = mEnrollDetailInfo == null ? getString(R.string.app_name) : mEnrollDetailInfo.enroll.title;
                String shareLink = mEnrollDetailInfo == null ? getString(R.string.share_main_url) : mEnrollDetailInfo.enroll.shareLink;
                shareDialog = ShareDialog.newInstence(title, shareLink);
                shareDialog.setShareHandler(mShareHandler);
            }
            shareDialog.show(getSupportFragmentManager(), "SHARE.DIALOG");
        }
    }

    /**
     * 报名
     */
    @OnClick(R.id.rly_enroll_apply)
    public void enrollApply() {
        if (LoginUtil.checkUserLogin(this, true)) {
            if (mEnrollDetailInfo == null) {
                showToast(getString(R.string.tip_data_error));
                return;
            }
            if (mEnrollDetailInfo.enroll.orgOnly == 1 && LocalUserInfo.getInstance().getUserType() == 3) {
                showToast(getString(R.string.tip_enroll_disjoin));
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(EnrollApplyActivity.PARAMS_ENROLL_DETAIL, mEnrollDetailInfo);
            invokActivity(this, EnrollApplyActivity.class, bundle, JumpCode.FLAG_REQ_ENROLL_APPLY);
        }
    }

    /**
     * 查看全部入围项目
     */
    @OnClick(R.id.tv_see_all)
    public void checkAll() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EnrollJoinActivity.PARAMS_ENROLL, mEnrollDetailInfo);
        bundle.putInt(EnrollJoinActivity.PARAMS_TYPE, EnrollJoinActivity.TYPE_ENROLL_ENROLLED);
        invokActivity(this, EnrollJoinActivity.class, bundle, JumpCode.FLAG_REQ_CHECK_ENROLL);
    }

    /**
     * 查看全部参与项目
     */
    @OnClick(R.id.tv_enroll_all)
    public void checkEnrollAll() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EnrollJoinActivity.PARAMS_ENROLL, mEnrollDetailInfo);
        bundle.putInt(EnrollJoinActivity.PARAMS_TYPE, EnrollJoinActivity.TYPE_ENROLL_ALL);
        invokActivity(this, EnrollJoinActivity.class, bundle, JumpCode.FLAG_REQ_CHECK_ENROLL);
    }

    /**
     * 填充数据
     */
    public void fillData() {
        try {
            if (mEnrollDetailInfo.enroll.enrollReceiving) {
                rLyEnrollApply.setVisibility(View.VISIBLE);
            } else {
                rLyEnrollApply.setVisibility(View.GONE);
            }
            tvEnrollMainTitle.setText(mEnrollDetailInfo.enroll.title);
            tvEnrollAnchor.setText(mEnrollDetailInfo.enroll.authName);
            tvEnrollTime.setText(DateUtil.transTime(String.valueOf(mEnrollDetailInfo.enroll.createTime), "yyyy年MM月dd日"));
            tvEnrollEndTime.setText(
                    String.format(getString(R.string.cooperate_end_time),
                            DateUtil.transTime(String.valueOf(mEnrollDetailInfo.enroll.endTime), "yyyy年MM月dd日")));
            tvEnrollViewTimes.setText(
                    String.format(getString(R.string.enroll_detail_read),
                            mEnrollDetailInfo.enroll.viewTimes)
            );
            setRichText(mEnrollDetailInfo.enroll.content);
            initEnrollAdopt();
            initEnrollAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEnrollAdopt() {
        //若果为空直接设置城
        if (mEnrollDetailInfo.enrolledAdopt == null || mEnrollDetailInfo.enrolledAdopt.size() <= 0) {
            checkAdoptAdopt.setVisibility(View.GONE);
            return;
        }
        checkAdoptAdopt.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAdopt.setLayoutManager(linearLayoutManager);
        EnrollJoinAdapter enrollJoinAdapter = new EnrollJoinAdapter(mEnrollDetailInfo.enrolledAdopt);
        enrollJoinAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(WorksDetailMessageActivity.PARAMS_ID, mEnrollDetailInfo.enrolledAdopt.get(position).getId());
                invokActivity(EnrollDetailActivity.this, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
            }
        });
        recyclerViewAdopt.setAdapter(enrollJoinAdapter);
        recyclerViewAdopt.setNestedScrollingEnabled(false);
    }

    private void initEnrollAll() {
        //若果为空直接设置城
        if (mEnrollDetailInfo.enrolledAll == null || mEnrollDetailInfo.enrolledAll.size() <= 0) {
            checkAdoptAdopt.setVisibility(View.GONE);
            return;
        }
        checkAdoptAll.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAll.setLayoutManager(linearLayoutManager);
        EnrolledAdapter enrolledAdapter = new EnrolledAdapter(mEnrollDetailInfo.enrolledAll);
        enrolledAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(WorksDetailMessageActivity.PARAMS_ID, mEnrollDetailInfo.enrolledAll.get(position).getId());
                invokActivity(EnrollDetailActivity.this, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
            }
        });
        recyclerViewAll.setAdapter(enrolledAdapter);
        recyclerViewAll.setNestedScrollingEnabled(false);
    }

    //获取招募详情
    public void getEnrollDetail() {
        if (mEnrollId == null) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", mEnrollId);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params=" + params);
        if (!pageLoading.isShowing() && !this.isFinishing()) {
            pageLoading.show();
        }
        requestCallD = RequestUtil.request(true, Constant.URL_ENROLL_DETAIL, params, 31, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    myHandler.postDelayed(myRunnable, 300);
                    try {
                        mEnrollDetailInfo = GsonTools.parseData(obj, EnrollDetailInfo.class);
                        myHandler.sendEmptyMessage(CODE_FILL_DATA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                    pageLoading.showErrorLoading();
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                pageLoading.showErrorLoading();
            }
        });
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareDialog != null) {
            mShareHandler.doResultIntent(intent, shareDialog);
        }
    }

    //获取随机广告
    public void getAD() {
        requestCallAD = RequestUtil.request(true, Constant.URL_ADVERT, null, 13, new RequestUtil.RequestListener() {
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

    /**
     * 跳转广告相应的内容
     */
    @OnClick(R.id.sdv_ad)
    public void jumpAD() {
        AdvertJumpUtil.invoke(this, this, mAdvert);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestCallD != null) {
            requestCallD.cancel();
            requestCallD = null;
        }
        if (requestCallAD != null) {
            requestCallAD.cancel();
            requestCallAD = null;
        }
    }

    @Override
    public void onClose() {
        this.finish();
    }

    @Override
    public void onRetry() {
        getEnrollDetail();
    }
}
