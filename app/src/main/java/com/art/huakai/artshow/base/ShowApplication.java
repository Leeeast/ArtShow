package com.art.huakai.artshow.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class ShowApplication extends Application {
    public static ShowApplication showApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        showApplication = this;
    }

    public static Context getAppContext() {
        return showApplication;
    }
}
