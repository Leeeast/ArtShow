package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class SkillParamsHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvValue;

    public SkillParamsHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvValue = (TextView) itemView.findViewById(R.id.tv_value);
    }
}
