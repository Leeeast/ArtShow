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

public class OrgProjectHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView sdvProject;
    public TextView tvProjectTitle, tvProjectStatus, tvActorCount, tvProjectPrice, tvFirstShowTime;
    public RelativeLayout rLyRootTheatre;

    public OrgProjectHolder(View itemView) {
        super(itemView);
        sdvProject = (SimpleDraweeView) itemView.findViewById(R.id.sdv_projcet);
        tvProjectTitle = (TextView) itemView.findViewById(R.id.tv_project_title);
        tvProjectStatus = (TextView) itemView.findViewById(R.id.tv_project_status);
        tvActorCount = (TextView) itemView.findViewById(R.id.tv_actor_count);
        tvFirstShowTime = (TextView) itemView.findViewById(R.id.tv_first_show_time);
        tvProjectPrice = (TextView) itemView.findViewById(R.id.tv_project_price);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
