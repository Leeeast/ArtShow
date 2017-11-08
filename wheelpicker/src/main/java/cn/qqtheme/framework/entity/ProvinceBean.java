package cn.qqtheme.framework.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/21.
 */

public class ProvinceBean implements Serializable, LinkageItem {
    private int id;
    private String name;
    private List<CityBean> children;

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CityBean> getChildren() {
        return children;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<CityBean> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return getName();
    }
}
