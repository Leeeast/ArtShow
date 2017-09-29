package com.art.huakai.artshow.eventbus;

/**
 * Created by lidongliang on 2017/9/30.
 */

public class LoginEvent {
    public static final int CODE_ACTION_LOGIN = 0;
    public static final int CODE_ACTION_REGISTER = 1;
    private int actionCode;

    public LoginEvent(int code) {
        this.actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }
}
