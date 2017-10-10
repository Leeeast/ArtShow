package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 微信登录后绑定手机号码 Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class SetPwdSucFragment extends BaseFragment implements View.OnClickListener {
    private static final String PARAMS_PHONE = "PARAMS_PHONE";
    private static final String PARAMS_PWD = "PARAMS_PWD";
    private String mPhone;
    private String mPwd;
    private ShowProgressDialog showProgressDialog;

    public SetPwdSucFragment() {
    }

    public static SetPwdSucFragment newInstance(String phone, String pwd) {
        SetPwdSucFragment fragment = new SetPwdSucFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PARAMS_PHONE", phone);
        bundle.putString("PARAMS_PWD", pwd);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mPhone = bundle.getString("PARAMS_PHONE");
            mPwd = bundle.getString("PARAMS_PWD");
        }
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_set_pwd_suc;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_login_direct).setOnClickListener(this);
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
            case R.id.btn_login_direct:
                doLogin();
                break;
        }
    }

    /**
     * 直接去登录
     */
    private void doLogin() {
        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPwd) || !PhoneUtils.isMobileNumber(mPhone)) {
            showToast(getString(R.string.tip_data_parsing_exception));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("mobile", mPhone);
        params.put("password", mPwd);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_USER_LOGIN, params, 14, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, "obj = " + obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    //TODO 解析数据
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }
}
