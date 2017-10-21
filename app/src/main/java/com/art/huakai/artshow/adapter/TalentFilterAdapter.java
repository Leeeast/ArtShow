package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-3-16.
 */
public class TalentFilterAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> lists;
    private OnItemClickListener onItemClickListener;
    private List<Boolean> checkeds;
    private int lastActivitedPosition = -1;
    private Boolean notifyDataChanged = false;
    private static final String TAG="SingleChooseAdapter";

    public TalentFilterAdapter(Context mContext, ArrayList<String> lists, int initPosition) {
        this.mContext = mContext;
        this.lists = lists;
        addData(lists,initPosition);
    }
    public void addData(List<String> lists,int initPosition) {
        checkeds = new ArrayList<Boolean>();
        lastActivitedPosition=initPosition;
        for (int i = 0; i < lists.size(); i++) {
            if(initPosition==i){
                checkeds.add(true);
            }else{
                checkeds.add(false);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallpaper_switch_item, parent, false);
        WallpaperSwitchViewHolder myViewHolder = new WallpaperSwitchViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof WallpaperSwitchViewHolder) {
            Log.e(TAG, "onBindViewHolder: 111");
            final WallpaperSwitchViewHolder wallpaperSwitchViewHolder = (WallpaperSwitchViewHolder) holder;
            wallpaperSwitchViewHolder.tv.setText(lists.get(position));
            if (checkeds.get(position)) {
                wallpaperSwitchViewHolder.tv.setTextColor(0xffe93c2c);
                wallpaperSwitchViewHolder.tv.setBackgroundResource(R.drawable.red_rectang);
            } else {
                wallpaperSwitchViewHolder.tv.setTextColor(0xff9b9b9b);
                wallpaperSwitchViewHolder.tv.setBackgroundResource(R.drawable.grey_rectang);
            }

            wallpaperSwitchViewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if(lastActivitedPosition!=-1){
                         if(lastActivitedPosition!=position){
                             checkeds.set(lastActivitedPosition, false);
                             notifyItemChanged(lastActivitedPosition);
                         }
                     }
                     checkeds.set(position, true);
                     if(null!=onItemClickListener){
                         onItemClickListener.onItemClick( position, lists.get(position));
                     }
                     lastActivitedPosition=position;
                    wallpaperSwitchViewHolder.tv.setTextColor(0xffe93c2c);
                    wallpaperSwitchViewHolder.tv.setBackgroundResource(R.drawable.red_rectang);

                }
            });
        }
    }

    public class WallpaperSwitchViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        RelativeLayout relativeLayout;
        public WallpaperSwitchViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.rl_whole);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String url);
    }

    public void resetData(){
        if(lastActivitedPosition!=-1&&lastActivitedPosition>=0&&lastActivitedPosition<getItemCount()){
            checkeds.set(lastActivitedPosition,false);
            notifyItemChanged(lastActivitedPosition);
            lastActivitedPosition=-1;
        }
    }

    public int getLastChoosedPosition(){
        if(lastActivitedPosition!=-1&&lastActivitedPosition>=0&&lastActivitedPosition<getItemCount()){
            return lastActivitedPosition;
        }else{
            return -1;
        }
    }

}
