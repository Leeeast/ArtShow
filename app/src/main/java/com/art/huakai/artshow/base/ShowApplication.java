package com.art.huakai.artshow.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.okhttp.OkHttpUtils;
import com.art.huakai.artshow.utils.FrescoHelper;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.tencent.stat.StatService;
import com.umeng.commonsdk.UMConfigure;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class ShowApplication extends MultiDexApplication {
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
        //初始化用户信息
        SharePreUtil.getInstance().initUserInfo();
        //初始化Fresco
        FrescoHelper.init(this);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        try {
            StatService.startStatService(this, null, com.tencent.stat.common.StatConstants.VERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //友盟在线参数
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, Constant.UM_APP_KEY);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
