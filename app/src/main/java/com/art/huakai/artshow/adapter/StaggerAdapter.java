package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.PicturesBean;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.List;

/**
 * Created by lining on 2017/10/10.
 */
public class StaggerAdapter extends RecyclerView.Adapter<StaggerAdapter.MyHolder> {

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
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stagger_item, null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        if (!TextUtils.isEmpty(datas.get(position).getMasterUrl())) {
            try {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, (int) (datas.get(position).getHeight() / datas.get(position).getWidth() * itemWidth));
                holder.chinaShowImageView.setLayoutParams(lp);
                holder.chinaShowImageView.setImageURI(Uri.parse(datas.get(position).getMasterUrl()));
            } catch (Exception e) {
                e.printStackTrace();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
                holder.chinaShowImageView.setLayoutParams(lp);
                holder.chinaShowImageView.setImageURI(Uri.parse(datas.get(position).getMasterUrl()));

            }
//            holder.chinaShowImageView.setImageURI(Uri.parse(datas.get(position).getMasterUrl()));
//            holder.chinaShowImageView.setImage(Uri.parse(datas.get(position).getMasterUrl()), context, new ChinaShowImageView.ImgScaleResultListener() {
//                @Override
//                public void imgSize(int width, int height) {
//                    float a = height;
//                    Log.e(TAG, "imgSize: width==" + width + "--height==" + height);
//                    Log.e(TAG, "imgSize:cutWidth== " + itemWidth + "--cutHeigth==" + a / width * itemWidth);
//
//                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, (int) (a / width * itemWidth));
//                    holder.chinaShowImageView.setLayoutParams(lp);
//                }
//            }, holder.chinaShowImageView);
        }
        holder.chinaShowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onItemClickListener: position==" + position);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
//        return 11;
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
