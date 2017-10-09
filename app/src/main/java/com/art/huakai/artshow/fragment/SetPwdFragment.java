package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.WebActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MD5;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 微信登录后绑定手机号码 Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class SetPwdFragment extends BaseFragment implements View.OnClickListener {
    private static final String PARAMS_PHONE = "PARAMS_PHONE";
    private static final String PARAMS_VERIFY_CODE = "PARAMS_VERIFY_CODE";
    private String mPhoneNum;
    private String mVerifyCode;
    private EditText edtPassword, edtPwdAffirm;
    private ShowProgressDialog showProgressDialog;

    public SetPwdFragment() {
        // Required empty public constructor
    }


    public static SetPwdFragment newInstance(String phoneNo, String verifyCode) {
        SetPwdFragment fragment = new SetPwdFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_PHONE, phoneNo);
        args.putString(PARAMS_VERIFY_CODE, verifyCode);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mPhoneNum = bundle.getString(PARAMS_PHONE);
            mVerifyCode = bundle.getString(PARAMS_VERIFY_CODE);
        }
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_set_pwd;
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

        edtPassword = (EditText) rootView.findViewById(R.id.edt_password);
        edtPwdAffirm = (EditText) rootView.findViewById(R.id.edt_pwd_affirm);

        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_register).setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
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
            case R.id.btn_register:
                setPwd();
                break;
            case R.id.tv_protocol:
                startActivity(new Intent(getContext(), WebActivity.class));
                break;
        }
    }

    /**
     * 设置密码
     */
    private void setPwd() {
        String pwd = edtPassword.getText().toString().trim();
        String pwdAffirm = edtPwdAffirm.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            showToast(getString(R.string.tip_input_pwd));
            return;
        }
        if (TextUtils.isEmpty(pwdAffirm)) {
            showToast(getString(R.string.tip_input_entrue_pwd));
            return;
        }
        if (!pwd.equals(pwdAffirm)) {
            showToast(getString(R.string.tip_input_dif_psw));
            return;
        }
        if (pwd.length() < 8) {
            showToast(getString(R.string.tip_input_length));
            return;
        }
        pwd = MD5.getMD5(pwd.getBytes());
        Map<String, String> params = new TreeMap<>();
        params.put("mobile", mPhoneNum);
        params.put("verifyCode", mVerifyCode);
        params.put("password", pwd);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "parmas:" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_EDIT_PWD, params, 13, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_set_pwd_success));
                    LocalUserInfo.getInstance().setMobile(mPhoneNum);
                    EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_RESET_PWD_SUCCESS));
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });

    }
}
