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
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class KeywordSearchNewsAdapter extends  RecyclerView.Adapter{

    private static final String TAG="LookingTheatreAdapter";

    private List<NewsesBean> list ;
    private Context mContext;
    private OnItemClickListener onItemClickListener;



    public KeywordSearchNewsAdapter(Context context, List<NewsesBean> list) {
        this.list = list;
        this.mContext = context;
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.search_news_item, parent, false);
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
                NewsesBean newsesBean=list.get(position);
                if(!TextUtils.isEmpty(newsesBean.getLogo())){
                    typeOneViewHolder.chinaShowImageView.setImageURI(Uri.parse(newsesBean.getLogo()));
                }
                typeOneViewHolder.tv_news_title.setText(newsesBean.getTitle());
                typeOneViewHolder.tv_news_time.setText(newsesBean.getCreateTime()+"");
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
        private TextView tv_news_title;
        private ChinaShowImageView chinaShowImageView;
        private LinearLayout ll_whole;
        private TextView tv_news_time;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            tv_news_title= (TextView) itemView.findViewById(R.id.tv_news_title);
            chinaShowImageView= (ChinaShowImageView) itemView.findViewById(R.id.sdv);
            ll_whole= (LinearLayout) itemView.findViewById(R.id.ll_whole);
            tv_news_time= (TextView) itemView.findViewById(R.id.tv_news_time);
        }
    }

    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void add(ArrayList<NewsesBean> theatres){
        int lastIndex = this.list.size();
        if (this.list.addAll(theatres)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }
//
    public void notifyDataSetChange(List<NewsesBean> theatres){
        list.clear();
        if(this.list.addAll(theatres)){
            notifyDataSetChanged();
        }
    }


}
