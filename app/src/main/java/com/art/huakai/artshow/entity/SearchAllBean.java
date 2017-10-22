package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lining on 2017/10/22.
 */
public class SearchAllBean {

    private List<NewsesBean> news;
    private List<Theatre> theaters;
    private List<EnrollInfo> enrolls;
    private List<Work> repertorys;
    private List<TalentBean> talents;

    public List<TalentBean> getTalents() {
        return talents;
    }

    public void setTalents(List<TalentBean> talents) {
        this.talents = talents;
    }

    public List<NewsesBean> getNews() {
        return news;
    }

    public void setNews(List<NewsesBean> news) {
        this.news = news;
    }

    public List<Theatre> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<Theatre> theaters) {
        this.theaters = theaters;
    }

    public List<EnrollInfo> getEnrolls() {
        return enrolls;
    }

    public void setEnrolls(List<EnrollInfo> enrolls) {
        this.enrolls = enrolls;
    }

    public List<Work> getRepertorys() {
        return repertorys;
    }

    public void setRepertorys(List<Work> repertorys) {
        this.repertorys = repertorys;
    }





}
