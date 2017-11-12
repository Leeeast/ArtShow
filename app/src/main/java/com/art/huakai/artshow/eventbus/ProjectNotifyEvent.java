package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectNotifyEvent {
    public static final int NOTIFY_AVATAR = 0;//头像
    public static final int NOTIFY_BASE_INFO = 1;//基本信息
    public static final int NOTIFY_SEND = 2;//发布
    public static final int NOTIFY_INTRODUCE = 3;//介绍
    public static final int NOTIFY_INTRODUCE_SHOW = 4;//演出介绍
    public static final int NOTIFY_AWARD_DES = 5;//获奖
    public static final int NOTIFY_TECH_REQUIRE = 6;//技术要求
    public static final int NOTIFY_INTRODUCE_CREATE = 7;//主创介绍
    public static final int NOTIFY_PHOTO = 8;//相片

    private int actionCode;

    public ProjectNotifyEvent(int code) {
        actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }

}
