package cn.qqtheme.framework.entity;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/21.
 */

public class CityBean implements LinkageItem {
    private int id;
    private String name;
    private List<String> children;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

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
