package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信登录后绑定手机号码 Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class BindPhoneFragment extends BaseFragment implements View.OnClickListener {

    public BindPhoneFragment() {
        // Required empty public constructor
    }


    public static BindPhoneFragment newInstance() {
        BindPhoneFragment fragment = new BindPhoneFragment();
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_bind_phone;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_next_step).setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_WECHAT_SET_PWD));
                break;
        }
    }
}
