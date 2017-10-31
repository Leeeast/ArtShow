package com.art.huakai.artshow.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 主创信息
 * Created by lidongliang on 2017/10/30.
 */

public class Staff implements Serializable {
    private String id;
    private String name;
    private String roleName;
    private String descpt;
    private String photo;
    public JSONObject getJsonObj() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());
            jsonObject.put("roleName", getRoleName());
            jsonObject.put("descpt", getDescpt());
            jsonObject.put("photo", getPhoto());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescpt() {
        return descpt;
    }

    public void setDescpt(String descpt) {
        this.descpt = descpt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
