package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/18.
 */
public class NewsDetail implements Serializable {

    /**
     * "id": "8a999cce5e2f092a015e327aa33d00e6",
     * "logo": "https://www.showonline.com.cn/image/2017/08/30/b452d8834548414e8daa10980d9e1ef9.jpg",
     * "title": "2017北京市体制外优秀青年戏剧推荐展演",
     * "authName": "演出中国",
     * "description": "2017北京市体制外优秀青年戏剧推荐展演",
     * "content": ""
     * "status": 1,
     * "createTime": 1507268617000,
     * "updateTime": null,
     * "viewTimes": 46,
     * "shareLink": "https://www.showonline.com.cn/news/detail?id=8a999cce5e2f092a015e327aa33d00e6&scene=share"
     */

    private String id;
    private String logo;
    private String title;
    private String authName;
    private String description;
    private String content;
    private int status;
    private long createTime;
    private long updateTime;
    private int viewTimes;
    private String shareLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }
}
