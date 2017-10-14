package com.art.huakai.artshow.eventbus;

/**
 * 修改名称后，各页面名称同步
 * Created by lidongliang on 2017/10/14.
 */

public class NameChangeEvent {

    private String mAccountName;

    public NameChangeEvent(String name) {
        this.mAccountName = name;
    }

    public String getAccountName() {
        return mAccountName;
    }

}
