package com.art.huakai.artshow.utils;

import android.content.Context;
import android.text.TextUtils;

import com.art.huakai.artshow.entity.LocalUserInfo;

/**
 * 登录相关判断
 * Created by lidongliang on 2017/10/11.
 */

public class LoginUtil {
    /**
     * 判断用户是否登录
     *
     * @param context      上下文
     * @param isShowDialog 是否显示登录提示Dialog
     * @return
     */
    public static boolean checkUserLogin(Context context, boolean isShowDialog) {
        if (!TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken()) &&
                !TextUtils.isEmpty(LocalUserInfo.getInstance().getMobile())) {
            return true;
        }
        return false;
    }
}
