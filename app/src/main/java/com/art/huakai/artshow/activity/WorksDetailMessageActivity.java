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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TalentDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.WorksDetailBean;
import com.art.huakai.artshow.fragment.ErrorFragment;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.fragment.ProjectDetailPoltFragment;
import com.art.huakai.artshow.fragment.ProjectDetailRequireFragment;
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
import butterknife.OnClick;
import okhttp3.Call;

public class WorksDetailMessageActivity extends BaseActivity implements View.OnClickListener {
    public static final String PARAMS_ID = "PARAMS_ID";
    public static final String PARAMS_ORG = "PARAMS_ORG";
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
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
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private String mProjectId;
    private WorksDetailBean worksDetailBean;
    private boolean mIsFromOrgan;
    private String URL_TALENT_DETAL;


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
    private ShareDialog shareDialog;


    private void setData() {

        mTabArray = getResources().getStringArray(R.array.work_detail_tab);
        mFragments = new ArrayList<>();
        ProjectDetailPoltFragment worksDetailShowFragment = ProjectDetailPoltFragment.newInstance(worksDetailBean);
        mFragments.add(worksDetailShowFragment);
        if (worksDetailBean.getPictures() != null && worksDetailBean.getPictures().size() > 0) {
            StaggerFragment staggerFragment = StaggerFragment.newInstance();
            staggerFragment.setLists(worksDetailBean.getPictures());
            mFragments.add(staggerFragment);
        } else {
            ErrorFragment errorFragment = ErrorFragment.newInstance();
            mFragments.add(errorFragment);
        }

        ProjectDetailRequireFragment worksDetailSkillFragment = ProjectDetailRequireFragment.newInstance(worksDetailBean);
        mFragments.add(worksDetailSkillFragment);
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
            mIsFromOrgan = extras.getBoolean(PARAMS_ORG, false);
        }
        if (mIsFromOrgan) {
            URL_TALENT_DETAL = Constant.URL_USER_REPERTORY_DETAIL;
        } else {
            URL_TALENT_DETAL = Constant.URL_REPERTORY_DETAIL;
        }
        getWorkDetail();
    }

    @Override
    public void initView() {

        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);
    }

    @Override
    public void setView() {
        ivRightImg.setImageResource(R.mipmap.icon_share_gray);
        if (mIsFromOrgan) {
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private void getWorkDetail() {
        Map<String, String> params = new TreeMap<>();
        params.put("id", mProjectId);
        if (mIsFromOrgan) {
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        }
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.i(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, URL_TALENT_DETAL, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.i(TAG, "onSuccess: obj=" + obj);
                        Gson gson = new Gson();
                        try {
                            worksDetailBean = gson.fromJson(obj, WorksDetailBean.class);
                            if (worksDetailBean != null) {
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

    @OnClick(R.id.fly_right_img)
    public void shareProject() {
        if (shareDialog == null) {
            shareDialog = ShareDialog.newInstence();
        }
        shareDialog.show(getSupportFragmentManager(), "SHARE.DIALOG");
    }

    @OnClick(R.id.btn_edit)
    public void jump2ProjectEdit() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProjectEditActivity.PARAMS_NEW, false);
        invokActivity(this, ProjectEditActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_EDIT);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void back() {
        finish();
    }
}
