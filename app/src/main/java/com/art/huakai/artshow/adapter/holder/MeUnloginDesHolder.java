package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class MeUnloginDesHolder extends RecyclerView.ViewHolder {

    public TextView tvCollDes;

    public MeUnloginDesHolder(View itemView) {
        super(itemView);
        tvCollDes = (TextView) itemView.findViewById(R.id.tv_me_unlogin_des);
    }
}
