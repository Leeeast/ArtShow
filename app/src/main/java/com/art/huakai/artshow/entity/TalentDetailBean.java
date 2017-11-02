package com.art.huakai.artshow.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 2017/10/27.
 */
public class TalentDetailBean implements Serializable {
    /**
     * id : 40288aba5f5d8e38015f5d8e59ac000c
     * logo : https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image
     * name : 张硕晨
     * birthday : 720115200000
     * linkman : null
     * linkTel : 18811411745
     * regionId : 34
     * description : 《生长》《白雪公主与魔镜》《罗斯莱尔》《无事生非》《罗刹国》
     * height : null
     * weight : null
     * school : 其他院校
     * agency : 自有职业
     * worksDescpt : 《生长》《白雪公主与魔镜》《罗斯莱尔》《无事生非》《罗刹国》
     * awardsDescpt : null
     * status : 1
     * userId : 8a999cce5df02f93015df11891b30008
     * createTime : 1509103130000
     * updateTime : null
     * pictures : [{"masterUrl":"https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image","largeUrl":"https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image","middleUrl":"https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image","smallUrl":"https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image","talentId":null,"height":1084,"width":750,"createTime":null},{"masterUrl":"https://www.showonline.com.cn/image/2017/08/18/a595501659764c3c8220014b04719da0.image","largeUrl":"https://www.showonline.com.cn/image/2017/08/18/a595501659764c3c8220014b04719da0.image","middleUrl":"https://www.showonline.com.cn/image/2017/08/18/a595501659764c3c8220014b04719da0.image","smallUrl":"https://www.showonline.com.cn/image/2017/08/18/a595501659764c3c8220014b04719da0.image","talentId":null,"height":852,"width":750,"createTime":null}]
     * classifyIds : [178,179,188]
     * classifyNames : ["话剧演员","影视演员","行为演员"]
     * regionName : null
     * age : 25
     * viewTimes : 1
     * authentication : 1
     */

    private String height;
    private String weight;

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

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getAwardsDescpt() {
        return awardsDescpt;
    }

    public void setAwardsDescpt(String awardsDescpt) {
        this.awardsDescpt = awardsDescpt;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String linkman;
    private String awardsDescpt;
    private String updateTime;

    private String id;
    private String logo;
    private String name;
    private long birthday;
    private String linkTel;
    private int regionId;
    private String description;
    private String school;
    private String agency;
    private String worksDescpt;
    private int status;
    private String userId;
    private long createTime;
    private int age;
    private int viewTimes;
    private int authentication;
    private ArrayList<PicturesBean> pictures;
    private List<Integer> classifyIds;
    private List<String> classifyNames;

    private String shareLink;

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
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

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public ArrayList<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<PicturesBean> pictures) {
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

}
