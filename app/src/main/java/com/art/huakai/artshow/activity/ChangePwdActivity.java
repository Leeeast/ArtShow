package com.art.huakai.artshow.activity;

import android.view.View;
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

public class ChangePwdActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_verify_code)
    TextView edtVerifyCode;
    @BindView(R.id.tv_send_verify)
    TextView tvSendVerify;
    @BindView(R.id.edt_password)
    TextView edtPassword;
    @BindView(R.id.edt_pwd_affirm)
    TextView edtPwdAffirm;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_change_pwd);

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
     * 帐号认证
     */
    @OnClick(R.id.btn_change_confirm)
    public void changePwd() {
        Toast.makeText(this, "确认修改", Toast.LENGTH_SHORT).show();
    }


}
