package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.utils.DateUtil;

import java.util.List;

/**
 * Created by lining on 17-9-7.
 */
public class CooperationOpportunitiesAdapter extends  RecyclerView.Adapter{

    private static final String TAG="IndustryNewsAdapter";

    private List<EnrollInfo> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public CooperationOpportunitiesAdapter(Context context, List<EnrollInfo> list) {
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
            if(list.get(position)!=null){

                typeOneViewHolder.tv_name.setText(list.get(position).title);
                typeOneViewHolder.tv_detail.setText(list.get(position).description);
                typeOneViewHolder.tv_update_time.setText(list.get(position).endTime+"");

                typeOneViewHolder.tv_update_time.setText("截止时间："+DateUtil.transTime(list.get(position).endTime+"","yyyy年MM月dd日"));
                if(list.get(position).enrollReceiving){
                    typeOneViewHolder.tv_status.setText("报名中");
                    typeOneViewHolder.tv_status.setTextColor(0xffe93c2c);
                }else{
                    typeOneViewHolder.tv_status.setText("已结束");
                    typeOneViewHolder.tv_status.setTextColor(0xff333333);
                }
            }

            typeOneViewHolder.rl_whole.setOnClickListener(new View.OnClickListener() {
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

            return list.size();
        }

        return 0;
    }


    public class TypeOneViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_detail;
        private TextView tv_update_time;
        private LinearLayout ll_whole;
        private RelativeLayout rl_whole;
        private TextView tv_status;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_detail= (TextView) itemView.findViewById(R.id.tv_detail);
            tv_update_time= (TextView) itemView.findViewById(R.id.tv_update_time);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
            rl_whole= (RelativeLayout) itemView.findViewById(R.id.rl_whole);
            tv_status= (TextView) itemView.findViewById(R.id.tv_status);
        }
    }




    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }


}
