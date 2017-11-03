package com.art.huakai.artshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.entity.TalentBean;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.entity.Work;
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
public class KeywordSearchEnrollsResultShowActivity extends BaseActivity implements View.OnClickListener {


    private static final String NEWS = "news";
    private static final String ENROLLS = "enrolls";
    private static final String REPERTORYS = "repertorys";
    private static final String TALENTS = "talents";
    private static final String THEATRES = "theatres";

    @BindView(R.id.rcv)
    RecyclerView recyclerView;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_search_count)
    TextView tvSearchCount;
    @BindView(R.id.tv_search_type)
    TextView tvSearchType;
    private String keyword = "新闻";
    private String searchType = "";
    private List<NewsesBean> newsesBeanList = new ArrayList<NewsesBean>();
    private List<Theatre> theatreList = new ArrayList<Theatre>();
    private List<EnrollInfo> enrollInfoList = new ArrayList<EnrollInfo>();
    private List<Work> workLists = new ArrayList<Work>();
    private List<TalentBean> talentBeanlists = new ArrayList<TalentBean>();

    private KeywordSearchNewsAdapter keywordSearchNewsAdapter;
    private KeywordSearchCooperateAdapter keywordSearchCooperateAdapter;
    private KeywordSearchProfessionalAdapter keywordSearchProfessionalAdapter;
    private KeywordSearchWorksAdapter keywordSearchWorksAdapter;
    private KeywordSearchTheatreAdapter KeywordSearchTheatreAdapter;
    private LinearLayoutManager linearlayoutManager;

    private int page=1;

    private void setData() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("搜索-"+keyword);
        if (searchType.equals(NEWS)) {
            tvSearchType.setText("资讯");
            tvSearchCount.setText("共发现"+newsesBeanList.size()+"条相关数据");
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
        } else if (searchType.equals(TALENTS)) {
            tvSearchType.setText("人才");
            tvSearchCount.setText("共发现"+talentBeanlists.size()+"条相关数据");
            if (keywordSearchProfessionalAdapter == null) {
                keywordSearchProfessionalAdapter = new KeywordSearchProfessionalAdapter(this, talentBeanlists);
                if (linearlayoutManager == null) {
                    linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                }
                recyclerView.setLayoutManager(linearlayoutManager);
                recyclerView.setAdapter(keywordSearchProfessionalAdapter);
                keywordSearchProfessionalAdapter.setOnItemClickListener(new KeywordSearchProfessionalAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
            } else {
                keywordSearchProfessionalAdapter.notifyDataSetChange(talentBeanlists);
            }
        } else if (searchType.equals(THEATRES)) {
            tvSearchType.setText("剧场");
            tvSearchCount.setText("共发现"+theatreList.size()+"条相关数据");
            if (KeywordSearchTheatreAdapter == null) {
                KeywordSearchTheatreAdapter = new KeywordSearchTheatreAdapter(this, theatreList);
                if (linearlayoutManager == null) {
                    linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                }
                recyclerView.setLayoutManager(linearlayoutManager);
                recyclerView.setAdapter(KeywordSearchTheatreAdapter);
                KeywordSearchTheatreAdapter.setOnItemClickListener(new KeywordSearchTheatreAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
            } else {
                KeywordSearchTheatreAdapter.notifyDataSetChange(theatreList);
            }
        } else if (searchType.equals(REPERTORYS)) {
            tvSearchType.setText("项目");
            tvSearchCount.setText("共发现"+workLists.size()+"条相关数据");
            if (keywordSearchWorksAdapter == null) {
                keywordSearchWorksAdapter = new KeywordSearchWorksAdapter(this, workLists);
                if (linearlayoutManager == null) {
                    linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                }
                recyclerView.setLayoutManager(linearlayoutManager);
                recyclerView.setAdapter(keywordSearchWorksAdapter);
                keywordSearchWorksAdapter.setOnItemClickListener(new KeywordSearchWorksAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
            } else {
                keywordSearchWorksAdapter.notifyDataSetChange(workLists);
            }
        } else if (searchType.equals(ENROLLS)) {
            tvSearchType.setText("合作");
            tvSearchCount.setText("共发现"+enrollInfoList.size()+"条相关数据");
            if (keywordSearchCooperateAdapter == null) {
                keywordSearchCooperateAdapter = new KeywordSearchCooperateAdapter(enrollInfoList);
                if (linearlayoutManager == null) {
                    linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                }
                recyclerView.setLayoutManager(linearlayoutManager);
                recyclerView.setAdapter(keywordSearchCooperateAdapter);
                keywordSearchCooperateAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                    }
                });
            } else {
                keywordSearchCooperateAdapter.notifyDataSetChange(enrollInfoList);
            }
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
    }


    private void getKeywordSearchAllMessage() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_KEYWORD_SEARCH_NEWS==" + Constant.URL_KEYWORD_SEARCH_NEWS);
        params.put("keyword", keyword);
        params.put("page",page+"");

//        params.put("sign", sign);
//        Log.e(TAG, "getList: sign==" + sign);
//        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        String url = "";
        if (searchType.equals(NEWS)) {
//            资讯
            url = Constant.URL_KEYWORD_SEARCH_NEWS;
        } else if (searchType.equals(ENROLLS)) {
//            合作招募
            url = Constant.URL_KEYWORD_SEARCH_ENROLL;
        } else if (searchType.equals(TALENTS)) {
//            人才
            url = Constant.URL_KEYWORD_SEARCH_TALENS;
        } else if (searchType.equals(THEATRES)) {
//            剧场
            url = Constant.URL_KEYWORD_SEARCH_THEATRES;
        } else if (searchType.equals(REPERTORYS)) {
//            项目
            url = Constant.URL_KEYWORD_SEARCH_REPERTORYS;
        }
        Log.e(TAG, "getKeywordSearchAllMessage: url==" + url);
        Log.e(TAG, "getKeywordSearchAllMessage: params==" + params.toString());
        RequestUtil.request(true, url, params, 113, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: 1111111111111obj222=" + obj);
                        Gson gson = new Gson();
                        if (searchType.equals(NEWS)) {
                            try {
                                newsesBeanList.clear();
                                newsesBeanList = gson.fromJson(obj, new TypeToken<List<NewsesBean>>() {
                                }.getType());
                            } catch (Exception e) {
                                Log.e(TAG, "数据结构异常");
                                return;
                            }
                            if (newsesBeanList.size() > 0) {
                                Log.e(TAG, "onSuccess: 1234567");
                                uiHandler.sendEmptyMessage(0);
                            } else {
                                Log.e(TAG, "数据为空");
                            }
                        } else if (searchType.equals(ENROLLS)) {
                            try {
                                enrollInfoList.clear();
                                enrollInfoList = gson.fromJson(obj, new TypeToken<List<EnrollInfo>>() {
                                }.getType());
                            } catch (Exception e) {
                                Log.e(TAG, "数据结构异常");
                                return;
                            }
                            if (enrollInfoList.size() > 0) {
                                Log.e(TAG, "onSuccess: 1234567");
                                uiHandler.sendEmptyMessage(0);
                            } else {
                                Log.e(TAG, "数据为空");
                            }
                        } else if (searchType.equals(TALENTS)) {
                            try {
                                talentBeanlists.clear();
                                talentBeanlists = gson.fromJson(obj, new TypeToken<List<TalentBean>>() {
                                }.getType());
                            } catch (Exception e) {
                                Log.e(TAG, "数据结构异常");
                                return;
                            }
                            if (talentBeanlists.size() > 0) {
                                Log.e(TAG, "onSuccess: 1234567");
                                uiHandler.sendEmptyMessage(0);
                            } else {
                                Log.e(TAG, "数据为空");
                            }
                        } else if (searchType.equals(THEATRES)) {
                            try {
                                theatreList.clear();
                                theatreList = gson.fromJson(obj, new TypeToken<List<Theatre>>() {
                                }.getType());
                            } catch (Exception e) {
                                Log.e(TAG, "数据结构异常");
                                return;
                            }
                            if (theatreList.size() > 0) {
                                Log.e(TAG, "onSuccess: 1234567");
                                uiHandler.sendEmptyMessage(0);
                            } else {
                                Log.e(TAG, "数据为空");
                            }
                        } else if (searchType.equals(REPERTORYS)) {
                            try {
                                workLists.clear();
                                workLists = gson.fromJson(obj, new TypeToken<List<Work>>() {
                                }.getType());
                            } catch (Exception e) {
                                Log.e(TAG, "数据结构异常");
                                return;
                            }
                            if (workLists.size() > 0) {
                                Log.e(TAG, "onSuccess: 1234567");
                                uiHandler.sendEmptyMessage(0);
                            } else {
                                Log.e(TAG, "数据为空");
                            }
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
