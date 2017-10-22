package com.art.huakai.artshow.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.ClassifyTypeBigHolder;
import com.art.huakai.artshow.adapter.holder.CooperateHolder;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.entity.ClassifyTypeBean;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class ClassifyTypeBigAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 10;
    public static final int TYPE_NORMAL = 11;
    private List<ClassifyTypeBean> mlist;
    private List<ClassifyTypeBean> mClassifyTypeAdded;

    public ClassifyTypeBigAdapter(List<ClassifyTypeBean> list, List<ClassifyTypeBean> classifyTypeAdded) {
        this.mlist = list;
        this.mClassifyTypeAdded = classifyTypeAdded;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_EMPTY == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = DeviceUtils.getScreenHeight(parent.getContext()) -
                    parent.getResources().getDimensionPixelSize(R.dimen.DIMEN_100PX);
            view.setLayoutParams(layoutParams);
            return new EmptyHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_type_big, parent, false);
            return new ClassifyTypeBigHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                break;
            case TYPE_NORMAL:
                if (position == mlist.size() - 1) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    layoutParams.bottomMargin = holder.itemView.getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX);
                }
                ClassifyTypeBigHolder bigHolder = (ClassifyTypeBigHolder) holder;
                ClassifyTypeBean classifyTypeBig = mlist.get(position);
                bigHolder.tvClassifyType.setText(classifyTypeBig.getName());

                List<ClassifyTypeBean> classifyTypeSmalls = classifyTypeBig.getChildren();
                if (classifyTypeSmalls != null && classifyTypeSmalls.size() > 0) {
                    ClassifyTypeSmallAdapter classifyTypeSmallAdapter = new ClassifyTypeSmallAdapter(classifyTypeSmalls,mClassifyTypeAdded);
                    GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(
                            3,
                            GridLayoutManager.VERTICAL,
                            holder.itemView.getResources().getDimensionPixelSize(R.dimen.DIMEN_20PX),
                            holder.itemView.getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX));
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.itemView.getContext(), 3);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    bigHolder.recyclerView.addItemDecoration(gridLayoutItemDecorationone);
                    bigHolder.recyclerView.setLayoutManager(gridLayoutManager);
                    bigHolder.recyclerView.setAdapter(classifyTypeSmallAdapter);
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
