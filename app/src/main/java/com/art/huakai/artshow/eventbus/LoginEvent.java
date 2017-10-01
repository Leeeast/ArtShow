package com.art.huakai.artshow.eventbus;

/**
 * Created by lidongliang on 2017/9/30.
 */

public class LoginEvent {
    public static final int CODE_ACTION_LOGIN = 0;
    public static final int CODE_ACTION_REGISTER = 1;
    public static final int CODE_ACTION_FORGET_PWD = 2;
    public static final int CODE_ACTION_REGISTER_SUC = 3;
    private int actionCode;//0-登录，1-注册，2-忘记密码,3-注册成功

    public LoginEvent(int code) {
        this.actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }
}
