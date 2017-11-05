package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    private String fromLatitude;
    private String fromLongitude;
    private String theatreName;
    private String toLatitude;
    private String toLongitude;
    private String theatreLocation;



    private Context mContext;
    ImageView imageView;


    private void setParams(String fromLatitude,String fromLongitude){


    }


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


            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {


    }


    private void navigation(){
        if(true){
            Intent i1 = new Intent();
            // 驾车路线规划
            i1.setData(Uri.parse("baidumap://map/direction?destination=name:对外经贸大学|latlng:39.98871,116.43234&mode=driving"));
            mContext.startActivity(i1);
        }else{
            Intent i1 = new Intent();
            // 驾车路线规划
            i1.setData(Uri.parse("http://uri.amap.com/navigation?from=116.478346,39.997361,西直门&to=116.3246,39.966577,北京大学&mode=car"));
            mContext.startActivity(i1);
        }
    }






}
