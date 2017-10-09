package com.art.huakai.artshow.entity;

/**
 * 创建一个用户数据的单例类，保存用户数据
 * Created by lidongliang on 2017/10/4.
 */

public class LocalUserInfo {
    private LocalUserInfo() {
    }

    private static LocalUserInfo mLocalUserInfo;

    public synchronized static LocalUserInfo getInstance() {
        if (mLocalUserInfo == null) {
            mLocalUserInfo = new LocalUserInfo();
        }
        return mLocalUserInfo;
    }

    private int expire;
    private String accessToken;
    private String id;
    private String name;
    private String mobile;
    private String email;
    private String wechatOpenid;
    private String dp;
    private String password;
    //注册类型；3：个人账户，1：剧场用户，2：出品方用户
    private int userType;
    //0 默认，注册成功未选择账户类型 ，1 已经选择账户类型，未填写个人资料，2 资料完善，未认证，3，已认证
    private int status;
    private long createTime;

    //是否记录密码
    private boolean isKeepPwd;

    public boolean isKeepPwd() {
        return isKeepPwd;
    }

    public void setKeepPwd(boolean keepPwd) {
        isKeepPwd = keepPwd;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
