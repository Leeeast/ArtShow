package com.art.huakai.artshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.BroswerPicActivity;
import com.art.huakai.artshow.adapter.StaggerAdapter;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.decoration.SpacesItemDecoration;
import com.art.huakai.artshow.entity.PicturesBean;

import java.util.ArrayList;
import java.util.List;


public class StaggerFragment extends HeaderViewPagerFragment {

    private String TAG = "StaggerFragment";
    private RecyclerView mRecyclerView;
    private ArrayList<PicturesBean> lists = new ArrayList<PicturesBean>();
    private StaggerAdapter adpter;
    private int itemWidth;
    private SpacesItemDecoration decoration;
    private ArrayList<String> picList = new ArrayList<String>();


    public static StaggerFragment newInstance() {
        return new StaggerFragment();
    }


    public void setLists(ArrayList<PicturesBean> lists) {
        this.lists = lists;
//        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stagger_show_pic, container, false);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        itemWidth = (metric.widthPixels - (int) getResources().getDimension(R.dimen.DIMEN_20PX) * 2) / 2;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initData();
        return view;
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    private void initData() {

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adpter = new StaggerAdapter(lists, getContext(), itemWidth);
        decoration = new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX));
        mRecyclerView.addItemDecoration(decoration);
        adpter.setOnItemClickListener(new StaggerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Log.e(TAG, "onItemClickListener: position==" + position);
                Intent intent = new Intent();
                intent.setClass(getContext(), BroswerPicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", picList);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adpter);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "run: heigth=="+mRecyclerView.getHeight() );
//                if(mRecyclerView.getHeight()<1280){
//                    RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1280);
//                    mRecyclerView.setLayoutParams(lp);
//                }
//
//            }
//        },300);


        for (int i = 0; i < lists.size(); i++) {

            picList.add(lists.get(i).getMasterUrl());

        }

    }


}
