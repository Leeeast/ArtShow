package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TalentDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.TalentDetailBean;
import com.art.huakai.artshow.fragment.ErrorFragment;
import com.art.huakai.artshow.fragment.PersonalDetailAwarsFragment;
import com.art.huakai.artshow.fragment.PersonalDetailworksFragment;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.utils.AnimUtils;
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
import butterknife.ButterKnife;
import okhttp3.Call;

public class PersonalDetailMessageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.talents_pic)
    ChinaShowImageView talentsPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.iv_authentication)
    ImageView ivAuthentication;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_university)
    TextView tvUniversity;
    @BindView(R.id.tv_organize)
    TextView tvOrganize;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.stl_dis_tab)
    SlidingTabLayout stlDisTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.ll_make_telephone)
    LinearLayout llMakeTelephone;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;


    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private Handler handler = new Handler();
    private String talentId;
    private TalentDetailBean talentDetailBean;


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
                setData();

            }
        }
    };

    private void setData() {

        mTabArray = getResources().getStringArray(R.array.talent_detail_tab);
        mFragments = new ArrayList<>();
        PersonalDetailworksFragment personalDetailworksFragment = PersonalDetailworksFragment.newInstance();
        mFragments.add(personalDetailworksFragment);
        if(talentDetailBean.getPictures()!=null&&talentDetailBean.getPictures().size()>0){
            StaggerFragment staggerFragment = StaggerFragment.newInstance();
            staggerFragment.setLists(talentDetailBean.getPictures());
            mFragments.add(staggerFragment);
        }else{
            ErrorFragment errorFragment = ErrorFragment.newInstance();
            mFragments.add(errorFragment);
        }
        PersonalDetailAwarsFragment personalDetailAwarsFragment = PersonalDetailAwarsFragment.newInstance();
        mFragments.add(personalDetailAwarsFragment);
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

        if (!TextUtils.isEmpty(talentDetailBean.getLogo())) {
            talentsPic.setImageURI(Uri.parse(talentDetailBean.getLogo()));
        }
        tvName.setText(talentDetailBean.getName());
        String major = "";
        if (talentDetailBean.getClassifyNames() != null && talentDetailBean.getClassifyNames().size() > 0) {
            for (int i = 0; i < talentDetailBean.getClassifyNames().size(); i++) {
                if (i == 0) {
                    major = talentDetailBean.getClassifyNames().get(i);
                } else {
                    major = major + "/" + talentDetailBean.getClassifyNames().get(i);
                }
            }
        }
        tvMajor.setText(major);
        tvAge.setText(talentDetailBean.getAge() + "");
        tvWeight.setText(talentDetailBean.getWeight());
        tvHeight.setText(talentDetailBean.getHeight());
        tvUniversity.setText(talentDetailBean.getSchool());
        tvOrganize.setText(talentDetailBean.getAgency());
        tvIntroduce.setText(talentDetailBean.getDescription());


    }


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_personal_detail_message;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        Intent intent = getIntent();
        talentId = intent.getStringExtra("id");
        getTalentDetail();
    }


    private void getTalentDetail() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_TALENT_DETAIL==" + Constant.URL_TALENT_DETAIL);
        params.put("id", talentId);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_TALENT_DETAIL, params, 120, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj1111=" + obj);
                        Gson gson = new Gson();
                        try {
                            talentDetailBean = gson.fromJson(obj, TalentDetailBean.class);
                            if (talentDetailBean != null) {
                                uiHandler.sendEmptyMessage(0);
                                return;
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
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
            }
        });
    }


}
