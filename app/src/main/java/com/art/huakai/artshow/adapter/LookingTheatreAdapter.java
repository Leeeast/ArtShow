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
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class LookingTheatreAdapter extends  RecyclerView.Adapter{

    private static final String TAG="LookingTheatreAdapter";

    private List<Theatre> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public LookingTheatreAdapter(Context context, List<Theatre> list) {
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
            if(position==0){
                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) typeOneViewHolder.ll_whole.getLayoutParams();
                layoutParams.setMargins((int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX),(int)mContext.getResources().getDimension(R.dimen.DIMEN_15PX));
                typeOneViewHolder.ll_whole.setLayoutParams(layoutParams);
            }
            if(list.get(position)!=null){
                Theatre theatre=list.get(position);
                if(!TextUtils.isEmpty(theatre.getLogo())){
                    typeOneViewHolder.chinaShowImageView.setSpecificSizeImageUrl(list.get(position).getLogo(),mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_144PX),mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_163PX));
                }
                typeOneViewHolder.tv_thratre_name.setText(theatre.getName());
                typeOneViewHolder.tv_seat_number.setText(theatre.getSeatingDescpt());
                typeOneViewHolder.tv_thratre_location.setText(theatre.getRegionName());
                typeOneViewHolder.tv_thratre_rent.setText(theatre.getExpenseDescpt());
                typeOneViewHolder.tv_unit.setText(theatre.getExpenseUnit());
            }
            typeOneViewHolder.ll_whole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        Log.e(TAG, "onClick: 2222" );
                        onItemClickListener.onItemClickListener(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(list!=null&&list.size()>0){
//            Log.e(TAG, "getItemCount: size=="+list.size() );
            return list.size();
        }
//        Log.e(TAG, "getItemCount: 00000" );
        return 0;
    }


    public class TypeOneViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_thratre_name;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        private TextView tv_seat_number;
        private TextView tv_thratre_location;
        private TextView tv_thratre_rent;
        private TextView tv_unit;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_thratre_name= (TextView) itemView.findViewById(R.id.tv_thratre_name);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
            tv_seat_number= (TextView) itemView.findViewById(R.id.tv_seat_number);
            tv_thratre_location= (TextView) itemView.findViewById(R.id.tv_thratre_location);
            tv_thratre_rent= (TextView) itemView.findViewById(R.id.tv_thratre_rent);
            tv_unit= (TextView) itemView.findViewById(R.id.tv_unit);
        }
    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void add(ArrayList<Theatre> theatres){
        int lastIndex = this.list.size();
        if (this.list.addAll(theatres)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void notifyDataSetChange(ArrayList<Theatre> theatres){
        list.clear();
        if(this.list.addAll(theatres)){
            notifyDataSetChanged();
        }
    }


}
