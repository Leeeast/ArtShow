package com.art.huakai.artshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 设置页面
 * Created by lidongliang on 2017/10/13.
 */
public class SetActivity extends BaseActivity {

    @BindView(R.id.lly_back)
    LinearLayout lLyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Unbinder mUnbinder;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_set;
    }

    @Override
    public void initData() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void initView() {
    }

    @Override
    public void setView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.setting_title);
    }

    /**
     * 账户信息
     */
    @OnClick(R.id.item_account_info)
    public void jumpAccountInfo() {
        startActivity(new Intent(this, AccountInfoActivity.class));
    }

    /**
     * 微信绑定
     */
    @OnClick(R.id.item_bind_wechat)
    public void jumpBindWechat() {
        Toast.makeText(this, "微信绑定", Toast.LENGTH_SHORT).show();
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.item_clear_cache)
    public void clearCache() {
        Toast.makeText(this, "清除缓存", Toast.LENGTH_SHORT).show();
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.item_about_us)
    public void jumpAboutUs() {
        Toast.makeText(this, "关于我们", Toast.LENGTH_SHORT).show();
    }

    /**
     * 联系我们
     */
    @OnClick(R.id.item_connect_us)
    public void jumpConnectUs() {
        Toast.makeText(this, "联系我们", Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_exit_login)
    public void exitLogin() {
        Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
