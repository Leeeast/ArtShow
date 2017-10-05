package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EmptyHolder extends RecyclerView.ViewHolder {

    private final ImageView ivEmpty;

    public EmptyHolder(View itemView) {
        super(itemView);
        ivEmpty = (ImageView) itemView.findViewById(R.id.iv_empty);
    }
}
