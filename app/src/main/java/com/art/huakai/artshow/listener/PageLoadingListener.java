package com.art.huakai.artshow.listener;

/**
 * RecyclerView item 点击
 * Created by lidongliang on 2017/10/18.
 */

public interface PageLoadingListener {
    /**
     * 关闭页面
     */
    void onClose();

    /**
     * 重试
     */
    void onRetry();
}
