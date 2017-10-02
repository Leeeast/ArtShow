package com.art.huakai.artshow.activity;

import android.support.v4.app.Fragment;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.fragment.AccountTypeSelectFragment;
import com.art.huakai.artshow.fragment.BindPhoneFragment;
import com.art.huakai.artshow.fragment.DataUploadFragment;
import com.art.huakai.artshow.fragment.DataUploadSusFragment;
import com.art.huakai.artshow.fragment.LoginFragment;
import com.art.huakai.artshow.fragment.LoginRegFragment;
import com.art.huakai.artshow.fragment.PerfectInfoFragment;
import com.art.huakai.artshow.fragment.RegisterFragment;
import com.art.huakai.artshow.fragment.RegisterSusFragment;
import com.art.huakai.artshow.fragment.RetrievePwdFragment;
import com.art.huakai.artshow.fragment.SetPwdFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity {

    private LoginRegFragment mLoginRegFragment;

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
        mLoginRegFragment = LoginRegFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, mLoginRegFragment).commit();
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
                RetrievePwdFragment retrievePwdFragment = RetrievePwdFragment.newInstance();
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
                RegisterSusFragment registerSusFragment = RegisterSusFragment.newInstance();
                initFragment(registerSusFragment);
                break;
            case LoginEvent.CODE_ACTION_REGISTER_FINISH:
                DataUploadFragment dataUploadFragment = DataUploadFragment.newInstance();
                initFragment(dataUploadFragment);
                break;
            case LoginEvent.CODE_ACTION_DATA_UPLOAD_SUC:
                DataUploadSusFragment dataUploadSusFragment = DataUploadSusFragment.newInstance();
                initFragment(dataUploadSusFragment);
                break;
            case LoginEvent.CODE_ACTION_BIND_PHONE:
                BindPhoneFragment bindPhoneFragment = BindPhoneFragment.newInstance();
                initFragmentAddback(bindPhoneFragment);
                break;
            case LoginEvent.CODE_ACTION_WECHAT_SET_PWD:
                SetPwdFragment setPwdFragment = SetPwdFragment.newInstance();
                initFragmentAddback(setPwdFragment);
                break;
        }
    }

    public void initFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, fragment).commit();
    }

    //添加一个fragment到返回栈中
    public void initFragmentAddback(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
