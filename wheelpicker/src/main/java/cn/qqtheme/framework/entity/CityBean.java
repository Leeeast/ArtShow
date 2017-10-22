package cn.qqtheme.framework.entity;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/21.
 */

public class CityBean implements LinkageItem {
    private int id;
    private String name;
    private List<String> children;

    public Object getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public List<String> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getName();
    }
}
