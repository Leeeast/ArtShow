package com.art.huakai.artshow.constant;

/**
 * Created by lidongliang on 2017/9/27.
 */

public class Constant {
    //主页再次按返回按钮，退出程序，时间间隔
    public static final int EXIT_APP_TIME_OFFSET = 2000;
    //SplashActivity显示时间
    public static final int SPLASH_TIME_OFFSET = 2000;
    //分页数据，每页展示数据条数
    public static final int COUNT_PER_PAGE = 12;
    //缓存时间 2个小时
    public static final int TIME_CACHE = 1000 * 60 * 60 * 2;
    //地区数据缓存时间
    public static final String TIME_ADDRESS_CACHE = "TIME_ADDRESS_CACHE";
    //地区数据缓存
    public static final String ADDRESS_CACHE = "ADDRESS_CACHE";
    //技能类型数据缓存时间
    public static final String TIME_CLASSIFY_CACHE = "TIME_CLASSIFY_CACHE";
    //技能类型缓
    public static final String CLASSIFY_CACHE = "CLASSIFY_CACHE";


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
    //获取用户信息
    public static final String URL_USER_INFO = BASE_URL + "/user/info";
    //修改个人信息
    public static final String URL_USER_EDITINFO = BASE_URL + "/user/editInfo";
    //手机号是否占用
    public static final String URL_USER_VERIFYMOBILE = BASE_URL + "/user/verifyMobile";
    //招募通告分页列表
    public static final String URL_ENROLL_PAGE = BASE_URL + "/enroll/page";
    //招募详情
    public static final String URL_ENROLL_DETAIL = BASE_URL + "/enroll/detail";
    //招募通告报名
    public static final String URL_ENROLL_ENROLL = BASE_URL + "/enroll/enroll";
    //已报名实体  已入围实体通过status区分 状态，0全部，1已入围的
    public static final String URL_ENROLL_ENROLLED = BASE_URL + "/enroll/enrolled";
    //简历分页
    public static final String URL_USER_TALENT = BASE_URL + "/user/talent";
    //简历分页
    public static final String URL_USER_TALENT_DETAIL = BASE_URL + "/user/talent/detail";
    //修改简历个人介绍
    public static final String URL_TALENT_EDIT_DESCRIPTION = BASE_URL + "/talent/edit/description";
    //简历详情
    public static final String URL_TALENT_DETAIL = BASE_URL + "/talent/detail";
    //修改简历获奖经历
    public static final String URL_TALENT_EDIT_AWARDSDESCPT = BASE_URL + "/talent/edit/awardsDescpt";
    //修改简历个人作品
    public static final String URL_TALENT_EDIT_WORKSDESCPT = BASE_URL + "/talent/edit/worksDescpt";
    //获取地区列表
    public static final String URL_REGION_LIST = BASE_URL + "/region/list";
    //获取技能类型列表 分类类型，talent：取人才类型，repertory：取演出项目类型
    public static final String URL_CLASSIFY_LIST = BASE_URL + "/classify/list";


    public static final String URL_GET_HOMEPAGE_INFOS = BASE_URL + "/index";

    public static final String URL_GET_CLASSFY_LIST = BASE_URL + "/classify/list";


    public static final String URL_GET_THEATRES = BASE_URL + "/theater/page";

    public static final String URL_GET_WORKS = BASE_URL + "/repertory/page";

    public static final String URL_GET_TALENTS = BASE_URL + "/talent/page";

//    获取地区借口
//    http://139.224.47.213:8080/showonline_api/region/list
//


}
