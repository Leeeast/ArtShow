package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.adapter.holder.OrgTheatreHolder;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.listener.OnHolderCallBack;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class OrgTheatreAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 10;
    public static final int TYPE_NORMAL = 12;
    private List<Theatre> mlist;
    private OnHolderCallBack onItemClickListener;

    public OrgTheatreAdapter(List<Theatre> list) {
        this.mlist = list;
    }

    public void setOnItemClickListener(OnHolderCallBack onItemClickListener) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_org_theatre, parent, false);
            return new OrgTheatreHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                break;
            case TYPE_NORMAL:
                final OrgTheatreHolder theatreHolder = (OrgTheatreHolder) holder;
                theatreHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickListener(position, theatreHolder);
                        }
                    }
                });
                if (position == 0) {
                    RecyclerView.LayoutParams layoutParams =
                            (RecyclerView.LayoutParams) theatreHolder.rLyRootTheatre.getLayoutParams();
                    layoutParams.topMargin = theatreHolder.rLyRootTheatre.
                            getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX);
                }
                Theatre theatre = mlist.get(position);
                theatreHolder.sdvTheatre.setImageURI(theatre.getLogo());
                theatreHolder.tvTheatreName.setText(theatre.getName());
                theatreHolder.tvSeatCount.setText(String.valueOf(theatre.getSeating()));
                theatreHolder.tvTheatrePosition.setText(theatre.getRegionName());
                String price = String.format(holder.itemView.getResources().getString(R.string.me_theatre_price), theatre.getExpense());
                theatreHolder.tvTheatrePrice.setText(price);
                if (theatre.getStatus() == 1) {
                    theatreHolder.tvTheatreStatus.setText(R.string.send_status);
                    theatreHolder.tvTheatreStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_suc));
                } else {
                    theatreHolder.tvTheatreStatus.setText(R.string.unsend_status);
                    theatreHolder.tvTheatreStatus.setTextColor(holder.itemView.getResources().getColor(R.color.theatre_send_fail));
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
