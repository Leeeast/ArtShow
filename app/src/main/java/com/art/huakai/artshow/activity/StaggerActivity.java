package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.StaggerAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.SpacesItemDecoration;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class StaggerActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    StaggerAdapter adpter;
    ArrayList<Integer> data=new ArrayList<Integer>();
    SpacesItemDecoration decoration;
    private int itemWidth;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_stagger_show_pic;
    }

    @Override
    public void initData() {

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        itemWidth=(metric.widthPixels-(int) getResources().getDimension(R.dimen.DIMEN_20PX)*2)/2;


        data.add(R.mipmap.account_type_n);
        data.add(R.mipmap.chinashow_search);
        data.add(R.mipmap.bg_ad_2);
        data.add(R.mipmap.chinashow_logo);
        data.add(R.mipmap.test);
        data.add(R.mipmap.ic_launcher);
        data.add(R.mipmap.upload_data);
        data.add(R.mipmap.tab_collaborate_n);
        data.add(R.mipmap.icon_item_weixin);
        data.add(R.mipmap.seat);
        data.add(R.mipmap.tab_show_circle_p);
        data.add(R.mipmap.guide2);


//        data.add("/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-10-08-17-50-47.png");
//        data.add("/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-10-05-10-46-22.png");
//        data.add("/storage/emulated/0/DCIM/Camera/IMG_20171002_145957.jpg");
//        data.add("/storage/emulated/0/DCIM/TmallPic/1506953238259.png");
//        data.add("/storage/emulated/0/DCIM/TmallPic/1506953961442.png");
//        data.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1506706528102.jpg");
//        data.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1506829741231.jpg");
//        data.add("/storage/emulated/0/Pictures/Screenshots/Screenshot_2017-10-08-17-35-31.png");
//        data.add("/storage/emulated/0/DCIM/Camera/IMG_20171010_220553.jpg");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        adpter  = new StaggerAdapter(data,StaggerActivity.this,R.layout.stagger_item,itemWidth);
        recyclerView.setAdapter(adpter);
        decoration=new SpacesItemDecoration((int) getResources().getDimension(R.dimen.DIMEN_20PX));
        recyclerView.addItemDecoration(decoration);

    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }





}
