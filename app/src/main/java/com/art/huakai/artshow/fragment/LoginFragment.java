package com.art.huakai.artshow.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.RegUserInfo;
import com.art.huakai.artshow.entity.UserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.MD5;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.LoadingButton;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 登录Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText edtPassword, edtPhone;
    private LoadingButton mLoadingButton;
    private CheckBox chkPwdRecord;
    private String mUserMobile;
    private String mUserPwd;
    private Gson mGson;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mGson = new Gson();
        mUserMobile = SharePreUtil.getInstance().getUserMobile();
        mUserPwd = SharePreUtil.getInstance().getUserPwd();
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
        mLoadingButton = (LoadingButton) rootView.findViewById(R.id.lbtn_login);
        mLoadingButton.setOnClickListener(this);
        //rootView.findViewById(R.id.lbtn_login).setOnClickListener(this);
        rootView.findViewById(R.id.tv_forget_pwd).setOnClickListener(this);

        //记录密码监听
        chkPwdRecord = (CheckBox) rootView.findViewById(R.id.chk_pwd_record);

        chkPwdRecord.setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.chk_pwd_see)).setOnCheckedChangeListener(this);
    }

    @Override
    public void setView() {
        if (!TextUtils.isEmpty(mUserMobile)) {
            edtPhone.setText(mUserMobile);
        }
        if (!TextUtils.isEmpty(mUserPwd)) {
            edtPassword.setText(mUserPwd);
        }
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
            case R.id.lbtn_login:
                doLogin();
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
                SharePreUtil.getInstance().setIsKeepPWD(isChecked);
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

    /**
     * 登录
     */
    public void doLogin() {
        final String phoneNum = edtPhone.getText().toString().trim();
        String pwd = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast(getString(R.string.tip_input_phone));
            return;
        }
        if (!PhoneUtils.isMobileNumber(phoneNum)) {
            showToast(getString(R.string.please_input_correct_phone));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast(getString(R.string.tip_input_pwd));
            return;
        }
        if (pwd.length() < 8) {
            showToast(getString(R.string.tip_input_length));
            return;
        }
        LocalUserInfo.getInstance().setMobile(phoneNum);
        pwd = MD5.getMD5(pwd.getBytes());
        Map<String, String> params = new TreeMap<>();
        params.put("mobile", phoneNum);
        params.put("password", pwd);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        mLoadingButton.startLoading();
        RequestUtil.request(true, Constant.URL_USER_LOGIN, params, 14, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                mLoadingButton.stopLoading();
                if (isSuccess) {
                    //记录帐号密码
                    if (chkPwdRecord.isChecked()) {
                        SharePreUtil.getInstance().setUserMobile(phoneNum);
                        SharePreUtil.getInstance().setUserPwd(edtPassword.getText().toString());
                    }
                    try {
                        RegUserInfo userInfo = mGson.fromJson(obj, RegUserInfo.class);
                        LocalUserInfo localUserInfo = LocalUserInfo.getInstance();
                        localUserInfo.setExpire(userInfo.expire);
                        localUserInfo.setAccessToken(userInfo.accessToken);
                        localUserInfo.setId(userInfo.user.id);
                        localUserInfo.setName(userInfo.user.name);
                        localUserInfo.setMobile(userInfo.user.mobile);
                        localUserInfo.setEmail(userInfo.user.email);
                        localUserInfo.setWechatOpenid(userInfo.user.wechatOpenid);
                        localUserInfo.setDp(userInfo.user.dp);
                        localUserInfo.setPassword(userInfo.user.password);
                        localUserInfo.setUserType(userInfo.user.userType);
                        localUserInfo.setStatus(userInfo.user.status);
                        localUserInfo.setCreateTime(userInfo.user.createTime);
                        SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_DEFAULT) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_SUC));
                    } else if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_UNFILL_DATA) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM));
                    } else {
                        updateUserInfo();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                mLoadingButton.stopLoading();
            }
        });
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        if (LoginUtil.checkUserLogin(getContext(), false)) {
            Map<String, String> params = new TreeMap<>();
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
            String sign = SignUtil.getSign(params);
            params.put("sign", sign);
            RequestUtil.request(true, Constant.URL_USER_PREVIEW, params, 16, new RequestUtil.RequestListener() {
                @Override
                public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                    LogUtil.i(TAG, obj);
                    if (isSuccess) {
                        try {
                            UserInfo userInfo = GsonTools.parseData(obj, UserInfo.class);
                            LocalUserInfo localUserInfo = LocalUserInfo.getInstance();
                            localUserInfo.setId(userInfo.user.id);
                            localUserInfo.setName(userInfo.user.name);
                            localUserInfo.setMobile(userInfo.user.mobile);
                            localUserInfo.setEmail(userInfo.user.email);
                            localUserInfo.setWechatOpenid(userInfo.user.wechatOpenid);
                            localUserInfo.setDp(userInfo.user.dp);
                            localUserInfo.setPassword(userInfo.user.password);
                            localUserInfo.setUserType(userInfo.user.userType);
                            localUserInfo.setStatus(userInfo.user.status);
                            localUserInfo.setCreateTime(userInfo.user.createTime);
                            localUserInfo.setAuthenStatus(userInfo.authenStatus);
                            localUserInfo.setTalentCount(userInfo.talentCount);
                            localUserInfo.setTheterCount(userInfo.theterCount);
                            localUserInfo.setRepertoryCount(userInfo.repertoryCount);
                            SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                            getActivity().finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ResponseCodeCheck.showErrorMsg(code);
                    }
                }

                @Override
                public void onFailed(Call call, Exception e, int id) {
                    LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                }
            });
        }
    }
}
