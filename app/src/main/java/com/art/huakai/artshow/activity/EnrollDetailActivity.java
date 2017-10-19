package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.EnrollDetailInfo;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class EnrollDetailActivity extends BaseActivity {

    public static final String PARAMS_ENROLL_DETAIL = "PARAMS_ENROLL_DETAIL";
    public final int CODE_FILL_DATA = 10;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.tv_enroll_detail_content)
    TextView tvEnrollDetailContent;

    private EnrollInfo mEnrollInfo;
    private EnrollDetailInfo mEnrollDetailInfo;
    private ShowProgressDialog showProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_FILL_DATA:
                    fillData();
                    break;
            }
        }
    };

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
            mEnrollInfo = (EnrollInfo) extras.getSerializable(PARAMS_ENROLL_DETAIL);
        }
        getEnrollDetail();
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
        Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
    }

    /**
     * 报名
     */
    @OnClick(R.id.rly_enroll_apply)
    public void enrollApply() {
        invokActivity(this, EnrollApplyActivity.class, null, JumpCode.FLAG_REQ_ENROLL_APPLY);
    }

    /**
     * 填充数据
     */
    public void fillData() {
        //Spanned spanned = Html.fromHtml(mEnrollDetailInfo.content);
        new Thread() {
            @Override
            public void run() {
                final Spanned spanned = Html.fromHtml(mEnrollDetailInfo.enroll.content, imgGetter, null);
                tvEnrollDetailContent.post(new Runnable() {
                    @Override
                    public void run() {
                        tvEnrollDetailContent.setText(spanned);
                        tvEnrollDetailContent.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                });
            }
        }.start();

    }

    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(final String source) {
            Drawable drawable = null;

            try {
                URL url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片
            } catch (Exception e) {
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
            return drawable;
        }
    };


    //获取招募详情
    public void getEnrollDetail() {
        if (mEnrollInfo == null) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", mEnrollInfo.id);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_ENROLL_DETAIL, params, 31, new RequestUtil.RequestListener() {

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
}
