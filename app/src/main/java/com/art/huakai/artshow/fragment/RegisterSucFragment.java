package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.ConfirmDialog;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class RegisterSucFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmDialog mTypeConfirmDialog;

    public RegisterSucFragment() {
        // Required empty public constructor
    }

    public static RegisterSucFragment newInstance() {
        RegisterSucFragment fragment = new RegisterSucFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_register_suc;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_return_main).setOnClickListener(this);
        rootView.findViewById(R.id.btn_commit_data).setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return_main:
                getActivity().finish();
                break;
            case R.id.lly_back:
                getActivity().finish();
                break;
            case R.id.btn_commit_data:
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_FINISH));
                break;
        }
    }
}
