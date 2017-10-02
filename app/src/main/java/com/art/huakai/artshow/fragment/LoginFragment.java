package com.art.huakai.artshow.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText edtPassword, edtPhone;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_login;
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
        btnLoginWechat.setOnClickListener(this);

        edtPhone = (EditText) rootView.findViewById(R.id.edt_phone);
        edtPassword = (EditText) rootView.findViewById(R.id.edt_password);


        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.tv_regiser).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forget_pwd).setOnClickListener(this);

        //记录密码监听
        ((CheckBox) rootView.findViewById(R.id.chk_pwd_record)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.chk_pwd_see)).setOnCheckedChangeListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_regiser:
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER));
                break;
            case R.id.btn_login:
                Toast.makeText(getContext(), "登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forget_pwd:
                //忘记密码
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_FORGET_PWD));
                break;
            case R.id.btn_login_wechat:
                //微信登录
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_BIND_PHONE));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chk_pwd_record:
                //TODO 是否记录密码逻辑

                break;
            case R.id.chk_pwd_see:
                if (isChecked) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //光标定位到最后
                edtPassword.setSelection(edtPassword.length());
                break;
        }
    }
}
