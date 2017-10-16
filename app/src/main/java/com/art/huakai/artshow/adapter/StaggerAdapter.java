package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.List;

/**
 * Created by lining on 2017/10/10.
 */
public class StaggerAdapter extends RecyclerView.Adapter<StaggerAdapter.MyHolder>  {

   private static final String TAG="StaggerAdapter";

    private List<Integer> datas;
    private Context context;
    private int resouce_id;
    private int itemWidth;


    public StaggerAdapter(List<Integer> data, Context context, int resouce_id,int itemWidth) {
        this.datas = data;
        this.context = context;
        this.resouce_id = resouce_id;
        this.itemWidth=itemWidth;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resouce_id,null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        final LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) holder.chinaShowImageView.getLayoutParams();
        holder.chinaShowImageView.setImage(Uri.parse("res:///"+datas.get(position)),context, new ChinaShowImageView.ImgScaleResultListener() {
            @Override
            public void imgSize(int width, int height) {

                Log.e(TAG, "imgSize: width=="+width+"--height=="+height );
                lp.width=itemWidth;
                lp.height=height/width*itemWidth;
                holder.chinaShowImageView.setLayoutParams(lp);
            }
        },holder.chinaShowImageView);




//        holder.img.setLayoutParams(lp);
//        holder.img.setImageResource(R.mipmap.abc);




    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ChinaShowImageView chinaShowImageView;

        public MyHolder(View view) {
            super(view);
            chinaShowImageView = (ChinaShowImageView) view.findViewById(R.id.csiv);
        }
    }


}
