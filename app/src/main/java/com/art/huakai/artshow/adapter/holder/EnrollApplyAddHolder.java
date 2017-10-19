package com.art.huakai.artshow.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EnrollApplyAddHolder extends RecyclerView.ViewHolder {

    public Button btnProduceAdd;

    public EnrollApplyAddHolder(View itemView) {
        super(itemView);
        btnProduceAdd = (Button) itemView.findViewById(R.id.btn_produce_add);
    }
}
