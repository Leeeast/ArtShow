package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.adapter.holder.OrgTheatreHolder;
import com.art.huakai.artshow.entity.PicturesBean;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.List;

/**
 * Created by lining on 2017/10/10.
 */
public class StaggerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_EMPTY = 20;
    private static final int TYPE_NORMAL = 21;


    private static final String TAG = "StaggerAdapter";
    private List<PicturesBean> datas;
    private Context context;
    private int itemWidth;
    private OnItemClickListener onItemClickListener;


    public StaggerAdapter(List<PicturesBean> data, Context context, int itemWidth) {
        this.datas = data;
        this.context = context;
        this.itemWidth = itemWidth;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_EMPTY == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stagger_item, parent, false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                View itemView = emptyHolder.itemView;
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.width = DeviceUtils.getScreenWeight(emptyHolder.itemView.getContext());
                itemView.setLayoutParams(layoutParams);

                FrameLayout.LayoutParams framLayoutParams = (FrameLayout.LayoutParams) emptyHolder.ivEmpty.getLayoutParams();
                framLayoutParams.topMargin = emptyHolder.itemView.getResources().getDimensionPixelSize(R.dimen.DIMEN_55PX);
                framLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                emptyHolder.ivEmpty.setLayoutParams(framLayoutParams);
                break;
            case TYPE_NORMAL:
                MyHolder myHolder = (MyHolder) holder;
                if (!TextUtils.isEmpty(datas.get(position).getMasterUrl())) {
                    try {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, (int) (datas.get(position).getHeight() / datas.get(position).getWidth() * itemWidth));
                        myHolder.chinaShowImageView.setLayoutParams(lp);
                        myHolder.chinaShowImageView.setImageURI(Uri.parse(datas.get(position).getMasterUrl()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
                        myHolder.chinaShowImageView.setLayoutParams(lp);
                        myHolder.chinaShowImageView.setImageURI(Uri.parse(datas.get(position).getMasterUrl()));

                    }
                }
                myHolder.chinaShowImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onItemClickListener: position==" + position);
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickListener(position);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas == null || datas.size() <= 0 ? 1 : datas.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (datas == null || datas.size() == 0) {
            return TYPE_EMPTY;
        } else {
            return TYPE_NORMAL;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ChinaShowImageView chinaShowImageView;

        public MyHolder(View view) {
            super(view);
            chinaShowImageView = (ChinaShowImageView) view.findViewById(R.id.csiv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

}
