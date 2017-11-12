package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.CooperateHolder;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.listener.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class KeywordSearchCooperateAdapter extends RecyclerView.Adapter {

    private List<EnrollInfo> list;
    private final Date mEndDate;
    private final SimpleDateFormat mSimpleDateFormat;
    private OnItemClickListener mOnItemClickListener;

    public KeywordSearchCooperateAdapter(List<EnrollInfo> list) {
        this.list = list;
        mEndDate = new Date();
        mSimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooperate_normal, parent, false);
        return new CooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

               if(holder instanceof CooperateHolder){
                   final CooperateHolder collHolder = (CooperateHolder) holder;
                   collHolder.itemView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (null != mOnItemClickListener) {
                               mOnItemClickListener.onItemClickListener(position);
                           }
                       }
                   });
                   EnrollInfo enrollInfo = list.get(position);
                   collHolder.tvItemTitle.setText(enrollInfo.title);
                   collHolder.tvItemDes.setText(enrollInfo.description);
                   if(list.get(position).enrollReceiving){
                       collHolder.tvItemStatus.setText("报名中");
                       collHolder.tvItemStatus.setTextColor(0xffe93c2c);
                   }else{
                       collHolder.tvItemStatus.setText("已结束");
                       collHolder.tvItemStatus.setTextColor(0xff333333);
                   }
                   mEndDate.setTime(enrollInfo.endTime);
                   String formatTime = mSimpleDateFormat.format(mEndDate);
                   String endTime = String.format(
                           ShowApplication.getAppContext().getString(R.string.cooperate_end_time),
                           formatTime);
                   collHolder.tvItemTime.setText(endTime);
                   //状态
                   //collHolder.tvItemStatus
        }
    }


    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    public void add(ArrayList<EnrollInfo> theatres){
        int lastIndex = this.list.size();
        if (this.list.addAll(theatres)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }


    public void notifyDataSetChange(List<EnrollInfo> theatres){
        list.clear();
        if(this.list.addAll(theatres)){
            notifyDataSetChanged();
        }
    }


}
