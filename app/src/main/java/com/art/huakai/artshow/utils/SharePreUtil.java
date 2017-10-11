package com.art.huakai.artshow.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.LocalUserInfo;

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
    private final String USER_KEEP_MOBILE = "USER_KEEP_MOBILE";
    private final String USER_KEEP_PWD = "USER_KEEP_PWD";
    //---userinfo start------------
    public final String USER_EXPIRE = "USER_EXPIRE";
    public final String USER_ACCESSTOKEN = "USER_ACCESSTOKEN";
    public final String USER_ID = "USER_ID";
    public final String USER_NAME = "USER_NAME";
    public final String USER_MOBILE = "USER_MOBILE";
    public final String USER_EMAIL = "USER_EMAIL";
    public final String USER_WECHATOPENID = "USER_WECHATOPENID";
    public final String USER_DP = "USER_DP";
    public final String USER_PASSWORD = "USER_PASSWORD";
    public final String USER_USERTYPE = "USER_USERTYPE";
    public final String USER_STATUS = "USER_STATUS";
    public final String USER_CREATETIME = "USER_CREATETIME";
    //---userinfo ecd------------

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

    /**
     * 保存手机号码
     */
    public void setUserMobile(String mobile) {
        Editor edit = mSp.edit();
        edit.putString(USER_KEEP_MOBILE, mobile);
        edit.commit();
    }

    /**
     * 获取手机号码
     */
    public String getUserMobile() {
        return mSp.getString(USER_KEEP_MOBILE, "");
    }

    /**
     * 设置是否保存密码
     *
     * @param pwd
     */
    public void setUserPwd(String pwd) {
        Editor edit = mSp.edit();
        edit.putString(USER_KEEP_PWD, pwd);
        edit.commit();
    }

    /**
     * 是否记住密码
     *
     * @return
     */
    public String getUserPwd() {
        return mSp.getString(USER_KEEP_PWD, "");
    }

    /**
     * 保存用户信息
     *
     * @param localUserInfo
     */
    public void storeUserInfo(LocalUserInfo localUserInfo) {
        Editor edit = mSp.edit();
        edit.putInt(USER_EXPIRE, localUserInfo.getExpire());
        edit.putString(USER_ACCESSTOKEN, localUserInfo.getAccessToken());
        edit.putString(USER_ID, localUserInfo.getId());
        edit.putString(USER_NAME, localUserInfo.getName());
        edit.putString(USER_MOBILE, localUserInfo.getMobile());
        edit.putString(USER_EMAIL, localUserInfo.getEmail());
        edit.putString(USER_WECHATOPENID, localUserInfo.getWechatOpenid());
        edit.putString(USER_DP, localUserInfo.getDp());
        edit.putString(USER_PASSWORD, localUserInfo.getPassword());
        edit.putInt(USER_USERTYPE, localUserInfo.getUserType());
        edit.putInt(USER_STATUS, localUserInfo.getStatus());
        edit.putLong(USER_CREATETIME, localUserInfo.getCreateTime());
        edit.commit();
    }

    /**
     * 读取用户信息
     */
    public void recoverUserInfo() {
        LocalUserInfo localUserInfo = LocalUserInfo.getInstance();
        localUserInfo.setExpire(mSp.getInt(USER_EXPIRE, 0));
        localUserInfo.setUserType(mSp.getInt(USER_USERTYPE, 0));
        localUserInfo.setStatus(mSp.getInt(USER_STATUS, 0));
        localUserInfo.setCreateTime(mSp.getLong(USER_CREATETIME, 0));
        localUserInfo.setAccessToken(mSp.getString(USER_ACCESSTOKEN, ""));
        localUserInfo.setId(mSp.getString(USER_ID, ""));
        localUserInfo.setName(mSp.getString(USER_NAME, ""));
        localUserInfo.setMobile(mSp.getString(USER_MOBILE, ""));
        localUserInfo.setEmail(mSp.getString(USER_EMAIL, ""));
        localUserInfo.setWechatOpenid(mSp.getString(USER_WECHATOPENID, ""));
        localUserInfo.setDp(mSp.getString(USER_DP, ""));
        localUserInfo.setPassword(mSp.getString(USER_PASSWORD, ""));
    }
}
