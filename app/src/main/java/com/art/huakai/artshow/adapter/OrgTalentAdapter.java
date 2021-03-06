package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.adapter.holder.OrgProjectHolder;
import com.art.huakai.artshow.adapter.holder.OrgTalentHolder;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.entity.TalentBean;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class OrgTalentAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 10;
    public static final int TYPE_NORMAL = 12;
    private List<TalentBean> mlist;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position, OrgTalentHolder holder);
    }

    public OrgTalentAdapter(List<TalentBean> list) {
        this.mlist = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_EMPTY == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = DeviceUtils.getScreenHeight(parent.getContext()) -
                    parent.getResources().getDimensionPixelSize(R.dimen.DIMEN_200PX);
            view.setLayoutParams(layoutParams);
            return new EmptyHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_org_talent, parent, false);
            return new OrgTalentHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                break;
            case TYPE_NORMAL:
                final OrgTalentHolder talentHolder = (OrgTalentHolder) holder;
                talentHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickListener(position, talentHolder);
                        }
                    }
                });
                if (position == 0) {
                    RecyclerView.LayoutParams layoutParams =
                            (RecyclerView.LayoutParams) talentHolder.rLyRootTheatre.getLayoutParams();
                    layoutParams.topMargin = talentHolder.rLyRootTheatre.
                            getResources().getDimensionPixelSize(R.dimen.DIMEN_11PX);
                    talentHolder.rLyRootTheatre.setLayoutParams(layoutParams);
                } else {
                    RecyclerView.LayoutParams layoutParams =
                            (RecyclerView.LayoutParams) talentHolder.rLyRootTheatre.getLayoutParams();
                    layoutParams.topMargin = talentHolder.rLyRootTheatre.
                            getResources().getDimensionPixelSize(R.dimen.DIMEN_7PX);
                    talentHolder.rLyRootTheatre.setLayoutParams(layoutParams);
                }
                TalentBean talentBean = mlist.get(position);
                talentHolder.sdvTalent.setImageURI(talentBean.getLogo());
                talentHolder.tvTalentName.setText(talentBean.getName());
                talentHolder.tvTalentAge.setText(String.valueOf(talentBean.getAge()));
                talentHolder.tvClassifyType.setText(talentBean.getClassifyNamesStr());
                if (talentBean.getStatus() == 1) {
                    talentHolder.tvTalentStatus.setText(R.string.send_status);
                    talentHolder.tvTalentStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_suc));
                } else {
                    talentHolder.tvTalentStatus.setText(R.string.unsend_status);
                    talentHolder.tvTalentStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_fail));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size() == 0 ? 1 : mlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist == null || mlist.size() == 0) {
            return TYPE_EMPTY;
        } else {
            return TYPE_NORMAL;
        }
    }
}
