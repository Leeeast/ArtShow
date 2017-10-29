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

public class OrgTalentHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView sdvTalent;
    public TextView tvTalentName, tvTalentStatus, tvClassifyType, tvTalentAge;
    public RelativeLayout rLyRootTheatre;

    public OrgTalentHolder(View itemView) {
        super(itemView);
        sdvTalent = (SimpleDraweeView) itemView.findViewById(R.id.sdv_talent);
        tvTalentName = (TextView) itemView.findViewById(R.id.tv_talent_name);
        tvTalentStatus = (TextView) itemView.findViewById(R.id.tv_talent_status);
        tvClassifyType = (TextView) itemView.findViewById(R.id.tv_classify_type);
        tvTalentAge = (TextView) itemView.findViewById(R.id.tv_talent_age);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
