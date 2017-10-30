package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lidonglaing on 2017/10/25.
 */

public class ProjectDetailInfo {

    private ProjectDetailInfo() {
    }

    private static ProjectDetailInfo projectDetailInfo;

    public synchronized static ProjectDetailInfo getInstance() {
        if (projectDetailInfo == null) {
            projectDetailInfo = new ProjectDetailInfo();
        }
        return projectDetailInfo;
    }

    private String id;
    private String logo;
    private String title;
    private String classifyId;
    private String linkman;
    private String linkTel;
    private String peopleNum;
    private String expense;
    private String regionId;
    private String seatingRequir;
    private String premiereTime;
    private String rounds;
    private String showLast;
    private String description;
    private String plot;
    private String awardsDescpt;
    private String requirements;
    private int status;
    private String userId;
    private long createTime;
    private String updateTime;
    private String performanceBeginDate;
    private String performanceEndDate;
    private String regionName;
    private List<PicturesBean> pictures;
    private List<Staff> staffs;
    private int viewTimes;

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

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
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

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSeatingRequir() {
        return seatingRequir;
    }

    public void setSeatingRequir(String seatingRequir) {
        this.seatingRequir = seatingRequir;
    }

    public String getPremiereTime() {
        return premiereTime;
    }

    public void setPremiereTime(String premiereTime) {
        this.premiereTime = premiereTime;
    }

    public String getRounds() {
        return rounds;
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getShowLast() {
        return showLast;
    }

    public void setShowLast(String showLast) {
        this.showLast = showLast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAwardsDescpt() {
        return awardsDescpt;
    }

    public void setAwardsDescpt(String awardsDescpt) {
        this.awardsDescpt = awardsDescpt;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPerformanceBeginDate() {
        return performanceBeginDate;
    }

    public void setPerformanceBeginDate(String performanceBeginDate) {
        this.performanceBeginDate = performanceBeginDate;
    }

    public String getPerformanceEndDate() {
        return performanceEndDate;
    }

    public void setPerformanceEndDate(String performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesBean> pictures) {
        this.pictures = pictures;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }
}
