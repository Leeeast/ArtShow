package com.art.huakai.artshow.constant;

/**
 * Created by lidongliang on 2017/5/24.
 */

public class JumpCode {

    // 一般请求码
    public static final int FLAG_REQ = 100;
    // 一般返回码
    public static final int FLAG_RES_RETURN = 500;

    // 请求码,往下加1
    public static final int FLAG_REQ_ACCOUNT_INFO = FLAG_REQ + 1;
    public static final int FLAG_REQ_SET_ACCOUNT_NAME = FLAG_REQ + 2;
    public static final int FLAG_REQ_ACCOUNT_NAME_CHANGE = FLAG_REQ + 3;
    public static final int FLAG_REQ_BIND_WECHAT = FLAG_REQ + 4;

    // 返回码,往下加1

}
