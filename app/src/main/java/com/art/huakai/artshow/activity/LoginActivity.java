package com.art.huakai.artshow.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.InfoBaseResp;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.UserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.fragment.AccountTypeSelectFragment;
import com.art.huakai.artshow.fragment.BindPhoneFragment;
import com.art.huakai.artshow.fragment.LoginFragment;
import com.art.huakai.artshow.fragment.LoginRegFragment;
import com.art.huakai.artshow.fragment.PerfectInfoFragment;
import com.art.huakai.artshow.fragment.RegisterFragment;
import com.art.huakai.artshow.fragment.RegisterSucFragment;
import com.art.huakai.artshow.fragment.RetrievePwdFragment;
import com.art.huakai.artshow.fragment.SetPwdFragment;
import com.art.huakai.artshow.fragment.SetPwdSucFragment;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class LoginActivity extends BaseActivity {

    //再次点击退出使用
    private long touchTime = 0;
    private int mCodeAction;
    private IWXAPI wxapi;
    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        EventBus.getDefault().register(this);
        LoginRegFragment mLoginRegFragment = LoginRegFragment.newInstance();
        initFragment(mLoginRegFragment);
        if (LoginUtil.checkUserLogin(this, false)) {
            if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_DEFAULT) {
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_SUC));
            } else if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_UNFILL_DATA) {
                EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM));
            }
        }
        regToWx();
    }

    /**
     * 微信相关初始化
     */
    private void regToWx() {
        wxapi = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APPID, true);
        wxapi.registerApp(Constant.WEIXIN_APPID);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }

    /**
     * 从登录fragment中来的事件，替换LogingActivity中的fragment
     *
     * @param enterEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(LoginEvent enterEvent) {
        if (enterEvent == null)
            return;
        mCodeAction = enterEvent.getActionCode();
        switch (enterEvent.getActionCode()) {
            case LoginEvent.CODE_ACTION_LOGIN:
                LoginFragment loginFragment = LoginFragment.newInstance();
                initFragmentAddback(loginFragment);
                break;
            case LoginEvent.CODE_ACTION_REGISTER:
                RegisterFragment registerFragment = RegisterFragment.newInstance();
                initFragmentAddback(registerFragment);
                break;
            case LoginEvent.CODE_ACTION_FORGET_PWD:
                RetrievePwdFragment retrievePwdFragment = RetrievePwdFragment.newInstance(enterEvent.getActionCode());
                initFragmentAddback(retrievePwdFragment);
                break;
            case LoginEvent.CODE_ACTION_REGISTER_SUC:
                AccountTypeSelectFragment accTypeSelFragment = AccountTypeSelectFragment.newInstance();
                initFragment(accTypeSelFragment);
                break;
            case LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM:
                PerfectInfoFragment perfectInfoFragment = PerfectInfoFragment.newInstance();
                initFragment(perfectInfoFragment);
                break;
            case LoginEvent.CODE_ACTION_PERFECT_INFO_SUC:
                RegisterSucFragment registerSucFragment = RegisterSucFragment.newInstance();
                initFragment(registerSucFragment);
                break;
            case LoginEvent.CODE_ACTION_BIND_PHONE:
                showProgressDialog.show();
                if (wxapi.isWXAppInstalled()) {
                    reigstWeixinLoginRiciver();
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    wxapi.sendReq(req);
                } else {
                    if (showProgressDialog.isShowing()) {
                        showProgressDialog.dismiss();
                    }
                    showToast(getString(R.string.weixin_no));
                }
                break;
            case LoginEvent.CODE_ACTION_WECHAT_SET_PWD:
                SetPwdFragment setPwdFragment =
                        SetPwdFragment.newInstance(enterEvent.getPhone(), enterEvent.getVerifyCode(), enterEvent.isResetPwd());
                initFragmentAddback(setPwdFragment);
                break;
            case LoginEvent.CODE_ACTION_RESET_PWD_SUCCESS:
                SetPwdSucFragment setPwdSucFragment = SetPwdSucFragment.newInstance(enterEvent.getPhone(), enterEvent.getPassword());
                initFragmentAddback(setPwdSucFragment);
                break;
        }
    }

    //显示fragment
    public void initFragment(BaseFragment baseFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, baseFragment, baseFragment.getTAG()).commit();
    }

    //添加一个fragment到返回栈中
    public void initFragmentAddback(BaseFragment baseFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, baseFragment, baseFragment.getTAG()).addToBackStack(baseFragment.getTAG()).commit();
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate("LoginFragment", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mCodeAction == LoginEvent.CODE_ACTION_REGISTER_SUC ||
                mCodeAction == LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) > Constant.EXIT_APP_TIME_OFFSET) {
                //让Toast的显示时间和等待时间相同
                Toast.makeText(this, getString(R.string.exit_perfect_tips), Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                //TODO 退出APP
                setResult(JumpCode.FLAG_RES_EXIT_APP);
                this.finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 用来接收从WXEntryActivity传入的微信回调的广播接收器
     */
    private void reigstWeixinLoginRiciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXEntryActivity.ACTION_WECHAT_AUTH_MESSAGE);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(wxReceiver, filter);
    }

    private BroadcastReceiver wxReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getSerializableExtra("onResp") != null) {
                onResp((InfoBaseResp) intent.getSerializableExtra("onResp"));
            }
        }
    };

    public void onResp(InfoBaseResp arg0) {
        final String authCode = arg0.getCode();
        String state = arg0.getState();
        int errCode = arg0.getErrCode();
        if (errCode == 0) {// 用户同意授权
            if (authCode != null && !(authCode.equals(""))) {
                Map<String, String> params = new TreeMap<>();
                params.put("code", authCode);
                String sign = SignUtil.getSign(params);
                params.put("sign", sign);
                RequestUtil.request(true, Constant.URL_USER_WXLOGIN, params, 91, new RequestUtil.RequestListener() {
                    @Override
                    public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                        LogUtil.i(TAG, obj);
                        if (showProgressDialog.isShowing()) {
                            showProgressDialog.dismiss();
                        }
                        if (isSuccess) {
                            try {
                                showToast("登陆成功");
                                LoginUtil.initLocalUserInfo(obj);
                                if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_DEFAULT) {
                                    EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_REGISTER_SUC));
                                } else if (LocalUserInfo.getInstance().getStatus() == LocalUserInfo.USER_STATUS_UNFILL_DATA) {
                                    EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM));
                                } else {
                                    updateUserInfo();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (code == ResponseCodeCheck.CODE_4012) {
                                Toast.makeText(LoginActivity.this, getString(R.string.wechat_login_suc_tip), Toast.LENGTH_LONG).show();
                                LocalUserInfo.getInstance().setWxAuthCode(authCode);
                                BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance();
                                initFragmentAddback(bindPhoneFragment);
                            }
                        }
                    }

                    @Override
                    public void onFailed(Call call, Exception e, int id) {
                        LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                        showToast(getString(R.string.wechat_login_fail));
                        if (showProgressDialog.isShowing()) {
                            showProgressDialog.dismiss();
                        }
                    }
                });
            }
        } else {
            showToast(getString(R.string.wechat_login_fail));
        }
        unregisterReceiver(wxReceiver);
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        if (LoginUtil.checkUserLogin(this, false)) {
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
                            finish();
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
