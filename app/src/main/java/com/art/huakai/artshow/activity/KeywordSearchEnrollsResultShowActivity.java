package com.art.huakai.artshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchCooperateAdapter;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lining on 2017/10/22.
 */
public class KeywordSearchEnrollsResultShowActivity extends BaseActivity implements View.OnClickListener, SmartRecyclerview.LoadingListener {


    private static final String ENROLLS = "enrolls";

    @BindView(R.id.rcv)
    SmartRecyclerview recyclerView;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_search_count)
    TextView tvSearchCount;
    @BindView(R.id.tv_search_type)
    TextView tvSearchType;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private String keyword = "";
    private String searchType = "";
    private List<EnrollInfo> enrollInfoList = new ArrayList<EnrollInfo>();
    private KeywordSearchCooperateAdapter keywordSearchCooperateAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int totalCount;
    private int page = 1;

    private void setData() {
        if(ivLoading==null)return;
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("搜索-" + keyword);
        if (searchType.equals(ENROLLS)) {
            tvSearchType.setText("合作");
                tvSearchCount.setText("共发现" + totalCount+ "条相关数据");
                keywordSearchCooperateAdapter = new KeywordSearchCooperateAdapter(enrollInfoList);
                linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearlayoutManager);
                recyclerView.setAdapter(keywordSearchCooperateAdapter);
                keywordSearchCooperateAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("PARAMS_ENROLL_ID", enrollInfoList.get(position).id);
                        invokActivity(KeywordSearchEnrollsResultShowActivity.this , EnrollDetailActivity.class, bundle, JumpCode.FLAG_REQ_ENROLL_DETAIL);
                    }
                });
        }

    }


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_search_result_show;
    }

    @Override
    public void initData() {

    }


    @Override
    public void initView() {

        llyBack.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
    }


    @Override
    public void setView() {

    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
                setData();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        Intent intent = getIntent();
        searchType = intent.getStringExtra("searchType");
        keyword = intent.getStringExtra("keyword");
        Log.e(TAG, "onCreate: searchType==" + searchType + "----keyword==" + keyword);
        getKeywordSearchAllMessage();
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
    }


    private void getKeywordSearchAllMessage() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_KEYWORD_SEARCH_NEWS==" + Constant.URL_KEYWORD_SEARCH_NEWS);
        params.put("keyword", keyword);
        params.put("page", page + "");

//        params.put("sign", sign);
//        Log.e(TAG, "getList: sign==" + sign);
//        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        String url = "";
         if (searchType.equals(ENROLLS)) {
//            合作招募
//            url = Constant.URL_KEYWORD_SEARCH_ENROLL;
             url = Constant.URL_ENROLL_PAGE;
        }
        Log.e(TAG, "getKeywordSearchAllMessage: url==" + url);
        Log.e(TAG, "getKeywordSearchAllMessage: params==" + params.toString());
        RequestUtil.request( url, params, 113, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if(ivLoading==null)return;
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ivLoading==null)return;
                        if(page==1&&enrollInfoList.size() == 0){
                            ivLoading.setVisibility(View.GONE);
                            llContent.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                },500);

                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj==" + obj);
                        Gson gson = new Gson();
                        ArrayList<EnrollInfo> tempTheatres = new ArrayList<EnrollInfo>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<EnrollInfo>>() {
                        }.getType());
                        if (tempTheatres != null && tempTheatres.size() > 0) {
                            if (enrollInfoList.size() == 0) {
                                if (enrollInfoList.addAll(tempTheatres)) {
                                    totalCount=id;
                                    uiHandler.removeCallbacksAndMessages(null);
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                            } else {
                                if (page == 1) {
                                    recyclerView.refreshComplete();
                                    enrollInfoList.clear();
                                    if (enrollInfoList.addAll(tempTheatres)) {
                                        totalCount=id;
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                } else {
                                    recyclerView.loadMoreComplete();
                                    enrollInfoList.addAll(tempTheatres);
                                    if (keywordSearchCooperateAdapter != null) {
                                        keywordSearchCooperateAdapter.add(tempTheatres);
                                    }
                                }
                                page++;
                            }
                        } else {
                            if (enrollInfoList.size() == 0) {
                                Log.e(TAG, "onSuccess: 首次加载数据失败");
                            } else {
                                if (page == 1) {
                                    Log.e(TAG, "onSuccess: 刷新数据失败");
                                    Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"刷新数据失败",Toast.LENGTH_SHORT).show();
                                    recyclerView.refreshComplete();
                                } else {
                                    recyclerView.loadMoreComplete();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败");
                                    Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"已无更多数据",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + enrollInfoList.size());
                    } else {
                        if (enrollInfoList.size() == 0) {
                            Log.e(TAG, "onSuccess: 首次加载数据失败");
                        } else {
                            if (page == 1) {
                                recyclerView.refreshComplete();
                                Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"刷新数据失败",Toast.LENGTH_SHORT).show();
                            } else {
                                recyclerView.loadMoreComplete();
                                Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"已无更多数据",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (enrollInfoList.size() == 0) {
                        Log.e(TAG, "onSuccess: 首次加载数据失败");
                        ivLoading.setVisibility(View.GONE);
                        llContent.setVisibility(View.GONE);
                        ivNoContent.setVisibility(View.VISIBLE);
                    } else {
                        if (page == 1) {
                            recyclerView.refreshComplete();
                            Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"刷新数据失败",Toast.LENGTH_SHORT).show();
                        } else {
                            recyclerView.loadMoreComplete();
                            Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"已无更多数据",Toast.LENGTH_SHORT).show();
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if(ivLoading==null)return;
                if (enrollInfoList.size() == 0) {
                    Log.e(TAG, "onSuccess: 首次加载数据失败");
                    ivLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                } else {
                    if (page == 1) {
                        recyclerView.refreshComplete();
                        Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"刷新数据失败",Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerView.loadMoreComplete();
                        Toast.makeText(KeywordSearchEnrollsResultShowActivity.this,"已无更多数据",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
//        tvSearchCount.setVisibility(View.INVISIBLE);
        getKeywordSearchAllMessage();

    }

    @Override
    public void onLoadMore() {
        getKeywordSearchAllMessage();
    }
}
