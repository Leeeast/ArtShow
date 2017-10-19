package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/18.
 */

public class EnrollDetailInfo {
    public List<String> enrolledAll;
    public List<String> enrolledAdopt;
    public EnrollDetail enroll;

    public class EnrollDetail {
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
    }
}
