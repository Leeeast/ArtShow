package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.widget.ChinaShowImageView;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EnrollApplyHolder extends RecyclerView.ViewHolder {

    public ChinaShowImageView sdvTheatre;
    public TextView tvTheatreName, tvSeatCount, tvFirstShowTime, tvProjectStatus;
    public CheckBox chkEnrollApply;
    public RelativeLayout rLyRootTheatre;

    public EnrollApplyHolder(View itemView) {
        super(itemView);
        sdvTheatre = (ChinaShowImageView) itemView.findViewById(R.id.sdv_theatre);
        tvTheatreName = (TextView) itemView.findViewById(R.id.tv_theatre_name);
        tvSeatCount = (TextView) itemView.findViewById(R.id.tv_actors_number);
        tvFirstShowTime = (TextView) itemView.findViewById(R.id.tv_first_show_time);
        chkEnrollApply = (CheckBox) itemView.findViewById(R.id.chk_enroll_apply);
        tvProjectStatus = (TextView) itemView.findViewById(R.id.tv_project_status);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
