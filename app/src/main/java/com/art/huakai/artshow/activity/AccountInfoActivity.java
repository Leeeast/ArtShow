package com.art.huakai.artshow.activity;

import android.view.View;
import android.widget.TextView;

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

public class AccountInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_account_info;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.set_account_info);

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
     * 名称
     */
    @OnClick(R.id.rly_name)
    public void showName() {
        invokActivity(this, AccountNameActivity.class, null, JumpCode.FLAG_REQ_SET_ACCOUNT_NAME);
    }

    /**
     * 帐号认证
     */
    @OnClick(R.id.rly_account_auth)
    public void accountAuth() {
        invokActivity(this, DataUploadActivity.class, null, JumpCode.FLAG_REQ_DATA_UPLOAD);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.rly_change_pwd)
    public void changePwd() {
        invokActivity(this, ChangePwdActivity.class, null, JumpCode.FLAG_REQ_CHANGE_PWD);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(NameChangeEvent nameChangeEvent) {
        if (nameChangeEvent == null) {
            return;
        }
        if (tvAccountName != null) {
            tvAccountName.setText(nameChangeEvent.getAccountName());
        }
    }
}
