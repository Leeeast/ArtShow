package com.art.huakai.artshow.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录主页Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class LoginRegFragment extends BaseFragment implements View.OnClickListener {

    private ShowProgressDialog liveProgressDialog;

    public LoginRegFragment() {
    }

    public static LoginRegFragment newInstance() {
        LoginRegFragment loginRegFragment = new LoginRegFragment();
        return loginRegFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        liveProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_login_reg;
    }

    @Override
    public void initView(View rootView) {
        //设置Login Button DrawableLeft
        Button btnLoginWechat = (Button) rootView.findViewById(R.id.btn_login_wechat);
        Drawable drawableLeft = getResources().getDrawable(R.mipmap.icon_wechat);
        drawableLeft.setBounds(
                getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX),
                0,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX)
                        + getResources().getDimensionPixelSize(R.dimen.DIMEN_20PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_20PX));
        btnLoginWechat.setCompoundDrawables(drawableLeft, null, null, null);


        rootView.findViewById(R.id.fly_close_login).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
        rootView.findViewById(R.id.btn_register).setOnClickListener(this);
        btnLoginWechat.setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fly_close_login:
                getActivity().onBackPressed();
                break;
            case R.id.btn_login:
                //发送登录事件到LoginActivity中,去替换fragment
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_LOGIN));
                break;
            case R.id.btn_register:
                //发送注册事件到LoginActivity中,去替换fragment
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER));
                break;
            case R.id.btn_login_wechat:
                //微信登录
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_BIND_PHONE));
                break;
        }
    }

}
