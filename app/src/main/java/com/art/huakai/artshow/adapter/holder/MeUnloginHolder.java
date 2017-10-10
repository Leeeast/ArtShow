package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.art.huakai.artshow.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class MeUnloginHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView sdv;

    public MeUnloginHolder(View itemView) {
        super(itemView);
        sdv = (SimpleDraweeView) itemView.findViewById(R.id.sdv);
    }
}
