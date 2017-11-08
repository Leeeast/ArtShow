package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class ActionTypeEvent {

    private boolean disableScroll;

    public ActionTypeEvent(boolean scroll) {
        this.disableScroll = scroll;
    }

    public boolean isDisableScroll() {
        return disableScroll;
    }

}
