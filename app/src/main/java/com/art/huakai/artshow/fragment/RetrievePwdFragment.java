package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MyCountTimer;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.SmartToast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 找回密码Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class RetrievePwdFragment extends BaseFragment implements View.OnClickListener {


    private EditText edtPhone, edtVerifyCode;
    private TextView tvSendVerify;
    private ShowProgressDialog showProgressDialog;

    public RetrievePwdFragment() {
    }

    public static RetrievePwdFragment newInstance() {
        RetrievePwdFragment fragment = new RetrievePwdFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
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
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getMobile())) {
            edtPhone.setText(LocalUserInfo.getInstance().getMobile());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_send_verify:
                verifyMobile();
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
     * 手机号是否占用
     */
    private void verifyMobile() {
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
            params.put("mobile", phoneNum);
            String sign = SignUtil.getSign(params);
            params.put("sign", sign);
            LogUtil.i(TAG, "params = " + params);
            RequestUtil.request(true, Constant.URL_USER_VERIFYMOBILE, params, 10, new RequestUtil.RequestListener() {
                @Override
                public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                    timeCount.onFinish();
                    LogUtil.i(TAG, obj);
                    if (isSuccess) {
                        SmartToast.makeToast(getContext(), null, null, Toast.LENGTH_SHORT).show();
                    } else {
                        requestVerifyCode();
                    }
                }

                @Override
                public void onFailed(Call call, Exception e, int id) {
                    LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                    timeCount.onFinish();
                }
            });
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
                    } else {
                        timeCount.onFinish();
                        ResponseCodeCheck.showErrorMsg(code);
                    }
                }

                @Override
                public void onFailed(Call call, Exception e, int id) {
                    timeCount.onFinish();
                    LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                }
            });
        }
    }
}
