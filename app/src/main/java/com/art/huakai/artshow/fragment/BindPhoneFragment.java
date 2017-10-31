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
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MyCountTimer;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 微信登录后绑定手机号码 Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class BindPhoneFragment extends BaseFragment {

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_verify_code)
    EditText edtVerifyCode;
    @BindView(R.id.tv_send_verify)
    TextView tvSendVerify;
    private ShowProgressDialog showProgressDialog;

    public BindPhoneFragment() {
        // Required empty public constructor
    }


    public static BindPhoneFragment newInstance() {
        BindPhoneFragment fragment = new BindPhoneFragment();
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_bind_phone;
    }

    @Override
    public void initView(View rootView) {
    }

    @Override
    public void setView() {
        edtVerifyCode.setText("098765");
    }

    @OnClick(R.id.btn_next_step)
    public void btnNextStep() {
        //EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_WECHAT_SET_PWD));
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
        Map<String, String> params = new TreeMap<>();
        params.put("wxcode", LocalUserInfo.getInstance().getWxAuthCode());
        params.put("scene", "beforeLogin");
        params.put("mobile", phoneNum);
        params.put("verifyCode", verifyCode);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_USER_WXBIND, params, 20, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {

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

    @OnClick(R.id.fly_send_verify)
    public void sendVerify() {
        requestVerifyCode();
    }

    /**
     * 获取验证码
     */
    public void requestVerifyCode() {
        String phoneNum = edtPhone.getText().toString().trim();
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
                    LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                    timeCount.onFinish();
                }
            });
        }
    }
}
