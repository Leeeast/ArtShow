package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorksDetailMessageActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.ll_make_telephone)
    LinearLayout llMakeTelephone;

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
    }
}
