package com.art.huakai.artshow.activity;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.fence.PoiItem;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PioSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {


    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    private PoiSearch poiSearch;
    private PoiItem mPoi;
    PoiSearch.Query query;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数

    private String location="朝阳";


    @Override
    public void immerseStatusBar() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_pio_search;
    }

    @Override
    public void initData() {

        doSearchQuery();

    }

    protected void doSearchQuery() {

        if(TextUtils.isEmpty(location))return;
        query = new PoiSearch.Query(location, "", "北京");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void initView() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentPage=0;
                location=edtSearch.getText().toString().trim();

            }
        });
    }

    @Override
    public void setView() {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        Toast.makeText(PioSearchActivity.this,rCode+"",Toast.LENGTH_SHORT).show();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<com.amap.api.services.core.PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    for (int i = 0; i < poiItems.size(); i++) {

                        Log.e(TAG, "onPoiSearched: "+poiItems.get(i).getDirection()+"---"+poiItems.get(i).getAdName()+"--"+poiItems.get(i).getBusinessArea());
                        Log.d(TAG, "onPoiSearched: getAdName==" + poiItems.get(i).toString());
                        Log.e(TAG, "onPoiSearched: " + poiItems.get(i).getLatLonPoint().getLatitude() + "--" + poiItems.get(i).getLatLonPoint().getLongitude());

                    }
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                }
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
}