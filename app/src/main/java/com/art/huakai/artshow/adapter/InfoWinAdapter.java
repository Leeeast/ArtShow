package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.art.huakai.artshow.R;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private LatLng latLng;
    private String agentName;
    private String snippet;
    private Context mContext;
    ImageView imageView;

    public InfoWinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData();
        View view = initView();
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData() {
//        latLng = marker.getPosition();
//        snippet = marker.getSnippet();
//        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infowindow, null);
        imageView= (ImageView) view.findViewById(R.id.iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,"哈哈哈",Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {



//        int id = v.getId();
//        switch (id){
//            case R.id.navigation_LL:  //点击导航
//                NavigationUtils.Navigation(latLng);
//                break;
//
//            case R.id.call_LL:  //点击打电话
//                PhoneCallUtils.call("028-"); //TODO 处理电话号码
//                break;
//        }



    }

}
