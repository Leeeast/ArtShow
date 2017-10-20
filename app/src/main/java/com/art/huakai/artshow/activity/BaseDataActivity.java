package com.art.huakai.artshow.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 基本资料Activity
 * Created by lidongliang on 2017/10/14.
 */

public class BaseDataActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_base_data;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.resume_base_data);
        tvSubtitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(LocalUserInfo.getInstance().getName())) {
            tvUserName.setText(LocalUserInfo.getInstance().getName());
        }
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
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void commitInfo() {
        Toast.makeText(this, "确认信息", Toast.LENGTH_SHORT).show();
    }

}
