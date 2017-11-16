package com.art.huakai.artshow.eventbus;

/**
 * Created by lidongliang on 2017/11/16.
 */

public class PullToRefreshEvent {
    public static final int TYPE_PULL_REFRESH = 0;
    public static final int TYPE_LOADING_MORE = 1;

    private int mType;

    public PullToRefreshEvent(int type) {
        this.mType = type;
    }

    public int getType() {
        return mType;
    }
}
