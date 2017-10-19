package com.art.huakai.artshow.entity;

/**
 * Created by lining on 2017/10/20.
 */
public class Work {
    /**
     * id : 8a999cce5e4ccfa8015e5691aeeb009b
     * logo : https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg
     * title : 我是我爷爷
     * agency : 青麦演艺经纪（北京）有限公司
     * expense : 100000
     * peopleNum : 30
     * premiereTime : 1508256000000
     * classifyId : 4
     * classifyName : 音乐剧
     * rounds : 0
     * status : 1
     * createTime : 1508347961982
     */

    private String id;
    private String logo;
    private String title;
    private String agency;
    private int expense;
    private int peopleNum;
    private long premiereTime;
    private int classifyId;
    private String classifyName;
    private int rounds;
    private int status;
    private long createTime;

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

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public long getPremiereTime() {
        return premiereTime;
    }

    public void setPremiereTime(long premiereTime) {
        this.premiereTime = premiereTime;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
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
