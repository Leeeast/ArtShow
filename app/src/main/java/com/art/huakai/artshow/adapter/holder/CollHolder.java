package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class CollHolder extends RecyclerView.ViewHolder {

    public TextView tvItemTitle, tvItemDes, tvItemTime, tvItemStatus;
    public LinearLayout lLyItemStatus;

    public CollHolder(View itemView) {
        super(itemView);
        tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
        tvItemDes = (TextView) itemView.findViewById(R.id.tv_item_des);
        tvItemTime = (TextView) itemView.findViewById(R.id.tv_item_time);
        lLyItemStatus = (LinearLayout) itemView.findViewById(R.id.lly_item_status);
        tvItemStatus = (TextView) itemView.findViewById(R.id.tv_item_status);
    }
}
