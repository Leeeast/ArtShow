package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class StaffHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView sdvStaff;
    public TextView tvCreatorName, tvCreatorRole;
    public RelativeLayout rLyRootTheatre;

    public StaffHolder(View itemView) {
        super(itemView);
        sdvStaff = (SimpleDraweeView) itemView.findViewById(R.id.sdv_staff);
        tvCreatorName = (TextView) itemView.findViewById(R.id.tv_creator_name);
        tvCreatorRole = (TextView) itemView.findViewById(R.id.tv_creator_role);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
