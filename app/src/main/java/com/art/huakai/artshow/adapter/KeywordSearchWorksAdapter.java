package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.Work;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class KeywordSearchWorksAdapter extends  RecyclerView.Adapter{

    private static final String TAG="LookingWorksAdapter";

    private List<Work> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



        public KeywordSearchWorksAdapter(Context context, List<Work> list) {
            this.list = list;
            this.mContext = context;

    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.looking_works_item, null);
            TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
            return typeOneViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeOneViewHolder){
            TypeOneViewHolder typeOneViewHolder= (TypeOneViewHolder) holder;
            if(position==0){
                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) typeOneViewHolder.ll_whole.getLayoutParams();
                layoutParams.setMargins((int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX));
                typeOneViewHolder.ll_whole.setLayoutParams(layoutParams);
            }
            if(list.get(position)!=null){
                Work work=list.get(position);
                if(!TextUtils.isEmpty(work.getLogo())){
                    typeOneViewHolder.chinaShowImageView.setImageURI(Uri.parse(work.getLogo()));
                }
                typeOneViewHolder.tv_actor_number.setText(work.getPeopleNum()+"");

                try {
                    if(0!=list.get(position + 1).getCreateTime()){
                        typeOneViewHolder.tv_show_time.setText(DateUtil.transTime(work.getPremiereTime()+"","yyyy.MM.dd"));
                    }
                }catch (Exception e){

                }
                typeOneViewHolder.tv_works_fee.setText(work.getExpense()+"");
                typeOneViewHolder.tv_works_name.setText(work.getTitle());
            }
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
        private TextView tv_works_name;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        private TextView tv_actor_number;
        private TextView tv_show_time;
        private TextView tv_works_fee;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_works_name= (TextView) itemView.findViewById(R.id.tv_works_name);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
            tv_actor_number= (TextView) itemView.findViewById(R.id.tv_actor_number);
            tv_show_time= (TextView) itemView.findViewById(R.id.tv_show_time);
            tv_works_fee= (TextView) itemView.findViewById(R.id.tv_works_fee);
        }
    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void add(ArrayList<Work> works){
        int lastIndex = this.list.size();
        if (this.list.addAll(works)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void notifyDataSetChange(List<Work> works){
        list.clear();
        if(this.list.addAll(works)){
            notifyDataSetChanged();
        }
    }




}
