package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/18.
 */
public class NewsesBean implements Serializable {

    /**
     * id : 8a999cce5e14535b015e18bb74e5001a
     * logo :
     * title : 2017青戏节发布会 | 从“一”马当先到“十”马奔腾
     * description : 2017青戏节发布会 | 从“一”马当先到“十”马奔腾
     * authName : 演出中国
     * createTime : 1507268617000
     */

    private String id;
    private String logo;
    private String title;
    private String description;
    private String authName;
    private long createTime;
    private String timeDescpt;

    public String getTimeDescpt() {
        return timeDescpt;
    }

    public void setTimeDescpt(String timeDescpt) {
        this.timeDescpt = timeDescpt;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
