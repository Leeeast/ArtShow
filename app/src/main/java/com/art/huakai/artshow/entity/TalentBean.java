package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lining on 2017/10/20.
 */
public class TalentBean {


    /**
     * id : 402894115f38179a015f38179aea0000
     * logo : http://139.224.47.213/image/8a999cce5f35db15015f381749680004.jpg
     * name : xdssdf
     * school : 北京舞蹈学院
     * age : 13
     * birthday : 1096560000000
     * classifyNames : ["话剧演员","影视演员"]
     * classifyNamesStr : 话剧演员 影视演员
     * regionName : 西城区
     * status : 1
     * createTime : 1508474586000
     * userId : 2c91faca5f37a50f015f37a6b7f80000
     * authentication : 1
     * agency : 收到发生的发
     * linkTel : 18511320250
     */

    private String id;
    private String logo;
    private String name;
    private String school;
    private int age;
    private long birthday;
    private String classifyNamesStr;
    private String regionName;
    private int status;
    private long createTime;
    private String userId;
    private int authentication;
    private String agency;
    private String linkTel;
    private List<String> classifyNames;

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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getClassifyNamesStr() {
        return classifyNamesStr;
    }

    public void setClassifyNamesStr(String classifyNamesStr) {
        this.classifyNamesStr = classifyNamesStr;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public List<String> getClassifyNames() {
        return classifyNames;
    }

    public void setClassifyNames(List<String> classifyNames) {
        this.classifyNames = classifyNames;
    }
}
