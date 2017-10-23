package com.art.huakai.artshow.activity;

import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.fragment.AccountTypeSelectFragment;
import com.art.huakai.artshow.fragment.BindPhoneFragment;
import com.art.huakai.artshow.fragment.DataUploadFragment;
import com.art.huakai.artshow.fragment.DataUploadSusFragment;
import com.art.huakai.artshow.fragment.LoginFragment;
import com.art.huakai.artshow.fragment.LoginRegFragment;
import com.art.huakai.artshow.fragment.PerfectInfoFragment;
import com.art.huakai.artshow.fragment.RegisterFragment;
import com.art.huakai.artshow.fragment.RegisterSucFragment;
import com.art.huakai.artshow.fragment.RetrievePwdFragment;
import com.art.huakai.artshow.fragment.SetPwdFragment;
import com.art.huakai.artshow.fragment.SetPwdSucFragment;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity {

    //再次点击退出使用
    private long touchTime = 0;
    private int mCodeAction;

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
                BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance();
                initFragmentAddback(bindPhoneFragment);
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
}
