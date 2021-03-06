package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/20.
 */

public class TalentDetailInfo {
    private TalentDetailInfo() {
    }

    private static TalentDetailInfo talentResumeInfo;

    public synchronized static TalentDetailInfo getInstance() {
        if (talentResumeInfo == null) {
            talentResumeInfo = new TalentDetailInfo();
        }
        return talentResumeInfo;
    }

    private String id;
    private String logo;
    private String name;
    private String birthday;
    private String linkTel;
    private String regionId;
    private String description;
    private String height;
    private String weight;
    private String school;
    private String agency;
    private String worksDescpt;
    private String awardsDescpt;
    private int status;
    private String userId;
    private long createTime;
    private long updateTime;
    private List<PicturesBean> pictures;
    private List<Integer> classifyIds;
    private List<String> classifyNames;
    private String regionName;
    private int age;
    private int viewTimes;
    private String authentication;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getWorksDescpt() {
        return worksDescpt;
    }

    public void setWorksDescpt(String worksDescpt) {
        this.worksDescpt = worksDescpt;
    }

    public String getAwardsDescpt() {
        return awardsDescpt;
    }

    public void setAwardsDescpt(String awardsDescpt) {
        this.awardsDescpt = awardsDescpt;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesBean> pictures) {
        this.pictures = pictures;
    }

    public List<Integer> getClassifyIds() {
        return classifyIds;
    }

    public void setClassifyIds(List<Integer> classifyIds) {
        this.classifyIds = classifyIds;
    }

    public List<String> getClassifyNames() {
        return classifyNames;
    }

    public void setClassifyNames(List<String> classifyNames) {
        this.classifyNames = classifyNames;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }
}
