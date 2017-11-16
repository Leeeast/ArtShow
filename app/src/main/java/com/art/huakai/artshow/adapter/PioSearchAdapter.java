package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.art.huakai.artshow.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class PioSearchAdapter extends RecyclerView.Adapter {

    private static final String TAG = "PioSearchAdapter";
    private List<com.amap.api.services.core.PoiItem> list;
    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public PioSearchAdapter(Context context, List<com.amap.api.services.core.PoiItem> list) {
        this.list = list;
        this.mContext = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pio_search_item, null);
        TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
        return typeOneViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeOneViewHolder) {
            TypeOneViewHolder typeOneViewHolder = (TypeOneViewHolder) holder;
            if (list.get(position) != null) {
                PoiItem poiItem = list.get(position);
                if (poiItem != null) {
                    typeOneViewHolder.tv_location_name.setText(poiItem.getTitle());
                }
            }
            typeOneViewHolder.ll_whole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        Log.i(TAG, "onClick: 2222");
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            Log.e(TAG, "getItemCount: size==" + list.size());
            return list.size();
        }
        Log.e(TAG, "getItemCount: 00000");
        return 0;
    }


    public class TypeOneViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_location_name;
        private LinearLayout ll_whole;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_location_name = (TextView) itemView.findViewById(R.id.tv_location_name);
            ll_whole = (LinearLayout) itemView.findViewById(R.id.ll_whole);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void add(ArrayList<com.amap.api.services.core.PoiItem> works) {
        int lastIndex = this.list.size();
        if (this.list.addAll(works)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void notifyDataSetChange(ArrayList<com.amap.api.services.core.PoiItem> works) {
        list.clear();
        if (this.list.addAll(works)) {
            notifyDataSetChanged();
        }
    }


}
