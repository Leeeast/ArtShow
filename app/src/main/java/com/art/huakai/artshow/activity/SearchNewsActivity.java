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
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
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
public class SearchNewsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.rcv)
    RecyclerView recyclerView;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String keyword = "剧场";
    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private KeywordSearchNewsAdapter keywordSearchNewsAdapter;
    private LinearLayoutManager linearlayoutManager;

    private void setData() {

        if (keywordSearchNewsAdapter == null) {
            keywordSearchNewsAdapter = new KeywordSearchNewsAdapter(this, newsesBeanList);
            if (linearlayoutManager == null) {
                linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            }
            recyclerView.setLayoutManager(linearlayoutManager);
            recyclerView.setAdapter(keywordSearchNewsAdapter);
            keywordSearchNewsAdapter.setOnItemClickListener(new KeywordSearchNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                }
            });
        } else {
            keywordSearchNewsAdapter.notifyDataSetChange(newsesBeanList);
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

        getKeywordSearchAllMessage();

    }


    @Override
    public void initView() {
        llyBack.setOnClickListener(this);
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


    private void getKeywordSearchAllMessage() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_KEYWORD_SEARCH_NEWS==" + Constant.URL_KEYWORD_SEARCH_NEWS);
        params.put("keyword", keyword);
//        String sign = SignUtil.getSign(params);
//        params.put("sign", sign);
//        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_KEYWORD_SEARCH_NEWS, params, 112, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: 1111111111111obj222=" + obj);
                        Gson gson = new Gson();
                        newsesBeanList.clear();
                        newsesBeanList = gson.fromJson(obj, new TypeToken<List<NewsesBean>>() {
                        }.getType());
                        if (newsesBeanList.size() > 0) {
                            uiHandler.sendEmptyMessage(0);
                        } else {

                        }
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
            }
        });
    }


}
