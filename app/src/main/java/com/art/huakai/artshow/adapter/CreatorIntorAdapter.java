package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EnrollApplyAddHolder;
import com.art.huakai.artshow.adapter.holder.StaffHolder;
import com.art.huakai.artshow.entity.Staff;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class CreatorIntorAdapter extends RecyclerView.Adapter {
    public static final int TYPE_NORMAL = 11;
    public static final int TYPE_ADD = 12;
    private List<Staff> mlist;
    private OnItemClickListener mOnItemClickListener;

    public CreatorIntorAdapter(List<Staff> list) {
        this.mlist = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onCreateNew(int position);

        void onUpdate(int position);

        void onDelete(int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE_ADD == viewType) {
            //设置空布局高，使空布局图片居中
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enroll_apply_add, parent, false);
            return new EnrollApplyAddHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creator_mbmber, parent, false);
            return new StaffHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_ADD:
                EnrollApplyAddHolder enrollApplyAddHolder = (EnrollApplyAddHolder) holder;
                enrollApplyAddHolder.btnProduceAdd.setText(R.string.project_member_add);
                enrollApplyAddHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onCreateNew(position);
                        }
                    }
                });
                break;
            case TYPE_NORMAL:
                StaffHolder staffHolder = (StaffHolder) holder;
                staffHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onUpdate(position);
                        }
                    }
                });
                staffHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onDelete(position);
                        }
                        return true;
                    }
                });
                Staff staff = mlist.get(position);
                staffHolder.sdvStaff.setImageURI(staff.getPhoto());
                staffHolder.tvCreatorName.setText(staff.getName());
                staffHolder.tvCreatorRole.setText(staff.getRoleName());
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
