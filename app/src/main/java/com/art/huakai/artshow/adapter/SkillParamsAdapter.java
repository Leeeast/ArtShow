package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.ClassifyTypeSmallHolder;
import com.art.huakai.artshow.adapter.holder.SkillParamsHolder;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.ClassifyTypeBean;
import com.art.huakai.artshow.entity.TechParamsBean;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class SkillParamsAdapter extends RecyclerView.Adapter {
    private List<TechParamsBean> mlist;
    private Toast toast;

    public SkillParamsAdapter(List<TechParamsBean> list) {
        this.mlist = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theatre_params_skill, parent, false);
        return new SkillParamsHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SkillParamsHolder) {
            SkillParamsHolder skillParamsHolder = (SkillParamsHolder) holder;
            TechParamsBean techParamsBean = mlist.get(position);
            skillParamsHolder.tvName.setText(techParamsBean.getName());
            skillParamsHolder.tvValue.setText(techParamsBean.getValue());
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
