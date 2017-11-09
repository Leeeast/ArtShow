package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class TalentNotifyEvent {
    public static final int NOTIFY_AVATAR = 0;//头像
    public static final int NOTIFY_BASE_INFO = 1;//基本信息
    public static final int NOTIFY_SEND = 2;//发布
    public static final int NOTIFY_INTRODUCE = 3;//介绍
    public static final int NOTIFY_WORKS_DES = 4;//项目介绍
    public static final int NOTIFY_AWARD_DES = 4;//获奖

    private int actionCode;

    public TalentNotifyEvent(int code) {
        actionCode = code;
    }

    public int getActionCode() {
        return actionCode;
    }

}
