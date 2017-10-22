package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class ClassifyTypeSmallHolder extends RecyclerView.ViewHolder {

    public CheckedTextView chkClassifyType;

    public ClassifyTypeSmallHolder(View itemView) {
        super(itemView);
        chkClassifyType = (CheckedTextView) itemView.findViewById(R.id.chk_classify_type);
    }
}
