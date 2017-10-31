package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lidongliang on 2017/11/1.
 */

public class EnrollDetail implements Serializable {
    public String id;
    public String title;
    public String description;
    public long endTime;
    public String authName;
    public String content;
    public int status;
    public String entityType;
    public long createTime;
    public long updateTime;
    public int viewTimes;
    public boolean enrollReceiving;
    public String shareLink;
    public int orgOnly;
    public int numberLimit;
}
