package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TalentDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.WorksDetailBean;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.fragment.WorksDetailShowFragment;
import com.art.huakai.artshow.fragment.WorksDetailSkillFragment;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.art.huakai.artshow.widget.headerviewpager.HeaderViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import okhttp3.Call;

public class WorksDetailMessageActivity extends BaseActivity implements View.OnClickListener {
    public static final String PARAMS_ID = "PARAMS_ID";

    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.sdv)
    ChinaShowImageView sdv;
    @BindView(R.id.tv_theatre_name)
    TextView tvTheatreName;
    @BindView(R.id.tv_theatre_kind)
    TextView tvTheatreKind;
    @BindView(R.id.iv_authentication)
    ImageView ivAuthentication;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_producers)
    TextView tvProducers;
    @BindView(R.id.tv_actor_number)
    TextView tvActorNumber;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_thratre_size)
    TextView tvThratreSize;
    @BindView(R.id.tv_show_time)
    TextView tvShowTime;
    @BindView(R.id.tv_first_show_time)
    TextView tvFirstShowTime;
    @BindView(R.id.tv_already_show_times)
    TextView tvAlreadyShowTimes;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.stl_dis_tab)
    SlidingTabLayout stlDisTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.ll_make_telephone)
    LinearLayout llMakeTelephone;
    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private String mProjectId;
    private WorksDetailBean worksDetailBean;


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
            }
        }
    };


    private void setData() {

        mTabArray = getResources().getStringArray(R.array.work_detail_tab);
        mFragments = new ArrayList<>();
        WorksDetailShowFragment foundTheatreFragment = WorksDetailShowFragment.newInstance();
        StaggerFragment staggerFragment = StaggerFragment.newInstance();
        staggerFragment.setLists(worksDetailBean.getPictures());
        WorksDetailSkillFragment foundTalentsFragment = WorksDetailSkillFragment.newInstance();
        mFragments.add(foundTheatreFragment);
        mFragments.add(staggerFragment);
        mFragments.add(foundTalentsFragment);

        TalentDetailFragmentAdapter disPagerAdapter = new TalentDetailFragmentAdapter(getSupportFragmentManager(), mFragments, mTabArray);

        viewpager.setAdapter(disPagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        stlDisTab.setViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollableLayout.setCurrentScrollableContainer(mFragments.get(position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        scrollableLayout.setCurrentScrollableContainer(mFragments.get(0));
        viewpager.setCurrentItem(0);

//        if (!TextUtils.isEmpty(talentDetailBean.getLogo())) {
//            talentsPic.setImageURI(Uri.parse(talentDetailBean.getLogo()));
//        }
//        tvName.setText(talentDetailBean.getName());
//        String major = "";
//        if (talentDetailBean.getClassifyNames() != null && talentDetailBean.getClassifyNames().size() > 0) {
//            for (int i = 0; i < talentDetailBean.getClassifyNames().size(); i++) {
//                if (i == 0) {
//                    major = talentDetailBean.getClassifyNames().get(i);
//                } else {
//                    major = major + "/" + talentDetailBean.getClassifyNames().get(i);
//                }
//            }
//        }
//        tvMajor.setText(major);
//        tvAge.setText(talentDetailBean.getAge() + "");
//        tvWeight.setText(talentDetailBean.getWeight());
//        tvHeight.setText(talentDetailBean.getHeight());
//        tvUniversity.setText(talentDetailBean.getSchool());
//        tvOrganize.setText(talentDetailBean.getAgency());
        tvIntroduce.setText(worksDetailBean.getDescription());


    }


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_works_detail_message;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mProjectId = extras.getString(PARAMS_ID);
        }
        getWorkDetail();
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
        }
    }

    private void getWorkDetail() {
        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_REPERTORY_DETAIL==" + Constant.URL_REPERTORY_DETAIL);
        params.put("id", mProjectId);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_REPERTORY_DETAIL, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj1111=" + obj);
                        Gson gson = new Gson();
                        try {
                            worksDetailBean = gson.fromJson(obj, WorksDetailBean.class);
                            if (worksDetailBean != null) {
                                uiHandler.sendEmptyMessage(0);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess: 数据解析异常");
                        }
                    } else {
                        Log.e(TAG, "onSuccess: 数据为空");
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
