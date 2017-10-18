package com.art.huakai.artshow.entity;

/**
 * Created by lining on 2017/10/18.
 */
public class BannersBean {


//    banner需要支持6种动作，根据target返回协议：
//    http://或https:// 打开网页
//    theater://           打开剧场详情
//    talent://             打开简历详情
//    repertory://       打开剧目详情
//    news://             打开资讯详情
//    enroll://             打开报名详情

    /**
     * id : 40288aba5f2865fc015f286602fe0006
     * logo : http://image2.135editor.com/cache/remote/aHR0cHM6Ly9tbWJpei5xcGljLmNuL21tYml6X3BuZy9UQ0Qzb1pwcUtpYWFXQm11ZmxPWjhjcm1DTWcyeERaYlBTRkdrcnVFMjNYS1Y0azQwTDBRRmljQUhQa2liUDFnTjhZRXA3cnZ4U2hWUmNjQWlhcUloOWNPV2cvMD93eF9mbXQ9cG5n
     * target : enroll://40288aba5f2865fc015f286602fe0002
     * status : 1
     * createTime : null
     */

    private String id;
    private String logo;
    private String target;
    private int status;
    private Object createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

}
