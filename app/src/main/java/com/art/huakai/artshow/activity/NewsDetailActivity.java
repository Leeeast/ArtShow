package com.art.huakai.artshow.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.AdvertBean;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.NewsDetail;
import com.art.huakai.artshow.entity.NewsesBean;
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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xrichtext.XRichText;
import cn.qqtheme.framework.util.ScreenUtils;
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
    @BindView(R.id.xrhtxt_detail_content)
    XRichText txrhtxtDetailContent;
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
        tvCreateTime.setText(DateUtil.transTime(String.valueOf(mNewsDetail.getCreateTime()), "yyyy年MM月dd"));
        tvEnrollViewTimes.setText(
                String.format(getString(R.string.enroll_detail_read),
                        mNewsDetail.getViewTimes())
        );
        txrhtxtDetailContent.callback(new XRichText.BaseClickCallback() {
            @Override
            public void onImageClick(List<String> urlList, int position) {

            }

            @Override
            public boolean onLinkClick(String url) {
                return true;
            }

            @Override
            public void onFix(XRichText.ImageHolder holder, Bitmap bitmap) {
                super.onFix(holder, bitmap);
                holder.setStyle(XRichText.Style.CENTER);
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                //设置宽高
                int screenWidth = ScreenUtils.widthPixels(getApplicationContext());
                int maxWidth = screenWidth - getResources().getDimensionPixelSize(R.dimen.DIMEN_30PX);
                int maxHidth = (maxWidth * bitmapHeight) / bitmapWidth;
                holder.setWidth(maxWidth);
                holder.setHeight(maxHidth);

            }
        }).text(mNewsDetail.getContent());
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
        RequestUtil.request(true, Constant.URL_GET_NEWS_DETAIL, params, 31, new RequestUtil.RequestListener() {

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
        RequestUtil.request(true, Constant.URL_ADVERT, null, 13, new RequestUtil.RequestListener() {
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
}
