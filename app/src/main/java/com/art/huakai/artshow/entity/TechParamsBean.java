package com.art.huakai.artshow.entity;

/**
 * Created by lidongliang on 2017/11/3.
 */

public class TechParamsBean {
    private String name;
    private String value;

    public TechParamsBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
