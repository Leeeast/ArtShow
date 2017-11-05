package com.art.huakai.artshow.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.widget.Toast;

import com.amap.api.fence.PoiItem;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.InfoWinAdapter;


import java.text.SimpleDateFormat;
import java.util.Date;


public class MapShowActivity extends Activity implements AMapLocationListener {

    MapView mMapView = null;
    Marker marker;
    MarkerOptions markerOption;
    LatLng latLng;
    AMap aMap;
    Handler handler=new Handler();
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private InfoWinAdapter infoWinAdapter;
    private String theatreName;
    private String toLatitude;
    private String toLongitude;
    private String theatreLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapview);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        Intent intent=getIntent();
        toLatitude=intent.getStringExtra("toLatitude");
        toLongitude=intent.getStringExtra("toLongitude");
        theatreName=intent.getStringExtra("theatreName");
        theatreLocation=intent.getStringExtra("theatreLocation");

        //初始化地图控制器对象
        aMap = mMapView.getMap();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        addMarker();
        infoWinAdapter=new InfoWinAdapter(this);
        infoWinAdapter.setParams(theatreName,toLatitude,toLongitude,theatreLocation);
        aMap.setInfoWindowAdapter(infoWinAdapter);
//        initPosition();



    }

    private void initPosition(){
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void addMarker(){

        try{
            latLng = new LatLng(Double.parseDouble(toLatitude),Double.parseDouble(toLongitude));
            markerOption = new MarkerOptions();
            markerOption.position(latLng);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),R.mipmap.theatre_location)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
            marker = aMap.addMarker(markerOption);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    marker.showInfoWindow();

                }
            },500);
        }catch (Exception e){

        }


    }



    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
//                infoWinAdapter.setLocation(amapLocation.getLatitude()+"",amapLocation.getLongitude()+"");
                amapLocation.getDistrict();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                Toast.makeText(MapShowActivity.this,amapLocation.toString(),Toast.LENGTH_SHORT).show();
                mlocationClient.stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(MapShowActivity.this,amapLocation.getErrorInfo(),Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

    }
}