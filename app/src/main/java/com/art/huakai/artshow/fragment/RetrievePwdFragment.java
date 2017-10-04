package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MyCountTimer;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 找回密码Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class RetrievePwdFragment extends BaseFragment implements View.OnClickListener {


    private EditText edtPhone, edtVerifyCode;
    private TextView tvSendVerify;

    public RetrievePwdFragment() {
    }

    public static RetrievePwdFragment newInstance() {
        RetrievePwdFragment fragment = new RetrievePwdFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_retrieve_pwd;
    }

    @Override
    public void initView(View rootView) {
        edtPhone = (EditText) rootView.findViewById(R.id.edt_phone);
        edtVerifyCode = (EditText) rootView.findViewById(R.id.edt_verify_code);
        tvSendVerify = (TextView) rootView.findViewById(R.id.tv_send_verify);

        rootView.findViewById(R.id.btn_affirm).setOnClickListener(this);
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
            case R.id.btn_affirm:
                String phoneNum = edtPhone.getText().toString().trim();
                String verifyCode = edtVerifyCode.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    showToast(getString(R.string.tip_input_phone));
                    return;
                }
                if (!PhoneUtils.isMobileNumber(phoneNum)) {
                    showToast(getString(R.string.please_input_correct_phone));
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    showToast(getString(R.string.tip_input_verify_code));
                    return;
                }
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_WECHAT_SET_PWD, phoneNum, verifyCode));
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void requestVerifyCode() {
        String phoneNum = edtPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast(getString(R.string.tip_input_phone));
            return;
        }
        if (!PhoneUtils.isMobileNumber(phoneNum)) {
            showToast(getString(R.string.please_input_correct_phone));
            return;
        } else {
            final MyCountTimer timeCount = new MyCountTimer(tvSendVerify,
                    getResources().getColor(R.color.register_new),
                    getResources().getColor(R.color.login_light));// 传入了文字颜色值
            timeCount.start();
            HashMap<String, String> params = new HashMap<>();
            params.put("receiver", phoneNum);
            params.put("method", "sms");
            RequestUtil.request(false, Constant.URL_GET_VERIFY_CODE, params, 11, new RequestUtil.RequestListener() {
                @Override
                public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                    LogUtil.i(TAG, obj);
                    if (isSuccess) {

                    }
                }

                @Override
                public void onFailed(Call call, Exception e, int id) {
                    LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                }
            });
        }
    }
}
