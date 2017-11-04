package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.WebActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.MD5;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
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
    private static final String PARAMS_IS_RESET_PWD = "PARAMS_IS_RESET_PWD";
    private static final String PARAMS_IS_WEIXIN_SETPWD = "PARAMS_IS_WEIXIN_SETPWD";

    private String mPhoneNum;
    private String mVerifyCode;
    private EditText edtPassword, edtPwdAffirm;
    private ShowProgressDialog showProgressDialog;
    private TextView tvSetPwdTitle;
    private boolean mIsResetPwd;
    private Button btnRegister;
    private TextView mTvProtocol;
    private String URL_SET_PWD;

    public SetPwdFragment() {
        // Required empty public constructor
    }


    public static SetPwdFragment newInstance(String phoneNo, String verifyCode, boolean isResetPwd) {
        SetPwdFragment fragment = new SetPwdFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_PHONE, phoneNo);
        args.putString(PARAMS_VERIFY_CODE, verifyCode);
        args.putBoolean(PARAMS_IS_RESET_PWD, isResetPwd);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mPhoneNum = bundle.getString(PARAMS_PHONE);
            mVerifyCode = bundle.getString(PARAMS_VERIFY_CODE);
            mIsResetPwd = bundle.getBoolean(PARAMS_IS_RESET_PWD, true);
        }
        if (mIsResetPwd) {
            URL_SET_PWD = Constant.URL_EDIT_PWD;
        } else {
            URL_SET_PWD = Constant.URL_USER_REGISTER;
        }
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_set_pwd;
    }

    @Override
    public void initView(View rootView) {
        mTvProtocol = (TextView) rootView.findViewById(R.id.tv_protocol);
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
        mTvProtocol.setText(spannableString);

        edtPassword = (EditText) rootView.findViewById(R.id.edt_password);
        edtPwdAffirm = (EditText) rootView.findViewById(R.id.edt_pwd_affirm);
        tvSetPwdTitle = (TextView) rootView.findViewById(R.id.tv_set_pwd_title);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);

        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        mTvProtocol.setOnClickListener(this);
    }

    @Override
    public void setView() {
        if (mIsResetPwd) {
            tvSetPwdTitle.setText(R.string.pwd_reset);
            btnRegister.setText(R.string.pwd_reset_affirm);
            mTvProtocol.setVisibility(View.INVISIBLE);
            mTvProtocol.setEnabled(false);
        } else {
            tvSetPwdTitle.setText(R.string.login_set_pwd);
            btnRegister.setText(R.string.app_register);
            mTvProtocol.setVisibility(View.VISIBLE);
            mTvProtocol.setEnabled(true);
        }
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
                Bundle bundle = new Bundle();
                bundle.putString(WebActivity.PARAMS_TYPE, WebActivity.KEY_SECRET);
                invokActivity(getContext(), WebActivity.class, bundle, JumpCode.FLAG_REQ_LINK);
                break;
        }
    }

    /**
     * 设置密码
     */
    private void setPwd() {
        final String pwd = edtPassword.getText().toString().trim();
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
        String MD5Pwd = MD5.getMD5(pwd.getBytes());
        Map<String, String> params = new TreeMap<>();
        if (!mIsResetPwd) {
            params.put("wxcode", LocalUserInfo.getInstance().getWxAuthCode());
        }
        params.put("mobile", mPhoneNum);
        params.put("verifyCode", mVerifyCode);
        params.put("password", MD5Pwd);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "parmas:" + params);
        showProgressDialog.show();
        RequestUtil.request(true, URL_SET_PWD, params, 13, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                try {
                    if (showProgressDialog.isShowing()) {
                        showProgressDialog.dismiss();
                    }
                    if (isSuccess) {
                        if (mIsResetPwd) {
                            showToast(getString(R.string.tip_set_pwd_success));
                            LocalUserInfo.getInstance().setMobile(mPhoneNum);
                            EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_RESET_PWD_SUCCESS, mPhoneNum, pwd));
                        } else {
                            showToast(getString(R.string.wechat_login_success));
                            LoginUtil.initLocalUserInfo(obj);
                            EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_SUC));
                        }
                    } else {
                        ResponseCodeCheck.showErrorMsg(code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
