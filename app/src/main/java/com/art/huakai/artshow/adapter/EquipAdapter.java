package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.ClassifyTypeSmallHolder;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.ClassifyTypeBean;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class EquipAdapter extends RecyclerView.Adapter {
    private List<String> mlist;
    private List<String> mTechParamAdded;
    private ClassifyTypeBean mClassifyTypeSmallAdded;
    private Toast toast;

    public EquipAdapter(List<String> list, List<String> classifyTypeAdded) {
        this.mlist = list;
        this.mTechParamAdded = classifyTypeAdded;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_type_small, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height=view.getContext().getResources().getDimensionPixelSize(R.dimen.DIMEN_100PX);
        return new ClassifyTypeSmallHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassifyTypeSmallHolder) {
            ClassifyTypeSmallHolder classifyTypeSmallHolder = (ClassifyTypeSmallHolder) holder;
            final String techParam = mlist.get(position);
            classifyTypeSmallHolder.chkClassifyType.setText(techParam);
            classifyTypeSmallHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTechParamAdded.contains(techParam)) {
                        mTechParamAdded.remove(techParam);
                    } else {
                        mTechParamAdded.add(techParam);
                    }
                    notifyDataSetChanged();
                }
            });
            if (mTechParamAdded.contains(techParam)) {
                classifyTypeSmallHolder.chkClassifyType.setChecked(true);
            } else {
                classifyTypeSmallHolder.chkClassifyType.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    //显示Toast防止一直不断提示
    public void showToast() {
        if (toast == null) {
            toast = Toast.makeText(ShowApplication.getAppContext(),
                    ShowApplication.getAppContext().getString(R.string.tip_talent_classify_type),
                    Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
