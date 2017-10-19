package com.art.huakai.artshow.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EnrollApplyAddHolder;
import com.art.huakai.artshow.adapter.holder.EnrollApplyHolder;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EnrollApplyAdapter extends RecyclerView.Adapter {
    public static final int TYPE_NORMAL = 11;
    public static final int TYPE_ADD = 12;
    private List<String> mlist;
    private OnItemClickListener mOnItemClickListener;
    //控制单选项
    private int selectPosition;

    public EnrollApplyAdapter(List<String> list) {
        this.mlist = list;
        selectPosition = 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_ADD == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enroll_apply_add, parent, false);
            return new EnrollApplyAddHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enroll_apply, parent, false);
            return new EnrollApplyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_ADD:
                EnrollApplyAddHolder enrollApplyAddHolder = (EnrollApplyAddHolder) holder;
                enrollApplyAddHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClickListener(position);
                        }
                    }
                });
                break;
            case TYPE_NORMAL:
                final EnrollApplyHolder enrollApplyHolder = (EnrollApplyHolder) holder;
                enrollApplyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClickListener(position);
                        }
                        if (enrollApplyHolder.chkEnrollApply.isChecked()) {
                            selectPosition = -1;
                            notifyDataSetChanged();
                        } else {
                            selectPosition = position;
                            notifyDataSetChanged();
                        }
                    }
                });
                if (selectPosition == position) {
                    enrollApplyHolder.chkEnrollApply.setChecked(true);
                    enrollApplyHolder.rLyRootTheatre.setSelected(true);
                } else {
                    enrollApplyHolder.chkEnrollApply.setChecked(false);
                    enrollApplyHolder.rLyRootTheatre.setSelected(false);
                }
                enrollApplyHolder.sdvTheatre.setImageURI(Uri.parse("asset:///test.png"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        //多了一个添加选项，所以+1
        return mlist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist == null || mlist.size() == 0 || position == mlist.size()) {
            return TYPE_ADD;
        } else {
            return TYPE_NORMAL;
        }
    }
}
