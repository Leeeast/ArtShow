package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/18.
 */
public class TheatersBean implements Serializable {

    /**
     * id : 402894115f2f3d8f015f2f3e123a0181
     * logo : https://www.showonline.com.cn/image/2017/09/13/323b9fb21d6e4fde92a33fc3a75f2dd5@thumb.JPG
     * name : 北外剧场
     * roomName :
     * expense : 48000
     * seating : 1014
     * regionName : null
     * linkman : 华艺互联
     * linkTel : 13811615602
     * status : 1
     * createTime : null
     */

    private String id;
    private String logo;
    private String name;
    private String roomName;
    private int expense;
    private int seating;
    private Object regionName;
    private String linkman;
    private String linkTel;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public Object getRegionName() {
        return regionName;
    }

    public void setRegionName(Object regionName) {
        this.regionName = regionName;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
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
