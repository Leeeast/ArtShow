package com.art.huakai.artshow.activity;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.fence.PoiItem;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.InfoWinAdapter;
import com.art.huakai.artshow.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapShowActivity extends Activity {


    MapView mMapView = null;
    Marker marker;
    MarkerOptions markerOption;
    LatLng latLng = new LatLng(39.906901,116.397972);
    AMap aMap;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);
//获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapview);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
//初始化地图控制器对象
        aMap = mMapView.getMap();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

        addMarker();

        aMap.setInfoWindowAdapter(new InfoWinAdapter(this));


    }

    private void addMarker(){
        markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.mipmap.icon_address)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
        marker = aMap.addMarker(markerOption);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                marker.showInfoWindow();

            }
        },500);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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









}