package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchCooperateAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchProfessionalAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchTheatreAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchWorksAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.SearchAllBean;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.google.gson.Gson;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lining on 2017/10/22.
 */
public class KeywordSearchAllActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.ll_whole)
    LinearLayout llWhole;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.fly_right_img)
    FrameLayout flyRightImg;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    private String keyword = "剧场";
    private SearchAllBean searchAllBean;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_search_all;
    }

    @Override
    public void initData() {

        getKeywordSearchAllMessage();

    }


    @Override
    public void initView() {
        llyBack.setOnClickListener(this);
//        Drawable drawableLeft = getResources().getDrawable(R.mipmap.search_gray);
//        drawableLeft.setBounds(
//                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX),
//                0,
//                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX)
//                        + getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX),
//                getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX));
//        edtSearch.setCompoundDrawables(drawableLeft, null, null, null);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }


    @Override
    public void setView() {
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
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


    private void setData() {

        if (searchAllBean != null) {

            Log.e(TAG, "setData:getNews().size== " + searchAllBean.getNews().size());
            Log.e(TAG, "setData:getTheaters().size== " + searchAllBean.getTheaters().size());
            Log.e(TAG, "setData: getTalents().size==" + searchAllBean.getTalents().size());
            Log.e(TAG, "setData: getRepertorys().size()==" + searchAllBean.getRepertorys().size());

            if (searchAllBean.getEnrolls() != null && searchAllBean.getEnrolls().size() > 0) {
                Log.e(TAG, "setData: 1111111");
                View view = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                tv_name.setText("合作招募");
                TextView tv_see_all = (TextView) view.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchResultShowActivity.class);
                        intent.putExtra("searchType", "enrolls");
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);

                    }
                });
                KeywordSearchCooperateAdapter keywordSearchCooperateAdapter = new KeywordSearchCooperateAdapter(searchAllBean.getEnrolls());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchCooperateAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchCooperateAdapter);
                llWhole.addView(view);
            }
            if (searchAllBean.getNews() != null && searchAllBean.getNews().size() > 0) {
                Log.e(TAG, "setData: 222222");
                View view = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                tv_name.setText("资讯");
                TextView tv_see_all = (TextView) view.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchResultShowActivity.class);
                        intent.putExtra("searchType", "news");
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);

                    }
                });
                KeywordSearchNewsAdapter keywordSearchNewsAdapter = new KeywordSearchNewsAdapter(this, searchAllBean.getNews());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchNewsAdapter.setOnItemClickListener(new KeywordSearchNewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchNewsAdapter);
                llWhole.addView(view);

            }
            if (searchAllBean.getRepertorys() != null && searchAllBean.getRepertorys().size() > 0) {
                Log.e(TAG, "setData: 333333");
                View view = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                tv_name.setText("项目");
                TextView tv_see_all = (TextView) view.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchResultShowActivity.class);
                        intent.putExtra("searchType", "repertorys");
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);
                    }
                });
                KeywordSearchWorksAdapter keywordSearchWorksAdapter = new KeywordSearchWorksAdapter(this, searchAllBean.getRepertorys());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchWorksAdapter.setOnItemClickListener(new KeywordSearchWorksAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchWorksAdapter);
                llWhole.addView(view);

            }
            if (searchAllBean.getTalents() != null && searchAllBean.getTalents().size() > 0) {
                Log.e(TAG, "setData: 4444444");
                View view = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                tv_name.setText("人才");
                TextView tv_see_all = (TextView) view.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchResultShowActivity.class);
                        intent.putExtra("searchType", "talents");
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);
                    }
                });
                KeywordSearchProfessionalAdapter keywordSearchProfessionalAdapter = new KeywordSearchProfessionalAdapter(this, searchAllBean.getTalents());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchProfessionalAdapter.setOnItemClickListener(new KeywordSearchProfessionalAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchProfessionalAdapter);
                llWhole.addView(view);

            }
            if (searchAllBean.getTheaters() != null && searchAllBean.getTheaters().size() > 0) {
                Log.e(TAG, "setData: 5555555");
                View view = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                tv_name.setText("剧场");
                TextView tv_see_all = (TextView) view.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchResultShowActivity.class);
                        intent.putExtra("searchType", "theatres");
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);

                    }
                });
                KeywordSearchTheatreAdapter keywordSearchTheatreAdapter = new KeywordSearchTheatreAdapter(this, searchAllBean.getTheaters());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchTheatreAdapter.setOnItemClickListener(new KeywordSearchTheatreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchTheatreAdapter);
                llWhole.addView(view);

            }
        }
    }


    private void getKeywordSearchAllMessage() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_KEYWORD_SEARCH==" + Constant.URL_KEYWORD_SEARCH);
        params.put("keyword", keyword);
//        String sign = SignUtil.getSign(params);
//        params.put("sign", sign);
//        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_KEYWORD_SEARCH, params, 111, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: 1111111111111obj222=" + obj);
                        Gson gson = new Gson();
                        try{
                            searchAllBean = gson.fromJson(obj, SearchAllBean.class);
                        }catch (Exception e){

                        }
                        uiHandler.sendEmptyMessage(0);
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
