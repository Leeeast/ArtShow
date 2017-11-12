package com.art.huakai.artshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SoftInputUtil;
import com.art.huakai.artshow.utils.ToastUtils;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.RunnableFuture;

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
    @BindView(R.id.rcv)
    SmartRecyclerview recyclerView;
    @BindView(R.id.ll_whole)
    LinearLayout llWhole;
    @BindView(R.id.rl_whole)
    RelativeLayout rlWhole;


    private String keyword = "";
    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private KeywordSearchNewsAdapter keywordSearchNewsAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int page = 1;
    private int totalCount;
    private boolean newSearch=true;
    private boolean isRefresh=false;

    private void setData() {
        if (ivLoading == null) return;
            tvTitle.setVisibility(View.VISIBLE);
            tvSearchCount.setText("共发现" + totalCount + "条相关数据");
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
        rlWhole.setVisibility(View.GONE);
        llyBack.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        ivDelete.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvTitle.setText("行业资讯-搜索");
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtSearch.requestFocus();
                SoftInputUtil.toggleInput(KeywordSearchNewsActivity.this);
            }
        },300);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(edtSearch.getText().toString().trim())){
                    ivDelete.setVisibility(View.INVISIBLE);
                }else{
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void setView() {

    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(ivDelete==null)return;
                tvSearchCount.setVisibility(View.VISIBLE);
                tvSearchCount.setText("共发现"+totalCount+"条相关数据");
                ivLoading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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
                    newSearch=true;
                    page = 1;
                    tvSearchCount.setVisibility(View.GONE);
                    SoftInputUtil.hideInput(KeywordSearchNewsActivity.this);
                    getKeywordSearchAllMessage();
                } else {
                    Toast.makeText(KeywordSearchNewsActivity.this,"请输入搜索内容",Toast.LENGTH_SHORT).show();
                }
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
    }


    private void getKeywordSearchAllMessage() {
        rlWhole.setVisibility(View.VISIBLE);
        Map<String, String> params = new TreeMap<>();
        params.put("keyword", keyword);
        params.put("page", page + "");
        String url = Constant.URL_KEYWORD_SEARCH_NEWS;
        Log.e(TAG, "getKeywordSearchAllMessage: url==" + url);
        Log.e(TAG, "getKeywordSearchAllMessage: params==" + params.toString());
        RequestUtil.request( url, params, 113, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (ivLoading == null) return;
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (newSearch) {
                            if(ivLoading==null)return;
                            ivLoading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
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
                            uiHandler.removeCallbacksAndMessages(null);
                            if (newSearch) {
                                newSearch=false;
                                newsesBeanList.clear();
                                if (newsesBeanList.addAll(tempTheatres)) {
                                    totalCount=id;
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                            } else {
                                if (isRefresh) {
                                    isRefresh=false;
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
                            if (newSearch) {
                                newSearch=false;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivLoading.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        ivNoContent.setVisibility(View.VISIBLE);
                                    }
                                });
                                Toast.makeText(KeywordSearchNewsActivity.this, "未查询到您搜索的数据", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 首次加载数据失败");
                            } else {
                                if (isRefresh) {
                                    isRefresh=false;
                                    Log.e(TAG, "onSuccess: 刷新数据失败");
                                    Toast.makeText(KeywordSearchNewsActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerView.refreshComplete();
                                        }
                                    });
                                } else {
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerView.loadMoreComplete();
                                        }
                                    });

                                    Toast.makeText(KeywordSearchNewsActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败");
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + newsesBeanList.size());
                    } else {
                        if (newSearch) {
                            Log.e(TAG, "onSuccess: 首次加载数据失败");
                            Toast.makeText(KeywordSearchNewsActivity.this, "未查询到您搜索的数据", Toast.LENGTH_SHORT).show();
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ivLoading.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    ivNoContent.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            if (isRefresh) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.refreshComplete();
                                    }
                                });
                                Log.e(TAG, "onSuccess: 刷新数据失败");
                                Toast.makeText(KeywordSearchNewsActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                            } else {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.loadMoreComplete();
                                    }
                                });
                                Log.e(TAG, "onSuccess: 加载更多数据失败");
                                Toast.makeText(KeywordSearchNewsActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (newSearch) {
                        Log.e(TAG, "onSuccess: 首次加载数据失败");
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ivLoading.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                ivNoContent.setVisibility(View.VISIBLE);
                                Toast.makeText(KeywordSearchNewsActivity.this, "未查询到您搜索的数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (isRefresh) {
                            isRefresh=false;
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.refreshComplete();
                                    Toast.makeText(KeywordSearchNewsActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.e(TAG, "onSuccess: 刷新数据失败");
                        } else {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.loadMoreComplete();
                                    Toast.makeText(KeywordSearchNewsActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.e(TAG, "onSuccess: 加载更多数据失败");
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
                newSearch=false;
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (ivLoading == null) return;
                if (newSearch) {
                    newSearch=false;
                    Log.e(TAG, "onSuccess: 首次加载数据失败");
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ivLoading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                            Toast.makeText(KeywordSearchNewsActivity.this, "未查询到您筛选的数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (isRefresh) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.refreshComplete();
                                Toast.makeText(KeywordSearchNewsActivity.this, "刷新数据失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e(TAG, "onSuccess: 刷新数据失败");
                    } else {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.loadMoreComplete();
                                Toast.makeText(KeywordSearchNewsActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e(TAG, "onSuccess: 加载更多数据失败");
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
        isRefresh=true;
        getKeywordSearchAllMessage();
    }

    @Override
    public void onLoadMore() {
        getKeywordSearchAllMessage();
    }
}
