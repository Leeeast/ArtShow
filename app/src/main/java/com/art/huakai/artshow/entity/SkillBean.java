package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lining on 2017/10/21.
 */
public class SkillBean {
    /**
     * id : 177
     * name : 演员
     * children : [{"id":178,"name":"话剧演员","children":[]},{"id":179,"name":"影视演员","children":[]},{"id":180,"name":"戏曲演员","children":[]},{"id":181,"name":"舞蹈演员","children":[]},{"id":182,"name":"歌唱演员","children":[]},{"id":183,"name":"器乐演员","children":[]},{"id":184,"name":"曲艺演员","children":[]},{"id":185,"name":"杂技演员","children":[]},{"id":186,"name":"魔术演员","children":[]},{"id":187,"name":"相声演员","children":[]},{"id":188,"name":"行为演员","children":[]},{"id":189,"name":"其他类演员","children":[]}]
     */

    private int id;
    private String name;
    private List<ChildrenBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }


}
