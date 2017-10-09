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
    public static final String TEST_HOST = "http://139.224.47.213:8080";
    public static final String PRODUCE_HOST = "";
    public static final String HOST = isTestEnv ? TEST_HOST : PRODUCE_HOST;

    public static final String COMMON_BASE_URL = "/showonline_api";
    public static final String BASE_URL = HOST + COMMON_BASE_URL;
    //上传图片统一接口  POST方式，需要设置header，Content-Type: multipart/form-data
    public static final String URL_UPLOAD_FILE = HOST + "/showonline_upload/upload";
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
    //修改个人信息
    public static final String URL_USER_EDITINFO = BASE_URL + "/user/editInfo";
}
