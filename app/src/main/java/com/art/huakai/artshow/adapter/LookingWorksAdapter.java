package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.entity.Work;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class LookingWorksAdapter extends RecyclerView.Adapter {

    private static final String TAG = "LookingWorksAdapter";

    private List<Work> list;
    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public LookingWorksAdapter(Context context, List<Work> list) {
        this.list = list;
        this.mContext = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.looking_works_item, parent, false);
        TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
        return typeOneViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeOneViewHolder) {
            TypeOneViewHolder typeOneViewHolder = (TypeOneViewHolder) holder;
            if (position == 0) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) typeOneViewHolder.ll_whole.getLayoutParams();
                layoutParams.topMargin = typeOneViewHolder.itemView.getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX);
                typeOneViewHolder.ll_whole.setLayoutParams(layoutParams);
            } else {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) typeOneViewHolder.ll_whole.getLayoutParams();
                layoutParams.topMargin = 0;
                typeOneViewHolder.ll_whole.setLayoutParams(layoutParams);
            }
            if (list.get(position) != null) {
                Work work = list.get(position);
                if (!TextUtils.isEmpty(work.getLogo())) {
                    typeOneViewHolder.chinaShowImageView.setSpecificSizeImageUrl(list.get(position).getLogo(), mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_144PX), mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_163PX));
                }
                typeOneViewHolder.tv_actor_number.setText(work.getPeopleNum() + "");
                try {
                    if (0 != list.get(position + 1).getCreateTime()) {
                        typeOneViewHolder.tv_show_time.setText(DateUtil.transTime(work.getPremiereTime() + "", "yyyy.MM.dd"));
                    }
                } catch (Exception e) {

                }
//                typeOneViewHolder.tv_show_time.setText(work.getPremiereTime()+"");

                typeOneViewHolder.tv_works_fee.setText(work.getExpenseDescpt());
                typeOneViewHolder.tv_unit.setText(work.getExpenseUnit());
                typeOneViewHolder.tv_works_name.setText(work.getTitle());
            }
            typeOneViewHolder.ll_whole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        Log.e(TAG, "onClick: 2222");
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {

            return list.size();
        }

        return 0;
    }


    public class TypeOneViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_works_name;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        private TextView tv_actor_number;
        private TextView tv_show_time;
        private TextView tv_works_fee;
        private TextView tv_unit;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_works_name = (TextView) itemView.findViewById(R.id.tv_works_name);
            chinaShowImageView = (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole = (LinearLayout) itemView.findViewById(R.id.ll_whole);
            tv_actor_number = (TextView) itemView.findViewById(R.id.tv_actor_number);
            tv_show_time = (TextView) itemView.findViewById(R.id.tv_show_time);
            tv_works_fee = (TextView) itemView.findViewById(R.id.tv_works_fee);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void add(ArrayList<Work> works) {
        int lastIndex = this.list.size();
        if (this.list.addAll(works)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void notifyDataSetChange(ArrayList<Work> works) {
        list.clear();
        if (this.list.addAll(works)) {
            notifyDataSetChanged();
        }
    }


}
