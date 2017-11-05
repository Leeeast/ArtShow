package com.art.huakai.artshow.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.dialog.ConfirmDialog;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 设置页面
 * Created by lidongliang on 2017/10/13.
 */
public class NavigationActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_theatre_name)
    TextView tvTheatreName;
    @BindView(R.id.tv_theatre_location)
    TextView tvTheatreLocation;
    @BindView(R.id.iv_goto_address)
    ImageView ivGotoAddress;
    private Unbinder mUnbinder;

    private String theatreName;
    private String toLatitude;
    private String toLongitude;
    private String theatreLocation;


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_navigation;
    }

    @Override
    public void initData() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        ivGotoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(TextUtils.isEmpty(toLatitude)||TextUtils.isEmpty(toLongitude)){
                   navigation();
               }else{

               }
            }
        });
    }

    @Override
    public void setView() {
        tvTheatreName.setText(theatreName);
        tvTheatreLocation.setText(theatreLocation);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void back() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        Intent intent=getIntent();
        toLatitude=intent.getStringExtra("toLatitude");
        toLongitude=intent.getStringExtra("toLongitude");
        theatreName=intent.getStringExtra("theatreName");
        theatreLocation=intent.getStringExtra("theatreLocation");

    }

    private void navigation(){
        try {
            if(isAvilible(NavigationActivity.this, "com.baidu.BaiduMap")){
                Intent i1 = new Intent();
                // 驾车路线规划
                i1.setData(Uri.parse("baidumap://map/direction?destination="));
                startActivity(i1);
                return;
            }else if(isAvilible(NavigationActivity.this, "com.autonavi.minimap")){
                Intent intent = new Intent();
                // 驾车路线规划
                intent = Intent.getIntent("androidamap://route");
                startActivity(intent);
                return;
            }

//            intent = Intent.getIntent("androidamap://route?sname=我的位置"+"&dev=0&m=0&t=1"+"&dlat="+"39.98871"+"&dlon="+"116.43234");
            Intent i1 = new Intent();
            // 驾车路线规划
            i1.setData(Uri.parse("baidumap://map/direction?destination="));
            startActivity(i1);
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
