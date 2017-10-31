package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.adapter.holder.OrgProjectHolder;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class OrgProjectAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 10;
    public static final int TYPE_NORMAL = 12;
    private List<RepertorysBean> mlist;
    private OnItemClickListener onItemClickListener;
    private Date mEndDate;
    private SimpleDateFormat mSimpleDateFormat;

    public OrgProjectAdapter(List<RepertorysBean> list) {
        this.mlist = list;
        mEndDate = new Date();
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_org_project, parent, false);
            return new OrgProjectHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                break;
            case TYPE_NORMAL:
                OrgProjectHolder projectHolder = (OrgProjectHolder) holder;
                projectHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickListener(position);
                        }
                    }
                });
                if (position == 0) {
                    RecyclerView.LayoutParams layoutParams =
                            (RecyclerView.LayoutParams) projectHolder.rLyRootTheatre.getLayoutParams();
                    layoutParams.topMargin = projectHolder.rLyRootTheatre.
                            getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX);
                }
                RepertorysBean repertorysBean = mlist.get(position);
                projectHolder.sdvProject.setImageURI(repertorysBean.getLogo());
                projectHolder.tvProjectTitle.setText(repertorysBean.getTitle());
                projectHolder.tvActorCount.setText(String.valueOf(repertorysBean.getPeopleNum()));
                mEndDate.setTime(repertorysBean.getPremiereTime());
                String premiereTime = mSimpleDateFormat.format(mEndDate);
                projectHolder.tvFirstShowTime.setText(premiereTime);
                String price = String.format(holder.itemView.getResources().getString(R.string.me_theatre_price), repertorysBean.getExpense());
                projectHolder.tvProjectPrice.setText(price);
                if (repertorysBean.getStatus() == 1) {
                    projectHolder.tvProjectStatus.setText(R.string.send_status);
                    projectHolder.tvProjectStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_suc));
                } else {
                    projectHolder.tvProjectStatus.setText(R.string.unsend_status);
                    projectHolder.tvProjectStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_fail));
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
