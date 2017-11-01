package com.art.huakai.artshow.entity;

/**
 * 创建一个用户数据的单例类，保存用户数据
 * Created by lidongliang on 2017/10/4.
 */

public class LocalUserInfo {
    //0 默认，注册成功未选择账户类型 ，1 已经选择账户类型，未填写个人资料，2 资料完善，未认证，3，已认证
    public static final int
            USER_STATUS_DEFAULT = 0,
            USER_STATUS_UNFILL_DATA = 1,
            USER_STATUS_UNIDENTIFY = 2,
            USER_STATUS_IDENTIFY_SUC = 3;
    //用户类型
    public static final int
            USER_TYPE_PERSONAL = 3,
            USER_TYPE_INSTITUTION = 1;

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

    private String wechatUnionid;
    private String dp;
    private String password;
    //注册类型；3：个人账户，1：机构
    private int userType;
    //0 默认，注册成功未选择账户类型 ，1 已经选择账户类型，未填写个人资料，2 资料完善，未认证，3，已认证
    private int status;
    private long createTime;
    //0-未申请认证，1-审核中， 2-审核驳回，3-审核通过
    private int authenStatus;
    private int repertoryCount;
    private int talentCount;
    private int theterCount;

    //微信授权code
    private String wxAuthCode;

    public String getWxAuthCode() {
        return wxAuthCode;
    }

    public void setWxAuthCode(String wxAuthCode) {
        this.wxAuthCode = wxAuthCode;
    }

    //是否记录密码
    private boolean isKeepPwd;

    public boolean isKeepPwd() {
        return isKeepPwd;
    }

    public void setKeepPwd(boolean keepPwd) {
        isKeepPwd = keepPwd;
    }

    //验证码
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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

    public int getAuthenStatus() {
        return authenStatus;
    }

    public void setAuthenStatus(int authenStatus) {
        this.authenStatus = authenStatus;
    }

    public int getRepertoryCount() {
        return repertoryCount;
    }

    public void setRepertoryCount(int repertoryCount) {
        this.repertoryCount = repertoryCount;
    }

    public int getTalentCount() {
        return talentCount;
    }

    public void setTalentCount(int talentCount) {
        this.talentCount = talentCount;
    }

    public int getTheterCount() {
        return theterCount;
    }

    public void setTheterCount(int theterCount) {
        this.theterCount = theterCount;
    }

    public String getWechatUnionid() {
        return wechatUnionid;
    }

    public void setWechatUnionid(String wechatUnionid) {
        this.wechatUnionid = wechatUnionid;
    }
}
