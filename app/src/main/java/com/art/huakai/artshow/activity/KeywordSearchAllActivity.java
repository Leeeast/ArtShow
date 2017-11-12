package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.KeywordSearchCooperateAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchNewsAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchProfessionalAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchTheatreAdapter;
import com.art.huakai.artshow.adapter.KeywordSearchWorksAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.SearchAllBean;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.SoftInputUtil;
import com.art.huakai.artshow.utils.ToastUtils;
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
    //    @BindView(R.id.lly_back)
//    LinearLayout llyBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_right_img)
//    ImageView ivRightImg;
//    @BindView(R.id.fly_right_img)
//    FrameLayout flyRightImg;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;

    private String keyword = "*";
    private SearchAllBean searchAllBean;
    private boolean loadingData=false;
    private int totalSize;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private String validKeyword;



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
//        getKeywordSearchAllMessage();
    }

    @Override
    public void initView() {
//        llyBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtSearch.requestFocus();
                SoftInputUtil.toggleInput(KeywordSearchAllActivity.this);
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
//        AnimUtils.rotate(ivLoading);
        ivLoading.setVisibility(View.GONE);
        ivNoContent.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(ivLoading==null)return;
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                tvAccount.setVisibility(View.VISIBLE);
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
                    SoftInputUtil.hideInput(KeywordSearchAllActivity.this);
                    getKeywordSearchAllMessage();
                    tvAccount.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(KeywordSearchAllActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
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
        totalSize=0;
        if(view1!=null){
            llWhole.removeView(view1);
        }
        if(view2!=null){
            llWhole.removeView(view2);
        }
        if(view3!=null){
            llWhole.removeView(view3);
        }
        if(view4!=null){
            llWhole.removeView(view4);
        }
        if(view5!=null){
            llWhole.removeView(view5);
        }
        if (searchAllBean != null) {
            if (searchAllBean.getEnrolls() != null && searchAllBean.getEnrolls().size() > 0) {
                totalSize=searchAllBean.getEnrolls().size();
                Log.e(TAG, "setData: 1111111");
                view1= LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view1.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.rcv);
                tv_name.setText("合作机会");
                TextView tv_see_all = (TextView) view1.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchEnrollsResultShowActivity.class);
                        intent.putExtra("searchType", "enrolls");
                        intent.putExtra("keyword", validKeyword);
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
                llWhole.addView(view1);
            }
            if (searchAllBean.getNews() != null && searchAllBean.getNews().size() > 0) {
                totalSize=totalSize+searchAllBean.getNews().size();
                Log.e(TAG, "setData: 222222");
                view2 = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view2.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view2.findViewById(R.id.rcv);
                tv_name.setText("资讯");
                TextView tv_see_all = (TextView) view2.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchNewsResultShowActivity.class);
                        intent.putExtra("searchType", "news");
                        intent.putExtra("keyword", validKeyword);
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
                llWhole.addView(view2);
            }
            if (searchAllBean.getRepertorys() != null && searchAllBean.getRepertorys().size() > 0) {
                totalSize=totalSize+searchAllBean.getRepertorys().size();
                Log.e(TAG, "setData: 333333");
                view3= LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view3.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view3.findViewById(R.id.rcv);
                tv_name.setText("项目");
                TextView tv_see_all = (TextView) view3.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchRepertorysResultShowActivity.class);
                        intent.putExtra("searchType", "repertorys");
                        intent.putExtra("keyword", validKeyword);
                        startActivity(intent);
                    }
                });
                KeywordSearchWorksAdapter keywordSearchWorksAdapter = new KeywordSearchWorksAdapter(this, searchAllBean.getRepertorys());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchWorksAdapter.setOnItemClickListener(new KeywordSearchWorksAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WorksDetailMessageActivity.PARAMS_ID, searchAllBean.getRepertorys().get(position).getId());
                        invokActivity(KeywordSearchAllActivity.this, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchWorksAdapter);
                llWhole.addView(view3);

            }
            if (searchAllBean.getTalents() != null && searchAllBean.getTalents().size() > 0) {
                totalSize=totalSize+searchAllBean.getTalents().size();
                Log.e(TAG, "setData: 4444444");
                view4= LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view4.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view4.findViewById(R.id.rcv);
                tv_name.setText("人才");
                TextView tv_see_all = (TextView) view4.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchTalentsResultShowActivity.class);
                        intent.putExtra("searchType", "talents");
                        intent.putExtra("keyword", validKeyword);
                        startActivity(intent);
                    }
                });
                KeywordSearchProfessionalAdapter keywordSearchProfessionalAdapter = new KeywordSearchProfessionalAdapter(this, searchAllBean.getTalents());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchProfessionalAdapter.setOnItemClickListener(new KeywordSearchProfessionalAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, searchAllBean.getTalents().get(position).getId());
                        invokActivity(KeywordSearchAllActivity.this, PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PERSONAL);
                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchProfessionalAdapter);
                llWhole.addView(view4);

            }
            if (searchAllBean.getTheaters() != null && searchAllBean.getTheaters().size() > 0) {
                totalSize=totalSize+searchAllBean.getTheaters().size();
                Log.e(TAG, "setData: 5555555");
                view5 = LayoutInflater.from(this).inflate(R.layout.keyword_search_all_item, null);
                TextView tv_name = (TextView) view5.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view5.findViewById(R.id.rcv);
                tv_name.setText("剧场");
                TextView tv_see_all = (TextView) view5.findViewById(R.id.tv_see_all);
                tv_see_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(KeywordSearchAllActivity.this, KeywordSearchTheatresResultShowActivity.class);
                        intent.putExtra("searchType", "theatres");
                        intent.putExtra("keyword", validKeyword);
                        startActivity(intent);

                    }
                });
                KeywordSearchTheatreAdapter keywordSearchTheatreAdapter = new KeywordSearchTheatreAdapter(this, searchAllBean.getTheaters());
                LinearLayoutManager industryNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                keywordSearchTheatreAdapter.setOnItemClickListener(new KeywordSearchTheatreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, searchAllBean.getTheaters().get(position).getId());
                        bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, false);
                        invokActivity(KeywordSearchAllActivity.this, TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
                    }
                });
//              实现屏蔽recyclerview的滑动效果
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(industryNewsLayoutManager);
                recyclerView.setAdapter(keywordSearchTheatreAdapter);
                llWhole.addView(view5);

            }
        }

        tvAccount.setText("共发现"+totalSize+"条相关数据");
    }


    private void getKeywordSearchAllMessage() {
        if(loadingData)return;
        loadingData=true;
        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_KEYWORD_SEARCH==" + Constant.URL_KEYWORD_SEARCH);
        params.put("keyword", keyword);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());

        RequestUtil.request(true, Constant.URL_KEYWORD_SEARCH, params, 111, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                loadingData=false;
                if (isSuccess) {
                    validKeyword=keyword;
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: 1111111111111obj222=" + obj);
                        Gson gson = new Gson();
                        searchAllBean = null;
                        try {
                            searchAllBean = gson.fromJson(obj, SearchAllBean.class);
                        } catch (Exception e) {

                        }
                        if (searchAllBean != null) {
                            if ((searchAllBean.getRepertorys() != null && searchAllBean.getRepertorys().size() > 0) || (searchAllBean.getTalents() != null && searchAllBean.getTalents().size() > 0) || (searchAllBean.getTheaters() != null && searchAllBean.getTheaters().size() > 0) || (searchAllBean.getEnrolls() != null && searchAllBean.getEnrolls().size() > 0) || (searchAllBean.getNews() != null && searchAllBean.getNews().size() > 0)) {
                                uiHandler.sendEmptyMessage(0);
                                return;
                            }
                        }
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
                if(ivLoading==null)return;
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if(ivLoading==null)return;
                loadingData=false;
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

            }
        });
    }


}
