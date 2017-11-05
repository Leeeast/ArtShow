package com.art.huakai.artshow.activity;


import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.PoiItem;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.LookingWorksAdapter;
import com.art.huakai.artshow.adapter.PioSearchAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PioSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, View.OnClickListener, SmartRecyclerview.LoadingListener {


    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private PoiSearch poiSearch;
    private PoiItem mPoi;
    PoiSearch.Query query;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private String location = "朝阳";
    private ArrayList<com.amap.api.services.core.PoiItem> poiItems = new ArrayList<com.amap.api.services.core.PoiItem>();
    private LinearLayoutManager linearlayoutManager;
    private PioSearchAdapter pioSearchAdapter;
    private boolean isLoadingMore = false;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
            }
        }
    };
    private Runnable searchRunnable;


    private void setData() {

        linearlayoutManager = new LinearLayoutManager(PioSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        pioSearchAdapter = new PioSearchAdapter(PioSearchActivity.this, poiItems);
        recyclerView.setAdapter(pioSearchAdapter);
        pioSearchAdapter.setOnItemClickListener(new PioSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent();
                intent.putExtra(TheatreBaseActivity.RESULT_ADDRESS, poiItems.get(position));
                setResult(JumpCode.FLAG_RES_ADDRESS_RESULT, intent);
                finish();
            }
        });
    }

    @Override
    public void immerseStatusBar() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_pio_search;
    }

    @Override
    public void initData() {

        searchRunnable = new Runnable() {
            @Override
            public void run() {
                doSearchQuery();
                ;
            }
        };
//        doSearchQuery();

    }

    protected void doSearchQuery() {

        if (TextUtils.isEmpty(location)) return;
        query = new PoiSearch.Query(location, "", "北京");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void initView() {

        recyclerView.setLoadingListener(this);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                location = edtSearch.getText().toString().trim();
                currentPage = 0;
                uiHandler.removeCallbacks(searchRunnable);
                uiHandler.postDelayed(searchRunnable, 500);
            }
        });
    }

    @Override
    public void setView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.tip_theatre_select_address);
        llyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        Toast.makeText(PioSearchActivity.this, rCode + "", Toast.LENGTH_SHORT).show();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            poiResult = result;
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条

                    Log.e(TAG, "onPoiSearched: 是同一条搜索");
                    for (int i = 0; i < poiItems.size(); i++) {
                        Log.e(TAG, "onPoiSearched: " + poiItems.get(i).getDirection() + "---" + poiItems.get(i).getAdName() + "--" + poiItems.get(i).getBusinessArea());
                        Log.d(TAG, "onPoiSearched: getAdName==" + poiItems.get(i).toString());
                        Log.e(TAG, "onPoiSearched: " + poiItems.get(i).getLatLonPoint().getLatitude() + "--" + poiItems.get(i).getLatLonPoint().getLongitude());
                    }
                    if (poiItems.size() == 0) {
                        poiItems = poiResult.getPois();
                        if (poiItems.size() > 0) {
                            uiHandler.sendEmptyMessage(0);
                            return;
                        } else {
                            Log.e(TAG, "onPoiSearched: 初次加载数据失败");
                        }
                    } else {
                        if (poiResult.getPois().size() > 0) {
                            pioSearchAdapter.add(poiResult.getPois());
                        } else {
                            Log.e(TAG, "onPoiSearched: 已无更多数据");
                        }
                    }
                } else {
                    poiItems.clear();
                    poiItems = poiResult.getPois();
                    if (poiItems.size() > 0) {
                        uiHandler.sendEmptyMessage(0);
                        return;
                    }
                    Log.e(TAG, "onPoiSearched: 初次加载数据失败");
                }
            }
        } else {
            if (isLoadingMore) {
                Log.e(TAG, "onPoiSearched: 加载更多失败");
            } else {
                Log.e(TAG, "onPoiSearched: 初次加载失败");
            }
        }
    }

    @Override
    public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        doSearchQuery();
    }

    @Override
    public void onLoadMore() {
        isLoadingMore = true;
        doSearchQuery();
    }

    @Override
    public void onClick(View v) {

    }
}