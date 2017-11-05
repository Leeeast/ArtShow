package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.Text;
import com.art.huakai.artshow.R;

import java.util.ArrayList;
import java.util.List;


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
    TextView imageView;


    public  void setParams(String theatreName,String toLatitude,String toLongitude,String theatreLocation){
     this.theatreName=theatreName;
     this.toLatitude=toLatitude;
     this.toLongitude=toLongitude;
     this.theatreLocation=theatreLocation;
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

        imageView= (TextView) view.findViewById(R.id.tv_navigation);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation();
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {


    }


//    private void navigation(){
//        if(true){
//            Intent i1 = new Intent();
//            // 驾车路线规划
//            i1.setData(Uri.parse("baidumap://map/direction?destination=name:对外经贸大学|latlng:39.98871,116.43234&mode=driving"));
//            mContext.startActivity(i1);
//        }else{
//            Intent i1 = new Intent();
//            // 驾车路线规划
//            i1.setData(Uri.parse("http://uri.amap.com/navigation?from=116.478346,39.997361,西直门&to=116.3246,39.966577,北京大学&mode=car"));
//            mContext.startActivity(i1);
//        }
//    }


    private void navigation(){
        try {
            if(isAvilible(mContext, "com.baidu.BaiduMap")){
                Intent i1 = new Intent();
                // 驾车路线规划
                String str="baidumap://map/direction?destination="+toLatitude+","+toLongitude;
                i1.setData(Uri.parse(str));
                mContext.startActivity(i1);
                return;
            }else if(isAvilible(mContext, "com.autonavi.minimap")){
                String str="androidamap://route?sname=我的位置"+"&dev=0&m=0&t=1"+"&dlat="+toLatitude+"&dlon="+toLongitude;
                Intent intent = new Intent();
                // 驾车路线规划
                intent = Intent.getIntent(str);
                mContext.startActivity(intent);
                return;
            }

//            intent = Intent.getIntent("androidamap://route?sname=我的位置"+"&dev=0&m=0&t=1"+"&dlat="+"39.98871"+"&dlon="+"116.43234");
            Intent i1 = new Intent();
            // 驾车路线规划
            i1.setData(Uri.parse("baidumap://map/direction?destination="));
            mContext.startActivity(i1);
        }catch (Exception e){

        }
    }


    private boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }



}
