package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.widget.ChinaShowImageView;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class MeTheatreHolder extends RecyclerView.ViewHolder {

    public ChinaShowImageView sdvTheatre;
    public TextView tvTheatreStatus, tvSeatCount, tvTheatrePosition, tvTheatrePrice;
    public RelativeLayout rLyRootTheatre;

    public MeTheatreHolder(View itemView) {
        super(itemView);
        sdvTheatre = (ChinaShowImageView) itemView.findViewById(R.id.sdv_theatre);
        tvTheatreStatus = (TextView) itemView.findViewById(R.id.tv_theatre_status);
        tvSeatCount = (TextView) itemView.findViewById(R.id.tv_seat_count);
        tvTheatrePosition = (TextView) itemView.findViewById(R.id.tv_theatre_position);
        tvTheatrePrice = (TextView) itemView.findViewById(R.id.tv_theatre_price);
        rLyRootTheatre = (RelativeLayout) itemView.findViewById(R.id.rly_root_theatre);
    }
}
