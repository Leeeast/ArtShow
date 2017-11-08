package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/27.
 */
public class PicturesBean implements Serializable {
    /**
     * "masterUrl": "http://139.224.47.213/image/8a999cce5f7d51fb015f7d5977700004.png",
     * "largeUrl": "http://139.224.47.213/image/8a999cce5f7d51fb015f7d5977700004.png",
     * "middleUrl": "http://139.224.47.213/image/8a999cce5f7d51fb015f7d5977700004.png",
     * "smallUrl": "http://139.224.47.213/image/8a999cce5f7d51fb015f7d5977700004.png",
     * "talentId": "8a999cce5f618543015f68f622740115",
     * "height": 1040,
     * "width": 780,
     * "createTime": 1509636536000
     */

    private String masterUrl;
    private String largeUrl;
    private String middleUrl;
    private String smallUrl;
    private String talentId;
    private int height;
    private int width;
    private long createTime;

    public String getTalentId() {
        return talentId;
    }

    public void setTalentId(String talentId) {
        this.talentId = talentId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

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
