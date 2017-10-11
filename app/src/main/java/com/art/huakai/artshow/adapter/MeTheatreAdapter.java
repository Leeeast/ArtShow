package com.art.huakai.artshow.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.CooperateHolder;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.adapter.holder.MeTheatreHolder;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class MeTheatreAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 10;
    public static final int TYPE_NORMAL = 12;
    private List<String> mlist;

    public MeTheatreAdapter(List<String> list) {
        this.mlist = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_EMPTY == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = DeviceUtils.getScreenHeight(parent.getContext()) -
                    parent.getResources().getDimensionPixelSize(R.dimen.main_tab_bottom) -
                    parent.getResources().getDimensionPixelSize(R.dimen.collaborate_head);
            view.setLayoutParams(layoutParams);
            return new EmptyHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me_theatre, parent, false);
            return new MeTheatreHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                break;
            case TYPE_NORMAL:
                MeTheatreHolder meTheatreHolder = (MeTheatreHolder) holder;
                if (position == 0) {
                    RecyclerView.LayoutParams layoutParams =
                            (RecyclerView.LayoutParams) meTheatreHolder.rLyRootTheatre.getLayoutParams();
                    layoutParams.topMargin = meTheatreHolder.rLyRootTheatre.
                            getResources().getDimensionPixelSize(R.dimen.DIMEN_30PX);
                }
                meTheatreHolder.sdvTheatre.setImageURI(Uri.parse("asset:///test.png"));
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
