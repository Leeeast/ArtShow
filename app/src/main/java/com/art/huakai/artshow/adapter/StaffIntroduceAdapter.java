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
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.WorksDetailBean;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.List;

/**
 * Created by lining on 17-9-7.
 */
public class StaffIntroduceAdapter extends  RecyclerView.Adapter{

    private static final String TAG="ProfessionalPersonAdapt";
    private List<WorksDetailBean.StaffsBean> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public StaffIntroduceAdapter(Context context, List<WorksDetailBean.StaffsBean> list) {
        this.list = list;
        this.mContext = context;

    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.staff_introduce_item, parent, false);
            TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
            return typeOneViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeOneViewHolder){
            TypeOneViewHolder typeOneViewHolder= (TypeOneViewHolder) holder;
//            typeOneViewHolder.chinaShowImageView.setImageResource(R.mipmap.test);
            if(list.get(position)!=null){
                if(!TextUtils.isEmpty(list.get(position).getPhoto())){
                    typeOneViewHolder.chinaShowImageView.setImageURI(Uri.parse(list.get(position).getPhoto()));
                }
                typeOneViewHolder.tv_actor_name.setText(list.get(position).getName());
                typeOneViewHolder.tv_actor_major.setText(list.get(position).getRoleName());
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
        private TextView tv_actor_name;
        private TextView tv_actor_major;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_actor_name= (TextView) itemView.findViewById(R.id.tv_actor_name);
            tv_actor_major= (TextView) itemView.findViewById(R.id.tv_actor_major);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
        }
    }




    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }


}
