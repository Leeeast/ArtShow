package com.art.huakai.artshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectNotifyEvent;
import com.art.huakai.artshow.eventbus.TalentNotifyEvent;
import com.art.huakai.artshow.eventbus.TheatreNotifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class StaggerFragment extends HeaderViewPagerFragment {
    public static final String PARAMS_LIST = "PARAMS_LIST";

    private String TAG = "StaggerFragment";
    private RecyclerView mRecyclerView;
    private ArrayList<PicturesBean> lists = new ArrayList<PicturesBean>();
    private StaggerAdapter adpter;
    private int itemWidth;
    private SpacesItemDecoration decoration;
    private ArrayList<String> picList = new ArrayList<String>();


    public static StaggerFragment newInstance(ArrayList<PicturesBean> picturesBeans) {
        StaggerFragment staggerFragment = new StaggerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PARAMS_LIST, picturesBeans);
        staggerFragment.setArguments(bundle);
        return staggerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            lists = getArguments().getParcelableArrayList(PARAMS_LIST);
        }
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
        Log.d(TAG, "initData: lists.size()=="+lists.size());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTalentPhoto(TalentNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        TalentDetailInfo t = TalentDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TalentNotifyEvent.NOTIFY_PHOTO:
                lists.clear();
                lists.addAll(t.getPictures());
                adpter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTheatrePhoto(TheatreNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        TheatreDetailInfo t = TheatreDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TheatreNotifyEvent.NOTIFY_THEATRE_PHOTO:
                lists.clear();
                lists.addAll(t.getPictures());
                adpter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProjectPhoto(ProjectNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        ProjectDetailInfo p = ProjectDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case ProjectNotifyEvent.NOTIFY_PHOTO:
                lists.clear();
                lists.addAll(p.getPictures());
                adpter.notifyDataSetChanged();
                break;
        }
    }
}
