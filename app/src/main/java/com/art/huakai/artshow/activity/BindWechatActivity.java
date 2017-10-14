package com.art.huakai.artshow.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class BindWechatActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.fly_right_img)
    FrameLayout fLyRightImg;
    @BindView(R.id.switch_bind_wechat)
    Switch switchBingWechat;

    private Unbinder mUnbinder;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_bind_wechat;
    }

    @Override
    public void initData() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_bind_wachat);
        fLyRightImg.setVisibility(View.VISIBLE);
        ivRightImg.setImageResource(R.drawable.ic_more_horiz);
    }

    @Override
    public void setView() {
        switchBingWechat.setOnCheckedChangeListener(this);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * title右边选项
     */
    @OnClick(R.id.iv_right_img)
    public void moreSelect() {
        Toast.makeText(this, "攻城狮正在开发中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(this, "开", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "关", Toast.LENGTH_SHORT).show();
        }
    }
}
