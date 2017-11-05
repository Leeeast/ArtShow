package com.art.huakai.artshow.utils;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.LocalUserInfo;

/**
 * Created by lidongliang on 2017/11/6.
 */

public class AuthStatusUtil {
    public static String getAuthDes() {
        String authDes = "";
        //0-未申请认证，1-审核中， 2-审核驳回，3-审核通过
        if (LocalUserInfo.getInstance().getAuthenStatus() == 0) {
            authDes = ShowApplication.getAppContext().getString(R.string.institution_auth_immediately);
        } else if (LocalUserInfo.getInstance().getAuthenStatus() == 1) {
            authDes = ShowApplication.getAppContext().getString(R.string.account_authing);
        } else if (LocalUserInfo.getInstance().getAuthenStatus() == 2) {
            authDes = ShowApplication.getAppContext().getString(R.string.account_auth_fail);
        } else if (LocalUserInfo.getInstance().getAuthenStatus() == 3) {
            authDes = ShowApplication.getAppContext().getString(R.string.account_authed);
        }
        return authDes;
    }
}
