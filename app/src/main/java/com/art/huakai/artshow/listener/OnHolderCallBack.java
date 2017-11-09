package com.art.huakai.artshow.listener;

import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView item 点击
 * Created by lidongliang on 2017/10/18.
 */

public interface OnHolderCallBack {
    void onItemClickListener(int position, RecyclerView.ViewHolder holder);
}
