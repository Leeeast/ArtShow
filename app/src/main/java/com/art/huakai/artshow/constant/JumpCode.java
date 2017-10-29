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
    public static final int FLAG_REQ_CHANGE_PWD = FLAG_REQ + 5;
    public static final int FLAG_REQ_DATA_UPLOAD = FLAG_REQ + 6;
    public static final int FLAG_REQ_RESUME_MY = FLAG_REQ + 7;
    public static final int FLAG_REQ_BASE_DATA = FLAG_REQ + 8;
    public static final int FLAG_REQ_ENROLL_DETAIL = FLAG_REQ + 9;
    public static final int FLAG_REQ_ENROLL_APPLY = FLAG_REQ + 10;
    public static final int FLAG_REQ_RESUME_FILL = FLAG_REQ + 11;
    public static final int FLAG_REQ_CLASSIFY_TYPE = FLAG_REQ + 12;
    public static final int FLAG_REQ_MAIN_LOGIN = FLAG_REQ + 13;
    public static final int FLAG_REQ_THEATRE_MY = FLAG_REQ + 14;
    public static final int FLAG_REQ_THEATRE_EDIT = FLAG_REQ + 15;
    public static final int FLAG_REQ_THEATRE_BASE = FLAG_REQ + 16;
    public static final int FLAG_REQ_THEATRE_FILL = FLAG_REQ + 17;
    public static final int FLAG_REQ_PROJECT_MY = FLAG_REQ + 18;
    public static final int FLAG_REQ_PROJECT_BASE = FLAG_REQ + 19;
    public static final int FLAG_REQ_PROJECT_FILL = FLAG_REQ + 20;
    public static final int FLAG_REQ_DETAIL_PROJECT = FLAG_REQ + 21;
    public static final int FLAG_REQ_DETAIL_THEATRE = FLAG_REQ + 22;
    public static final int FLAG_REQ_TALENT_MY = FLAG_REQ + 23;
    public static final int FLAG_REQ_TALENT_EDIT = FLAG_REQ + 24;
    public static final int FLAG_REQ_DETAIL_PERSONAL = FLAG_REQ + 25;


    // 返回码,往下加1

    public static final int FLAG_RES_EXIT_APP = FLAG_RES_RETURN + 1;
    public static final int FLAG_RES_DATA_AUTH = FLAG_RES_RETURN + 2;
    public static final int FLAG_RES_CLASSIFY_TYPE_CONFIRM = FLAG_RES_RETURN + 3;

}
