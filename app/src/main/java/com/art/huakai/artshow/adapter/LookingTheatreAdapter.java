package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.net.Uri;
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
 * Created by lining on 17-10-7.
 */
public class LookingTheatreAdapter extends  RecyclerView.Adapter{

    private static final String TAG="LookingTheatreAdapter";

    private ArrayList<String> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public LookingTheatreAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.mContext = context;

    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.looking_theatre_item, parent, false);
            TypeOneViewHolder typeOneViewHolder = new TypeOneViewHolder(view);
            return typeOneViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeOneViewHolder){
            TypeOneViewHolder typeOneViewHolder= (TypeOneViewHolder) holder;
//            typeOneViewHolder.chinaShowImageView.setImageResource(R.mipmap.test);
            typeOneViewHolder.chinaShowImageView.setImageURI(Uri.parse("asset:///test.png"));
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
        private TextView tv_thratre_name;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        private TextView tv_seat_number;
        private TextView tv_thratre_location;
        private TextView tv_thratre_rent;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_thratre_name= (TextView) itemView.findViewById(R.id.tv_thratre_name);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
            tv_seat_number= (TextView) itemView.findViewById(R.id.tv_seat_number);
            tv_thratre_location= (TextView) itemView.findViewById(R.id.tv_thratre_location);
            tv_thratre_rent= (TextView) itemView.findViewById(R.id.tv_thratre_rent);
        }
    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }


}
