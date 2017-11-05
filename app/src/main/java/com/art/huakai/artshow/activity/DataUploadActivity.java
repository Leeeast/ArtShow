package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.fragment.DataUploadFragment;
import com.art.huakai.artshow.fragment.DataUploadSusFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import butterknife.BindView;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class DataUploadActivity extends BaseActivity {
    public static final String PARAMS_FROM = "PARAMS_FROM";
    @BindView(R.id.fly_content)
    FrameLayout flyContent;
    private int mUserStatus;
    private String mFrom;


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_data_upload;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mFrom = extras.getString(PARAMS_FROM, DataUploadFragment.FROM_LOGIN);
        }
        mUserStatus = LocalUserInfo.getInstance().getAuthenStatus();
        switch (mUserStatus) {
            case LocalUserInfo.USER_UNAUTH:
            case LocalUserInfo.USER_AUTH_FAIL:
                DataUploadFragment dataUploadFragment = DataUploadFragment.newInstance(mFrom);
                initFragment(dataUploadFragment);
                break;
            case LocalUserInfo.USER_AUTHING:
            case LocalUserInfo.USER_AUTH_PASS:
                DataUploadSusFragment dataUploadSusFragment = DataUploadSusFragment.newInstance();
                initFragment(dataUploadSusFragment);
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }

    //显示fragment
    public void initFragment(BaseFragment baseFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, baseFragment, baseFragment.getTAG()).commit();
    }

    @Override
    public void onBackPressed() {
        setResult(JumpCode.FLAG_RES_DATA_AUTH);
        finish();
    }
}
