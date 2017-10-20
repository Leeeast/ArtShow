package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/21.
 */

public class ProvinceBean {
    public int id;
    public String name;
    public List<City> children;

    public class City {
        int id;
        String name;
        List<String> children;
    }
}
