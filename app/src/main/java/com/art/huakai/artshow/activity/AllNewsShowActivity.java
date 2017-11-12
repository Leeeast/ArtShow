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
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.ToastUtils;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.PullToRefreshScroll.PullToRefreshLayout;
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
public class AllNewsShowActivity extends BaseActivity implements View.OnClickListener, SmartRecyclerview.LoadingListener {


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
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.rcv)
    SmartRecyclerview recyclerView;


    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private KeywordSearchNewsAdapter keywordSearchNewsAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int page = 1;
    private boolean isLoading=false;
    private boolean isRefresh=false;

    private void setData() {
        if(ivLoading==null)return;
            keywordSearchNewsAdapter = new KeywordSearchNewsAdapter(this, newsesBeanList);
            linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            recyclerView.setAdapter(keywordSearchNewsAdapter);
            keywordSearchNewsAdapter.setOnItemClickListener(new KeywordSearchNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                    Bundle bundle = new Bundle();
                    bundle.putString(NewsDetailActivity.PARAMS_NEWS_ID, newsesBeanList.get(position).getId());
                    invokActivity(AllNewsShowActivity.this, NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);

                }
            });
    }


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_all_news_show;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        llyBack.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        ivRightImg.setImageResource(R.mipmap.search_black);
        ivRightImg.setOnClickListener(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("行业资讯");
    }

    @Override
    public void setView() {

    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(ivLoading==null)return;
                recyclerView.refreshComplete();
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
            case R.id.iv_right_img:
                Intent intent=new Intent();
                intent.setClass(AllNewsShowActivity.this,KeywordSearchNewsActivity.class);
                startActivity(intent);
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
        getKeywordSearchAllMessage();
        ivLoading.setVisibility(View.VISIBLE);
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }


    private void getKeywordSearchAllMessage() {

        if(isLoading)return;
        isLoading=true;
        final Map<String, String> params = new TreeMap<>();
        params.put("page", page + "");
        String url = Constant.URL_GET_NEWS;
        Log.e(TAG, "getKeywordSearchAllMessage: url==" + url);
        Log.e(TAG, "getKeywordSearchAllMessage: params==" + params.toString());
        RequestUtil.request(true, url, params, 113, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if(ivLoading==null)return;
                isLoading=false;
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ivLoading==null)return;
                        if (page == 1 && newsesBeanList.size() == 0) {
                            ivLoading.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                }, 500);
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        try{
                            Gson gson = new Gson();
                            ArrayList<NewsesBean> tempNewsesBeanList = new ArrayList<NewsesBean>();
                            tempNewsesBeanList = gson.fromJson(obj, new TypeToken<List<NewsesBean>>() {
                            }.getType());
                            if(tempNewsesBeanList!=null&&tempNewsesBeanList.size()>0){
                                page++;
                                if(isRefresh){
                                    newsesBeanList.clear();
                                    if(newsesBeanList.addAll(tempNewsesBeanList)){
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                    isRefresh=false;
                                }else{
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(recyclerView==null)return;
                                            if(page!=1){
                                                recyclerView.loadMoreComplete();
                                            }
                                        }
                                    });
                                    newsesBeanList.addAll(tempNewsesBeanList);
                                    if(keywordSearchNewsAdapter!=null){
                                        keywordSearchNewsAdapter.add(tempNewsesBeanList);
                                    }else{
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                }
                                return;
                            }
                        }catch (Exception e){
                        }
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
                if (ivLoading == null) return;
                if (isRefresh) {
                    Toast.makeText(AllNewsShowActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                }else{
                    if(page==1){
                        ivLoading.setVisibility(View.GONE);
                        ivNoContent.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(AllNewsShowActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                    }
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isRefresh){
                            recyclerView.refreshComplete();
                        }else if(page!=1){
                            recyclerView.loadMoreComplete();
                        }
                    }
                });

            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                isLoading=false;
                if(ivLoading==null)return;

                if (isRefresh) {
                    Toast.makeText(AllNewsShowActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                }else{
                    if(page==1){
                        ivLoading.setVisibility(View.GONE);
                        ivNoContent.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(AllNewsShowActivity.this, "已无更多数据", Toast.LENGTH_SHORT).show();
                    }
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isRefresh){
                            recyclerView.refreshComplete();
                        }else if(page!=1){
                            recyclerView.loadMoreComplete();
                        }
                    }
                });

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
