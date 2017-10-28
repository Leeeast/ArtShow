package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/27.
 */
public class PicturesBean implements Serializable {
    /**
     * masterUrl : https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image
     * largeUrl : https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image
     * middleUrl : https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image
     * smallUrl : https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image
     * talentId : null
     * height : 1084
     * width : 750
     * createTime : null
     */

    private String masterUrl;
    private String largeUrl;
    private String middleUrl;
    private String smallUrl;
    private int height;
    private int width;

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }

    public String getMiddleUrl() {
        return middleUrl;
    }

    public void setMiddleUrl(String middleUrl) {
        this.middleUrl = middleUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
