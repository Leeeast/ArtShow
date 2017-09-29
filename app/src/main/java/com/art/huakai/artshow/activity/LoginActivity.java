package com.art.huakai.artshow.activity;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.fragment.LoginRegFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

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
        mLoginRegFragment = LoginRegFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, mLoginRegFragment).commit();
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }


}
