package com.art.huakai.artshow.constant;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class Constant {
    //主页再次按返回按钮，退出程序，时间间隔
    public static final int EXIT_APP_TIME_OFFSET = 2000;
    //SplashActivity显示时间
    public static final int SPLASH_TIME_OFFSET = 2000;

    private static boolean isTestEnv = true;
    private static final String TEST_BASE_URL = "http://139.224.47.213:8080/showonline_api";
    private static final String PRODUCE_BASE_URL = "";
    public static final String BASE_URL = isTestEnv ? TEST_BASE_URL : PRODUCE_BASE_URL;

    //获取验证码接口
    public static final String URL_GET_VERIFY_CODE = BASE_URL + "/verify/code";
    //用户注册接口
    public static final String URL_USER_REGISTER = BASE_URL + "/user/register";
    //绑定账户类型
    public static final String URL_BIND_TYPE = BASE_URL + "/user/bindUserType";
    //修改密码
    public static final String URL_EDIT_PWD = BASE_URL + "/user/editPassword";
    //登录
    public static final String URL_USER_LOGIN = BASE_URL + "/user/login";
}
