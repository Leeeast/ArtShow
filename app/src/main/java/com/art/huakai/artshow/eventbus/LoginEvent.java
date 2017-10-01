package com.art.huakai.artshow.eventbus;

/**
 * Created by lidongliang on 2017/9/30.
 */

public class LoginEvent {
    public static final int CODE_ACTION_LOGIN = 0;
    public static final int CODE_ACTION_REGISTER = 1;
    public static final int CODE_ACTION_FORGET_PWD = 2;
    public static final int CODE_ACTION_REGISTER_SUC = 3;
    public static final int CODE_ACTION_ACCOUNT_TYPE_AFFIRM = 4;
    public static final int CODE_ACTION_PERFECT_INFO_SUC = 5;
    public static final int CODE_ACTION_REGISTER_FINISH = 6;
    public static final int CODE_ACTION_DATA_UPLOAD_SUC = 7;
    //0-登录，1-注册，2-忘记密码,3-注册成功,4-账户类型选择成功,5-账户信息提交完成,6-注册完成,7-资料提交完成
    private int actionCode;

    public LoginEvent(int code) {
        this.actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }
}
