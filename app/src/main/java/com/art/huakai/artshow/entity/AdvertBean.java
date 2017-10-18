package com.art.huakai.artshow.entity;

/**
 * Created by lining on 2017/10/18.
 */
public class AdvertBean {

    /**
     * id : 8a999cce5e473445015e4c3cd6940654
     * logo : https://www.showonline.com.cn/image/2017/09/04/f38335157d8b45f5b9aa179694d8e014.jpg
     * target : news://8a999cce5e473445015e4c3cd6940127
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
