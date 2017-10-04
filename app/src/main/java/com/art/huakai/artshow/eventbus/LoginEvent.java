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
    public static final int CODE_ACTION_BIND_PHONE = 8;
    public static final int CODE_ACTION_WECHAT_SET_PWD = 9;

    /**
     * 0-登录，1-注册，2-忘记密码,3-注册成功,4-账户类型选择成功,5-账户信息提交完成,6-注册完成,7-资料提交完成
     * 8-绑定手机号,9-微信登录设置密码
     */
    private int actionCode;

    public LoginEvent(int code) {
        this.actionCode = code;
    }

    public LoginEvent(int code, String phone, String verifyCode) {
        this.actionCode = code;
        this.phone = phone;
        this.verifyCode = verifyCode;
    }

    public int getActionCode() {
        return actionCode;
    }

    private String phone;
    private String verifyCode;

    public String getPhone() {
        return phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }
}
