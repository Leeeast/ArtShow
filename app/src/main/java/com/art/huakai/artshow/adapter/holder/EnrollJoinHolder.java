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

public class EnrollJoinHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView sdvEnrollJoin;
    public TextView tvProjectTitle, tvFirstShowTime, tvOrgName;
    public RelativeLayout rLyRootTheatre;

    public EnrollJoinHolder(View itemView) {
        super(itemView);
        sdvEnrollJoin = (SimpleDraweeView) itemView.findViewById(R.id.sdv_enroll_join);
        tvProjectTitle = (TextView) itemView.findViewById(R.id.tv_project_title);
        tvOrgName = (TextView) itemView.findViewById(R.id.tv_org_name);
        tvFirstShowTime = (TextView) itemView.findViewById(R.id.tv_first_show_time);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
