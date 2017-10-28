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
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TalentDetailFragmentAdapter;
import com.art.huakai.artshow.adapter.TheatreDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.TalentDetailBean;
import com.art.huakai.artshow.entity.TheatersBean;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.fragment.PersonalDetailAwarsFragment;
import com.art.huakai.artshow.fragment.PersonalDetailworksFragment;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.fragment.TheatreDetailParamsFragment;
import com.art.huakai.artshow.fragment.TheatreDetailTheatreFragment;
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

public class TheatreDetailMessageActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tv_seat_count)
    TextView tvSeatCount;
    @BindView(R.id.ll_check_ticket_area)
    LinearLayout llCheckTicketArea;
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


    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private Handler handler = new Handler();
    private String theatreId;
    private TheatreDetailBean theatreDetailBean;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                setData();

            }
        }
    };


    private void setData() {

        mTabArray = getResources().getStringArray(R.array.theatre_detail_tab);
        mFragments = new ArrayList<HeaderViewPagerFragment>();
        TheatreDetailTheatreFragment foundTheatreFragment = TheatreDetailTheatreFragment.newInstance();
        StaggerFragment staggerFragment = StaggerFragment.newInstance();
        staggerFragment.setLists(theatreDetailBean.getPictures());
        TheatreDetailParamsFragment foundTalentsFragment = TheatreDetailParamsFragment.newInstance();
        mFragments.add(foundTheatreFragment);
        mFragments.add(staggerFragment);
        mFragments.add(foundTalentsFragment);

        TheatreDetailFragmentAdapter disPagerAdapter = new TheatreDetailFragmentAdapter(getSupportFragmentManager(), mFragments, mTabArray);

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

        if (!TextUtils.isEmpty(theatreDetailBean.getLogo())) {
            sdv.setImageURI(Uri.parse(theatreDetailBean.getLogo()));
        }
        tvTheatreName.setText(theatreDetailBean.getName());

        tvFee.setText(theatreDetailBean.getExpense() + "");
        tvSeatCount.setText(theatreDetailBean.getSeating() + "");
//        tvWeight.setText(talentDetailBean.getWeight());
//        tvHeight.setText(talentDetailBean.getHeight());
//        tvUniversity.setText(talentDetailBean.getSchool());
//        tvOrganize.setText(talentDetailBean.getAgency());
        tvIntroduce.setText(theatreDetailBean.getDescription());


    }

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_detail_message;
    }

    @Override
    public void initData() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        Intent intent = getIntent();
        theatreId = intent.getStringExtra("id");
        getTheatreDetail();

    }


    private void getTheatreDetail() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_THEATRE_DETAIL==" + Constant.URL_THEATER_DETAIL);
        params.put("id", theatreId);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_THEATER_DETAIL, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj1111=" + obj);
                        Gson gson = new Gson();
                        try {
                            theatreDetailBean = gson.fromJson(obj, TheatreDetailBean.class);
                            if (theatreDetailBean != null) {
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
