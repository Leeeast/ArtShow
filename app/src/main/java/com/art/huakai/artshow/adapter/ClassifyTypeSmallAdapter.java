package com.art.huakai.artshow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.ClassifyTypeBigHolder;
import com.art.huakai.artshow.adapter.holder.ClassifyTypeSmallHolder;
import com.art.huakai.artshow.adapter.holder.EmptyHolder;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.ClassifyTypeBean;
import com.art.huakai.artshow.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/5.
 */

public class ClassifyTypeSmallAdapter extends RecyclerView.Adapter {
    private List<ClassifyTypeBean> mlist;
    private List<ClassifyTypeBean> mClassifyTypeAdded;
    private ClassifyTypeBean mClassifyTypeSmallAdded;
    private Toast toast;

    public ClassifyTypeSmallAdapter(List<ClassifyTypeBean> list, List<ClassifyTypeBean> classifyTypeAdded) {
        this.mlist = list;
        this.mClassifyTypeAdded = classifyTypeAdded;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify_type_small, parent, false);
        return new ClassifyTypeSmallHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassifyTypeSmallHolder) {
            ClassifyTypeSmallHolder classifyTypeSmallHolder = (ClassifyTypeSmallHolder) holder;
            final ClassifyTypeBean classifyTypeSmall = mlist.get(position);
            classifyTypeSmallHolder.chkClassifyType.setText(classifyTypeSmall.getName());
            classifyTypeSmallHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClassifyTypeAdded.contains(classifyTypeSmall)) {
                        mClassifyTypeAdded.remove(classifyTypeSmall);
                    } else {
                        if (mClassifyTypeAdded.size() < 3) {
                            mClassifyTypeAdded.add(classifyTypeSmall);
                        } else {
                            showToast();
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            if (mClassifyTypeAdded.contains(classifyTypeSmall)) {
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
