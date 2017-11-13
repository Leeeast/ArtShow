package com.art.huakai.artshow.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 2017/10/22.
 */
public class SearchAllBean {

    private ArrayList<NewsesBean> news;
    private ArrayList<Theatre> theaters;
    private ArrayList<EnrollInfo> enrolls;
    private ArrayList<Work> repertorys;
    private ArrayList<TalentBean> talents;

    public ArrayList<TalentBean> getTalents() {
        return talents;
    }

    public void setTalents(ArrayList<TalentBean> talents) {
        this.talents = talents;
    }

    public ArrayList<NewsesBean> getNews() {
        return news;
    }

    public void setNews(ArrayList<NewsesBean> news) {
        this.news = news;
    }

    public ArrayList<Theatre> getTheaters() {
        return theaters;
    }

    public void setTheaters(ArrayList<Theatre> theaters) {
        this.theaters = theaters;
    }

    public ArrayList<EnrollInfo> getEnrolls() {
        return enrolls;
    }

    public void setEnrolls(ArrayList<EnrollInfo> enrolls) {
        this.enrolls = enrolls;
    }

    public ArrayList<Work> getRepertorys() {
        return repertorys;
    }

    public void setRepertorys(ArrayList<Work> repertorys) {
        this.repertorys = repertorys;
    }





}
