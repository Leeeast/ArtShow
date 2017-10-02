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
    private static final String BASE_URL = isTestEnv ? TEST_BASE_URL : PRODUCE_BASE_URL;

    //获取验证码借口
    public static final String URL_GET_VERIFY_CODE = BASE_URL + "/verify/code?receiver=%s&method=sms";


}
