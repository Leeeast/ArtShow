package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 登录主页Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class LoginRegFragment extends BaseFragment implements View.OnClickListener {

    public LoginRegFragment() {
    }

    public static LoginRegFragment newInstance() {
        LoginRegFragment loginRegFragment = new LoginRegFragment();
        return loginRegFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_login_reg;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.fly_close_login).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
        rootView.findViewById(R.id.btn_register).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login_wechat).setOnClickListener(this);
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
                break;
            case R.id.btn_register:
                break;
            case R.id.btn_login_wechat:
                break;
        }
    }

}
