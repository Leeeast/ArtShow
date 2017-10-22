package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class ClassifyTypeBigHolder extends RecyclerView.ViewHolder {

    public TextView tvClassifyType;
    public RecyclerView recyclerView;

    public ClassifyTypeBigHolder(View itemView) {
        super(itemView);
        tvClassifyType = (TextView) itemView.findViewById(R.id.tv_classify_type);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_type);
    }
}
