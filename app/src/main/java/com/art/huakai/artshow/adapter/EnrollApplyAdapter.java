package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.EnrollApplyAddHolder;
import com.art.huakai.artshow.adapter.holder.EnrollApplyHolder;
import com.art.huakai.artshow.entity.EnrollDetailInfo;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.utils.DateUtil;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EnrollApplyAdapter extends RecyclerView.Adapter {
    public static final int TYPE_NORMAL = 11;
    public static final int TYPE_ADD = 12;
    private List<RepertorysBean> mRepertorys;
    private List<RepertorysBean> mRepertorysAdded;
    private OnItemClickListener mOnItemClickListener;
    //控制单选项
    private EnrollDetailInfo mEnrollDetailInfo;

    public EnrollApplyAdapter(List<RepertorysBean> list, List<RepertorysBean> repertorysAdded, EnrollDetailInfo enrollDetailInfo) {
        this.mRepertorys = list;
        this.mRepertorysAdded = repertorysAdded;
        this.mEnrollDetailInfo = enrollDetailInfo;
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
                final RepertorysBean repertorysBean = mRepertorys.get(position);
                enrollApplyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mRepertorysAdded.contains(repertorysBean)) {
                            mRepertorysAdded.remove(repertorysBean);
                        } else {
                            if (mEnrollDetailInfo.enroll.numberLimit == 1) {
                                mRepertorysAdded.clear();
                                mRepertorysAdded.add(repertorysBean);
                            } else {
                                if (mRepertorysAdded.size() < mEnrollDetailInfo.enroll.numberLimit) {
                                    mRepertorysAdded.add(repertorysBean);
                                } else {
                                    String limitTip = String.format(enrollApplyHolder.itemView.getContext().getString(R.string.tip_enroll_limit),
                                            mEnrollDetailInfo.enroll.numberLimit);
                                    Toast.makeText(
                                            enrollApplyHolder.itemView.getContext(), limitTip, Toast.LENGTH_SHORT
                                    ).show();
                                }

                            }

                        }
                        notifyDataSetChanged();
                    }
                });
                if (mRepertorysAdded.contains(repertorysBean)) {
                    enrollApplyHolder.chkEnrollApply.setChecked(true);
                    enrollApplyHolder.rLyRootTheatre.setSelected(true);
                } else {
                    enrollApplyHolder.chkEnrollApply.setChecked(false);
                    enrollApplyHolder.rLyRootTheatre.setSelected(false);
                }
                if (repertorysBean.getStatus() == 1) {
                    enrollApplyHolder.rLyRootTheatre.setEnabled(true);
                    enrollApplyHolder.tvProjectStatus.setVisibility(View.GONE);
                    enrollApplyHolder.chkEnrollApply.setVisibility(View.VISIBLE);
                } else {
                    enrollApplyHolder.rLyRootTheatre.setEnabled(false);
                    enrollApplyHolder.tvProjectStatus.setVisibility(View.VISIBLE);
                    enrollApplyHolder.chkEnrollApply.setVisibility(View.GONE);
                }
                enrollApplyHolder.sdvTheatre.setImageURI(repertorysBean.getLogo());
                enrollApplyHolder.tvTheatreName.setText(repertorysBean.getTitle());
                enrollApplyHolder.tvSeatCount.setText(String.valueOf(repertorysBean.getPeopleNum()));
                enrollApplyHolder.tvFirstShowTime.setText(DateUtil.transTime(String.valueOf(repertorysBean.getPremiereTime()), "yy.MM.dd"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        //多了一个添加选项，所以+1
        return mRepertorys.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mRepertorys == null || mRepertorys.size() == 0 || position == mRepertorys.size()) {
            return TYPE_ADD;
        } else {
            return TYPE_NORMAL;
        }
    }
}
