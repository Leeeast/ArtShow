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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.ToastUtils;
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
public class KeywordSearchNewsActivity extends BaseActivity implements View.OnClickListener, SmartRecyclerview.LoadingListener {


    private static final String NEWS = "news";
    public static final String ENROLL = "enroll";
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.fly_right_img)
    FrameLayout flyRightImg;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.rly_coll_head)
    LinearLayout rlyCollHead;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.tv_account)
    TextView tvSearchCount;
    @BindView(R.id.tv_search_type)
    TextView tvSearchType;
    @BindView(R.id.rcv)
    SmartRecyclerview recyclerView;
    @BindView(R.id.ll_whole)
    LinearLayout llWhole;

//    @BindView(R.id.rcv)
//    SmartRecyclerview recyclerView;
//    @BindView(R.id.lly_back)
//    LinearLayout llyBack;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.tv_search_count)
//    TextView tvSearchCount;
//    @BindView(R.id.tv_search_type)
//    TextView tvSearchType;
//    @BindView(R.id.iv_loading)
//    ImageView ivLoading;
//    @BindView(R.id.iv_no_content)
//    ImageView ivNoContent;
//    @BindView(R.id.ll_content)
//    LinearLayout llContent;


    private String keyword = "新闻";
    private String searchType = "";
    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private KeywordSearchNewsAdapter keywordSearchNewsAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int page = 1;
    private boolean newSearch = true;


    private void setData() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("搜索-" + keyword);
        if (searchType.equals(NEWS)) {
            tvSearchType.setText("资讯");
            tvSearchCount.setText("共发现" + newsesBeanList.size() + "条相关数据");
            keywordSearchNewsAdapter = new KeywordSearchNewsAdapter(this, newsesBeanList);
            linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            recyclerView.setAdapter(keywordSearchNewsAdapter);
            keywordSearchNewsAdapter.setOnItemClickListener(new KeywordSearchNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

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
        return R.layout.activity_search_news;
    }

    @Override
    public void initData() {


    }

    @Override
    public void initView() {

        llyBack.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        ivDelete.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }


    @Override
    public void setView() {

    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                llWhole.setVisibility(View.VISIBLE);
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
            case R.id.iv_delete:
                edtSearch.setText("");
                break;
            case R.id.tv_search:
                keyword = edtSearch.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    getKeywordSearchAllMessage();
                } else {
                    ToastUtils.showToast(KeywordSearchNewsActivity.this, 20, "请输入内容");
                }
                newSearch = true;
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
        llWhole.setVisibility(View.GONE);
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
        if (searchType.equals(NEWS)) {
//            资讯
            url = Constant.URL_KEYWORD_SEARCH_NEWS;
        } else if (searchType.equals(ENROLL)) {
            url = Constant.URL_KEYWORD_SEARCH_ENROLL;
        }
        Log.e(TAG, "getKeywordSearchAllMessage: url==" + url);
        Log.e(TAG, "getKeywordSearchAllMessage: params==" + params.toString());
        RequestUtil.request(true, url, params, 113, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {

                if (newSearch) {
                    newsesBeanList.clear();
                }
                newSearch = false;
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 1 && newsesBeanList.size() == 0) {
                            ivLoading.setVisibility(View.GONE);
                            llWhole.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                }, 500);
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj==" + obj);
                        Gson gson = new Gson();
                        ArrayList<NewsesBean> tempTheatres = new ArrayList<NewsesBean>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<NewsesBean>>() {
                        }.getType());
                        if (tempTheatres != null && tempTheatres.size() > 0) {
                            if (newsesBeanList.size() == 0) {
                                if (newsesBeanList.addAll(tempTheatres)) {
                                    uiHandler.removeCallbacksAndMessages(null);
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                            } else {
                                if (page == 1) {
                                    recyclerView.refreshComplete();
                                    newsesBeanList.clear();
                                    if (newsesBeanList.addAll(tempTheatres)) {
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                } else {
                                    recyclerView.loadMoreComplete();
                                    newsesBeanList.addAll(tempTheatres);
                                    if (keywordSearchNewsAdapter != null) {
                                        keywordSearchNewsAdapter.add(tempTheatres);
                                    }
                                }
                                page++;
                            }
                        } else {
                            if (newsesBeanList.size() == 0) {
                                Log.e(TAG, "onSuccess: 首次加载数据失败");
                            } else {
                                if (page == 1) {
                                    Log.e(TAG, "onSuccess: 刷新数据失败");
                                    recyclerView.refreshComplete();
                                } else {
                                    recyclerView.loadMoreComplete();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败");
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + newsesBeanList.size());
                    } else {
                        if (newsesBeanList.size() == 0) {
                            Log.e(TAG, "onSuccess: 首次加载数据失败");
                        } else {
                            if (page == 1) {
                                recyclerView.refreshComplete();
                                Log.e(TAG, "onSuccess: 刷新数据失败");
                            } else {
                                recyclerView.loadMoreComplete();
                                Log.e(TAG, "onSuccess: 加载更多数据失败");
                            }
                        }
                    }
                } else {
                    if (newsesBeanList.size() == 0) {
                        Log.e(TAG, "onSuccess: 首次加载数据失败");
                        ivLoading.setVisibility(View.GONE);
                        llWhole.setVisibility(View.GONE);
                        ivNoContent.setVisibility(View.VISIBLE);
                    } else {
                        if (page == 1) {
                            recyclerView.refreshComplete();
                            Log.e(TAG, "onSuccess: 刷新数据失败");
                        } else {
                            recyclerView.loadMoreComplete();
                            Log.e(TAG, "onSuccess: 加载更多数据失败");
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (newSearch) {
                    newsesBeanList.clear();
                }
                newSearch = false;
                if (newsesBeanList.size() == 0) {
                    Log.e(TAG, "onSuccess: 首次加载数据失败");
                    ivLoading.setVisibility(View.GONE);
                    llWhole.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                } else {
                    if (page == 1) {
                        recyclerView.refreshComplete();
                        Log.e(TAG, "onSuccess: 刷新数据失败");
                    } else {
                        recyclerView.loadMoreComplete();
                        Log.e(TAG, "onSuccess: 加载更多数据失败");
                    }
                }

            }
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
        getKeywordSearchAllMessage();
    }

    @Override
    public void onLoadMore() {
        getKeywordSearchAllMessage();
    }
}
