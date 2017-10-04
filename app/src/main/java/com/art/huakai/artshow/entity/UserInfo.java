package com.art.huakai.artshow.entity;

/**
 * 用户注册返回信息
 * Created by lidongliang on 2017/10/4.
 */
public class UserInfo {
    public int expire;
    public String accessToken;
    public User user;

    public class User {
        public String id;
        public String name;
        public String mobile;
        public String email;
        public String wechatOpenid;
        public String dp;
        public String password;
        public int userType;
        public int status;
        public long createTime;
    }
}
