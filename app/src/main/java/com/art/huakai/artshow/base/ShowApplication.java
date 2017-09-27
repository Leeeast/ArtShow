package com.art.huakai.artshow.base;

import android.app.Application;
import android.content.Context;

import com.art.huakai.artshow.utils.SharePreUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class ShowApplication extends Application {
    public static ShowApplication showApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        showApplication = this;
        init();
    }

    /**
     * 获取应用ApplicationContext
     *
     * @return
     */
    public static Context getAppContext() {
        return showApplication;
    }

    private void init() {
        //初始化初始化SharePreferences
        SharePreUtil.getInstance().initSharePre();
        //初始化Fresco
        Fresco.initialize(getApplicationContext());
    }
}
