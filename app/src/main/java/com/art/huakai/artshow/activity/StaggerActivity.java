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
    ArrayList<String> data=new ArrayList<String>();
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


        data.add("https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image");
        data.add("https://www.showonline.com.cn/image/2017/08/18/a595501659764c3c8220014b04719da0.image");
        data.add("https://www.showonline.com.cn/image/2017/08/18/bc8ac1d4ee3d410ebb0a6051c76d4120.image");
        data.add("https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg");
        data.add("https://www.showonline.com.cn/image/2017/08/31/b66ea1790bb94023bebc4685b4953530@thumb.jpg");
        data.add("https://www.showonline.com.cn/image/2017/09/04/567420dd44b742b493c90751f3e8f9a1.jpg");
        data.add("https://www.showonline.com.cn/image/2017/09/04/b4532714990244ce9f35d75525f70956@thumb.jpg");
        data.add("https://www.showonline.com.cn/image/2017/08/30/3ecffee89f574fcd97f79bb888ac562a@thumb.jpg");
        data.add("https://www.showonline.com.cn/image/2017/08/02/a6b59bc64c9841928ed02a5c74f4e86d.jpg");
        data.add("https://www.showonline.com.cn/image/2017/07/22/21ab11ed86384731b5c141765ee9954c.jpg");
        data.add("https://www.showonline.com.cn/image/2017/09/17/9cd5d315931b4ad98e64d742cfc16367@thumb.jpeg");
        data.add("https://www.showonline.com.cn/image/2017/09/16/99ea670acde0411386df7b7dfd6e12dc@thumb.jpeg");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//        adpter  = new StaggerAdapter(data,StaggerActivity.this,itemWidth);
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
