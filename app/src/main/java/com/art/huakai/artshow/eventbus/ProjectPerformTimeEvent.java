package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectPerformTimeEvent {
    private String startTime;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    private String endTime;

    public ProjectPerformTimeEvent(String startTime, String endtime) {
        this.startTime = startTime;
        this.endTime = endtime;
    }
}
