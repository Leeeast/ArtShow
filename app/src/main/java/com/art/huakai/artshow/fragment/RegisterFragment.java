package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.WebActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.okhttp.OkHttpUtils;
import com.art.huakai.artshow.okhttp.callback.StringCallback;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MyCountTimer;
import com.art.huakai.artshow.utils.PhoneUtils;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * 注册新账户Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private EditText edtPhone, edtVerifyCode, edtPassword, edtPwdAffirm;
    private TextView tvSendVerify;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView(View rootView) {
        TextView tvProtocol = (TextView) rootView.findViewById(R.id.tv_protocol);
        SpannableString spannableString = new SpannableString(getString(R.string.register_protocol_tip));
        spannableString.setSpan(
                new TextAppearanceSpan(getContext(), R.style.protocol_style_gray),
                0,
                spannableString.length() - 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(
                new TextAppearanceSpan(getContext(), R.style.protocol_style_blue),
                spannableString.length() - 7,
                spannableString.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(spannableString);

        edtPhone = (EditText) rootView.findViewById(R.id.edt_phone);
        edtVerifyCode = (EditText) rootView.findViewById(R.id.edt_verify_code);
        edtPassword = (EditText) rootView.findViewById(R.id.edt_password);
        edtPwdAffirm = (EditText) rootView.findViewById(R.id.edt_pwd_affirm);

        tvSendVerify = (TextView) rootView.findViewById(R.id.tv_send_verify);

        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_register).setOnClickListener(this);

        tvProtocol.setOnClickListener(this);
        tvSendVerify.setOnClickListener(this);
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
            case R.id.tv_send_verify:
                requestVerifyCode();
                break;
            case R.id.tv_protocol:
                startActivity(new Intent(getContext(), WebActivity.class));
                break;
            case R.id.btn_register:
                //应该注册成功后，走接下来的完善信息流程
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_SUC));
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void requestVerifyCode() {
        String phoneNum = edtPhone.getText().toString();
        if (!PhoneUtils.isMobileNumber(phoneNum)) {
            Toast.makeText(getContext(), getString(R.string.please_input_correct_phone), Toast.LENGTH_SHORT).show();
            return;
        } else {
            final MyCountTimer timeCount = new MyCountTimer(tvSendVerify, 0xfff30008, 0xff969696);// 传入了文字颜色值
            timeCount.start();
            String url = String.format(Constant.URL_GET_VERIFY_CODE, phoneNum);
            OkHttpUtils
                    .get()
                    .url(url)
                    .id(100)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.e(TAG, e.getMessage() + "-" + id);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtil.i(TAG, response + ":id = " + id);
                        }
                    });
        }
        //et_yanzheng.requestFocus();
    }
}
