package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

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
    private Gson mGson;

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
        mGson = new Gson();
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
        String mMD5Pwd = MD5.getMD5(mPwd.getBytes());
        params.put("password", mMD5Pwd);
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
                    //记录帐号密码
                    if (SharePreUtil.getInstance().isKeepPwd()) {
                        SharePreUtil.getInstance().setUserMobile(mPhone);
                        SharePreUtil.getInstance().setUserPwd(mPwd);
                    }
                    //解析数据
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
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
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
