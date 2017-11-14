package com.art.huakai.artshow.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MD5;
import com.art.huakai.artshow.utils.MyCountTimer;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class ChangePwdActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_verify_code)
    TextView edtVerifyCode;
    @BindView(R.id.tv_send_verify)
    TextView tvSendVerify;
    @BindView(R.id.edt_password)
    TextView edtPassword;
    @BindView(R.id.edt_pwd_affirm)
    TextView edtPwdAffirm;
    private ShowProgressDialog showProgressDialog;
    private String mPhoneNum;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        mPhoneNum = LocalUserInfo.getInstance().getMobile();
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_change_pwd);

    }

    @Override
    public void setView() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 帐号认证
     */
    @OnClick(R.id.btn_change_confirm)
    public void changePwd() {
        setPwd();
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_send_verify)
    public void getVerifyCode() {
        requestVerifyCode();
    }

    /**
     * 手机号是否占用
     */
    private void requestVerifyCode() {
        if (!PhoneUtils.isMobileNumber(mPhoneNum)) {
            showToast(getString(R.string.tip_data_error));
            return;
        }
        final MyCountTimer timeCount = new MyCountTimer(tvSendVerify,
                getResources().getColor(R.color.register_new),
                getResources().getColor(R.color.login_light));// 传入了文字颜色值
        timeCount.start();
        Map<String, String> params = new TreeMap<>();
        params.put("receiver", mPhoneNum);
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


    /**
     * 设置密码
     */

    private void setPwd() {
        String verifyCode = edtVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            showToast(getString(R.string.tip_input_verify_code));
            return;
        }
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
        params.put("mobile", mPhoneNum);
        params.put("verifyCode", verifyCode);
        params.put("password", MD5Pwd);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "parmas:" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_EDIT_PWD, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                try {
                    if (showProgressDialog.isShowing()) {
                        showProgressDialog.dismiss();
                    }
                    if (isSuccess) {
                        showToast(getString(R.string.tip_set_pwd_success));
                        finish();
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
