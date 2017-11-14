package com.art.huakai.artshow.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.art.huakai.artshow.activity.LoginActivity;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.SharePreUtil;

/**
 * Created by Administrator on 2017/11/14.
 */

public class ReLoginService extends IntentService {

    public ReLoginService() {
        this("ReLoginService");
    }

    public ReLoginService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "00000", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharePreUtil.getInstance().clearUserInfo();
        SharePreUtil.getInstance().initUserInfo();
        LoginUtil.clearLocalUserInfo();
        Intent intentLogin = new Intent(ReLoginService.this, LoginActivity.class);
        ReLoginService.this.startActivity(intentLogin);
    }
}
