package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EnrollJoinAdapter;
import com.art.huakai.artshow.adapter.EnrolledAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.AdvertBean;
import com.art.huakai.artshow.entity.EnrollDetailInfo;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.AdvertJumpUtil;
import com.art.huakai.artshow.utils.DateUtil;
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

public class EnrollDetailActivity extends BaseActivity {

    public static final String PARAMS_ENROLL_ID = "PARAMS_ENROLL_ID";
    public final int CODE_FILL_DATA = 10;
    public final int CODE_FILL_AD = 11;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.xrhtxt_enroll_detail_content)
    XRichText txrhtxtEnrollDetailContent;
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

    private String mEnrollId;
    private EnrollDetailInfo mEnrollDetailInfo;
    private ShowProgressDialog showProgressDialog;
    private AdvertBean mAdvert;

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
        showProgressDialog = new ShowProgressDialog(this);
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
    }

    @Override
    public void setView() {
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
        if (mEnrollDetailInfo.enroll.enrollReceiving) {
            rLyEnrollApply.setVisibility(View.VISIBLE);
        } else {
            rLyEnrollApply.setVisibility(View.GONE);
        }
        tvEnrollMainTitle.setText(mEnrollDetailInfo.enroll.title);
        tvEnrollAnchor.setText(mEnrollDetailInfo.enroll.authName);
        tvEnrollTime.setText(DateUtil.transTime(String.valueOf(mEnrollDetailInfo.enroll.createTime), "yyyy年MM月dd"));
        tvEnrollEndTime.setText(
                String.format(getString(R.string.cooperate_end_time),
                        DateUtil.transTime(String.valueOf(mEnrollDetailInfo.enroll.endTime), "yyyy年MM月dd")));
        tvEnrollViewTimes.setText(
                String.format(getString(R.string.enroll_detail_read),
                        mEnrollDetailInfo.enroll.viewTimes)
        );
        txrhtxtEnrollDetailContent.callback(new XRichText.BaseClickCallback() {
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
        }).text(mEnrollDetailInfo.enroll.content);

        initEnrollAdopt();
        initEnrollAll();
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
        showProgressDialog.show();
        requestCallD = RequestUtil.request(true, Constant.URL_ENROLL_DETAIL, params, 31, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        mEnrollDetailInfo = GsonTools.parseData(obj, EnrollDetailInfo.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
