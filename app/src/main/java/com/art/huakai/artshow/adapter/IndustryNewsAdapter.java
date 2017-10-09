package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.art.huakai.artshow.R;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.ArrayList;

/**
 * Created by lining on 17-9-7.
 */
public class IndustryNewsAdapter extends  RecyclerView.Adapter{

    private static final String TAG="IndustryNewsAdapter";

    private ArrayList<String> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private static final int TYPE_ONE=1;
    private static final int TYPE_TWO=2;


    public IndustryNewsAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.mContext = context;

    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: 111111" );
        if(viewType==1){
            View view = LayoutInflater.from(mContext).inflate(R.layout.type_one_one_item, parent, false);
            TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
            return typeOneViewHolder;
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.type_one_two_item, parent, false);
            TypeTwoViewHolder typeTwoViewHolder = new TypeTwoViewHolder(view);
            return typeTwoViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: position=="+position );
        if (holder instanceof TypeOneViewHolder){
           TypeOneViewHolder typeOneViewHolder= (TypeOneViewHolder) holder;
           typeOneViewHolder.chinaShowImageViewOne.setImageResource(R.mipmap.test);
            typeOneViewHolder.chinaShowImageViewTwo.setImageResource(R.mipmap.test);
//            typeOneViewHolder.chinaShowImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e(TAG, "onClick: 2222" );
//                }
//            });
            typeOneViewHolder.ll_whole_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        Log.e(TAG, "onClick: 2222" );
                        Toast.makeText(mContext,""+position,Toast.LENGTH_LONG).show();
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
            typeOneViewHolder.ll_whole_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        Log.e(TAG, "onClick: 2222" );
                        Toast.makeText(mContext,""+position,Toast.LENGTH_LONG).show();
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }else if(holder instanceof TypeTwoViewHolder){
            TypeTwoViewHolder typeTwoViewHolder= (TypeTwoViewHolder) holder;
            typeTwoViewHolder.chinaShowImageView.setImageResource(R.mipmap.test);
            typeTwoViewHolder.ll_whole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        Toast.makeText(mContext,""+position,Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onClick: 11111" );
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(list!=null&&list.size()>0){
            Log.e(TAG, "getItemCount: size=="+list.size() );
            return list.size();
        }
        Log.e(TAG, "getItemCount: 00000" );
        return 0;
    }


    public class TypeOneViewHolder extends RecyclerView.ViewHolder {
        private ChinaShowImageView chinaShowImageViewOne;
        private TextView tv_name_one;
        private TextView tv_update_time_one;
        private LinearLayout ll_whole_one;
        private ChinaShowImageView chinaShowImageViewTwo;
        private TextView tv_name_two;
        private TextView tv_update_time_two;
        private LinearLayout ll_whole_two;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            chinaShowImageViewOne= (ChinaShowImageView) itemView.findViewById(R.id.sdv_one);
            tv_name_one= (TextView) itemView.findViewById(R.id.tv_name_one);
            tv_update_time_one= (TextView) itemView.findViewById(R.id.tv_update_time_one);
            ll_whole_one= (LinearLayout) itemView.findViewById(R.id.ll_whole_one);

            chinaShowImageViewTwo= (ChinaShowImageView) itemView.findViewById(R.id.sdv_two);
            tv_name_two= (TextView) itemView.findViewById(R.id.tv_name_two);
            tv_update_time_two= (TextView) itemView.findViewById(R.id.tv_update_time_two);
            ll_whole_two= (LinearLayout) itemView.findViewById(R.id.ll_whole_two);

        }
    }

    public class TypeTwoViewHolder extends RecyclerView.ViewHolder {
        private ChinaShowImageView chinaShowImageView;
        private TextView tv_name;
        private TextView tv_update_time;
        private TextView tv_subtitle;
        private LinearLayout ll_whole;
        public TypeTwoViewHolder(View itemView) {
            super(itemView);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_update_time= (TextView) itemView.findViewById(R.id.tv_update_time);
            tv_subtitle= (TextView) itemView.findViewById(R.id.tv_subtitle);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
        }
    }




    @Override
    public int getItemViewType(int position) {
        if(position<=0){
            return TYPE_ONE;
        }else{
            return TYPE_TWO;
        }
    }


//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
//        if(layoutManager instanceof GridLayoutManager){
//            final GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
//            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type=getItemViewType(position);
//                    if(type==TYPE_ONE){
//                        return 1;
//                    }else{
//                        return 2;
//                    }
//                }
//            });
//        }
//    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }


}