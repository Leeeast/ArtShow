package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import butterknife.BindView;
import butterknife.OnClick;

public class EnrollDetailActivity extends BaseActivity {

    public static final String PARAMS_ENROLL_DETAIL = "PARAMS_ENROLL_DETAIL";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    private EnrollInfo mEnrollInfo;

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
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mEnrollInfo = (EnrollInfo) extras.getSerializable(PARAMS_ENROLL_DETAIL);
        }
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_name);
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

    @OnClick(R.id.fly_right_img)
    public void shareEnrollDetail() {
        Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
    }

}
