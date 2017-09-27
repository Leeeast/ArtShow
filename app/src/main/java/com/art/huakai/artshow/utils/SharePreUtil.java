package com.art.huakai.artshow.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.art.huakai.artshow.base.ShowApplication;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class SharePreUtil {

    private SharedPreferences mSp;

    private SharePreUtil() {
    }

    private static SharePreUtil mSharePreUtil;
    private final String ART_SHOW_SP = "ART_SHOW_SP";

    private final String IS_FIRST_ENTER_APP = "IS_FIRST_ENTER_APP";


    public synchronized static SharePreUtil getInstance() {
        if (mSharePreUtil == null) {
            mSharePreUtil = new SharePreUtil();
        }
        return mSharePreUtil;
    }

    /**
     * 初始化SharePreferences
     */
    public void initSharePre() {
        if (mSp == null) {
            mSp = ShowApplication.getAppContext().getSharedPreferences(ART_SHOW_SP, Activity.MODE_PRIVATE);
        }
    }

    /**
     * 第一次进入app设置
     *
     * @param isFirstEnterApp
     */
    public void setIsFirstEnterApp(boolean isFirstEnterApp) {
        Editor edit = mSp.edit();
        edit.putBoolean(IS_FIRST_ENTER_APP, isFirstEnterApp);
        edit.commit();
    }

    /**
     * 是否是第一次进入app
     *
     * @return
     */
    public boolean isFirstEnterApp() {
        return mSp.getBoolean(IS_FIRST_ENTER_APP, true);
    }
}
