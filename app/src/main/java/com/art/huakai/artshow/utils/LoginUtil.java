package com.art.huakai.artshow.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.LoginActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.CommonTipDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.RegUserInfo;

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
    public static boolean checkUserLogin(final Context context, boolean isShowDialog) {
        if (!TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken()) &&
                !TextUtils.isEmpty(LocalUserInfo.getInstance().getMobile())) {
            return true;
        }
        if (isShowDialog) {
            CommonTipDialog Dialog = CommonTipDialog.getInstance(
                    context.getString(R.string.tip_dialog_login),
                    context.getString(R.string.cancel),
                    context.getString(R.string.app_login_bold));
            Dialog.setOnDismissListener(new CommonTipDialog.OnDismissListener() {
                @Override
                public void cancel() {

                }

                @Override
                public void sure() {
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((FragmentActivity) context).startActivityForResult(intent, JumpCode.FLAG_REQ_MAIN_LOGIN);
                }
            });
            Dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "COMMONTIP.DIALOG");
        }
        return false;
    }

    /**
     * 初始化用户信息到本地
     *
     * @param jsonStr
     */
    public static void initLocalUserInfo(String jsonStr) {
        try {
            RegUserInfo userInfo = GsonTools.parseData(jsonStr, RegUserInfo.class);
            LocalUserInfo localUserInfo = LocalUserInfo.getInstance();
            localUserInfo.setExpire(userInfo.expire);
            localUserInfo.setAccessToken(userInfo.accessToken);
            localUserInfo.setId(userInfo.user.id);
            localUserInfo.setName(userInfo.user.name);
            localUserInfo.setMobile(userInfo.user.mobile);
            localUserInfo.setEmail(userInfo.user.email);
            localUserInfo.setWechatOpenid(userInfo.user.wechatOpenid);
            localUserInfo.setDp(userInfo.user.dp);
            localUserInfo.setPassword(userInfo.user.password);
            localUserInfo.setUserType(userInfo.user.userType);
            localUserInfo.setStatus(userInfo.user.status);
            localUserInfo.setCreateTime(userInfo.user.createTime);
            localUserInfo.setWechatUnionid(userInfo.user.wechatUnionid);
            localUserInfo.setTheterCount(userInfo.userResources.theaterCount);
            localUserInfo.setTalentCount(userInfo.userResources.talentCount);
            localUserInfo.setRepertoryCount(userInfo.userResources.repertoryCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
