package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.adapter.NewsShowAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
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
public class NewsShowActivity extends BaseActivity implements View.OnClickListener, SmartRecyclerview.LoadingListener {


    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String keyword = "剧场";
    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private NewsShowAdapter keywordSearchNewsAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int page = 1;


    private void setData() {
        keywordSearchNewsAdapter = new NewsShowAdapter(this, newsesBeanList);
        linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        recyclerView.setAdapter(keywordSearchNewsAdapter);
        keywordSearchNewsAdapter.setOnItemClickListener(new NewsShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(NewsDetailActivity.PARAMS_NEWS_ID, newsesBeanList.get(position).getId());
                invokActivity(NewsShowActivity.this, NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);
            }
        });
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

        getList();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void getList() {

        Map<String, String> params = new TreeMap<>();
        params.put("page", page + "");
        String sign = SignUtil.getSign(params);
        Log.e(TAG, "getList: URL_GET_NEWS==" + Constant.URL_GET_NEWS);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getList: params==" + params.toString());
        RequestUtil.request(true, Constant.URL_GET_NEWS, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj==" + obj);
                        Gson gson = new Gson();
                        ArrayList<NewsesBean> tempTheatres = new ArrayList<NewsesBean>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<NewsesBean>>() {
                        }.getType());
                        if (tempTheatres != null && tempTheatres.size() > 0) {
                            Log.e(TAG, "onSuccess: 111111");
                            if (newsesBeanList.size() == 0) {
                                Log.e(TAG, "onSuccess: 22222");
                                if (newsesBeanList.addAll(tempTheatres)) {
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                                Log.e(TAG, "onSuccess: page==" + page);
                            } else {
                                if (page == 1) {
                                    Log.e(TAG, "onSuccess: 33333");
                                    recyclerView.refreshComplete();
                                    newsesBeanList.clear();
                                    if (newsesBeanList.addAll(tempTheatres)) {
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                } else {
                                    Log.e(TAG, "onSuccess: 444444");
                                    recyclerView.loadMoreComplete();
                                    newsesBeanList.addAll(tempTheatres);
                                    if (keywordSearchNewsAdapter != null) {
                                        keywordSearchNewsAdapter.add(tempTheatres);
                                    }
                                }
                                page++;
                            }
                        } else {
                            Log.e(TAG, "onSuccess: 55555");
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
                        Log.e(TAG, "onSuccess: 666666");
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
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
        getList();
    }

    @Override
    public void onLoadMore() {
        getList();
    }
}
