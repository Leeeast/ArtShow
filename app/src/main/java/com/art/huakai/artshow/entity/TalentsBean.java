package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lining on 2017/10/18.
 */
public class TalentsBean {

    /**
     * id : 402894115f2f3d8f015f2f3e239301e2
     * logo : https://www.showonline.com.cn/image/2017/07/21/e8cd6ae0c18d4544a85010524949b2e8@thumb.jpg
     * name : 秦枫
     * school : 中央戏剧学院
     * age : 0
     * birthday : 1508256000000
     * classifyNames : ["话剧演员","编剧","导演"]
     * classifyNamesStr : 话剧演员 编剧 导演
     * regionName :
     * status : 1
     * createTime : 1508326122000
     * userId : 8a999cce5d599d5c015d62bf21ac0038
     * authentication : 1
     * agency : 北京现代音乐学院
     * linkTel : 15101116378
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
