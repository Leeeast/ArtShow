package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class TheatreNotifyEvent {
    public static final int NOTIFY_THEATRE_AVATAR = 0;//头像
    public static final int NOTIFY_THEATRE_BASE_INFO = 1;//基本信息
    public static final int NOTIFY_THEATRE_SEND = 2;//发布
    public static final int NOTIFY_THEATRE_INTRODUCE = 3;//介绍
    public static final int NOTIFY_THEATRE_WORKS_DES = 4;//项目介绍
    public static final int NOTIFY_THEATRE_AWARD_DES = 4;//获奖

    private int actionCode;

    public TheatreNotifyEvent(int code) {
        actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }

}
