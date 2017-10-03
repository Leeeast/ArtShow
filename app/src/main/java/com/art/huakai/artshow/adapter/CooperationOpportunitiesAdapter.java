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

import java.util.ArrayList;

/**
 * Created by lining on 17-9-7.
 */
public class CooperationOpportunitiesAdapter extends  RecyclerView.Adapter{

    private static final String TAG="IndustryNewsAdapter";

    private ArrayList<String> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public CooperationOpportunitiesAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.mContext = context;

    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: 111111" );
            View view = LayoutInflater.from(mContext).inflate(R.layout.type_two_one_item, parent, false);
            TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
            return typeOneViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: position=="+position );
        if (holder instanceof TypeOneViewHolder){
            TypeOneViewHolder typeOneViewHolder= (TypeOneViewHolder) holder;
            typeOneViewHolder.ll_whole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        Log.e(TAG, "onClick: 2222" );
                        Toast.makeText(mContext,""+position,Toast.LENGTH_LONG).show();
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
        private TextView tv_name;
        private TextView tv_detail;
        private TextView tv_update_time;
        private LinearLayout ll_whole;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_detail= (TextView) itemView.findViewById(R.id.tv_detail);
            tv_update_time= (TextView) itemView.findViewById(R.id.tv_update_time);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
        }
    }




    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }


}
