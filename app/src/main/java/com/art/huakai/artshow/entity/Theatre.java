package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/20.
 */
public class Theatre implements Serializable {

    /**
     * id : 402881e85f30a14c015f30a15e2b0066
     * logo : https://www.showonline.com.cn/image/2017/08/29/691194ce02a0463686748e32d972cbc0@thumb.JPG
     * name : 磁劇場
     * roomName :
     * expense : 10000
     * seating : 227
     * regionName : 东城区
     * linkman : 华艺互联
     * linkTel : 18600508821
     * status : 1
     * createTime : 1508349402000
     */

    private String id;
    private String logo;
    private String name;
    private String roomName;
    private int expense;
    private int seating;
    private String regionName;
    private String linkman;
    private String linkTel;
    private int status;
    private long createTime;
    private String expenseDescpt;
    private String expenseUnit;
    private String seatingDescpt;

    public String getExpenseDescpt() {
        return expenseDescpt;
    }

    public void setExpenseDescpt(String expenseDescpt) {
        this.expenseDescpt = expenseDescpt;
    }

    public String getExpenseUnit() {
        return expenseUnit;
    }

    public void setExpenseUnit(String expenseUnit) {
        this.expenseUnit = expenseUnit;
    }

    public String getSeatingDescpt() {
        return seatingDescpt;
    }

    public void setSeatingDescpt(String seatingDescpt) {
        this.seatingDescpt = seatingDescpt;
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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
