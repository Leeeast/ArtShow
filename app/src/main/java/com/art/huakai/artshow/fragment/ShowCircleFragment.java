package com.art.huakai.artshow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.CooperationOpportunitiesAdapter;
import com.art.huakai.artshow.adapter.ExcellentWorksAdapter;
import com.art.huakai.artshow.adapter.IndustryNewsAdapter;
import com.art.huakai.artshow.adapter.ProfessionalPersonAdapter;
import com.art.huakai.artshow.adapter.RecommendTheaterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.widget.ChinaShowImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 演圈Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class ShowCircleFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = ShowCircleFragment.class.getSimpleName();

    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.home_page_top_bg)
    ChinaShowImageView homePageTopBg;
    @BindView(R.id.tv_home_page_top_title)
    TextView tvHomePageTopTitle;
    @BindView(R.id.tv_home_page_top_director)
    TextView tvHomePageTopDirector;
    @BindView(R.id.tv_home_page_top_festival)
    TextView tvHomePageTopFestival;
    @BindView(R.id.tv_one_title)
    TextView tvOneTitle;
    @BindView(R.id.tv_one_whole)
    TextView tvOneWhole;
    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.rcv_one)
    RecyclerView rcvOne;
    ArrayList<String> lists = new ArrayList<>();
    IndustryNewsAdapter typeOneAdapter;
    LinearLayoutManager linearLayoutManagerOne;


    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.tv_two_title)
    TextView tvTwoTitle;
    @BindView(R.id.tv_two_whole)
    TextView tvTwoWhole;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.rcv_two)
    RecyclerView rcvTwo;
    CooperationOpportunitiesAdapter typeTwoAdapter;
    LinearLayoutManager linearLayoutManagerTwo;
    LinearItemDecoration linearItemDecorationTwo;


    @BindView(R.id.tv_three_title)
    TextView tvThreeTitle;
    @BindView(R.id.tv_three_whole)
    TextView tvThreeWhole;
    @BindView(R.id.rl_three)
    RelativeLayout rlThree;
    @BindView(R.id.rcv_three)
    RecyclerView rcvThree;
    LinearLayoutManager linearLayoutManagerThree;
    ExcellentWorksAdapter excellentWorksAdapter;
    LinearItemDecoration linearItemDecorationThree;


    @BindView(R.id.tv_four_title)
    TextView tvFourTitle;
    @BindView(R.id.tv_four_whole)
    TextView tvFourWhole;
    @BindView(R.id.rl_four)
    RelativeLayout rlFour;
    @BindView(R.id.rcv_four)
    RecyclerView rcvFour;
    LinearLayoutManager linearLayoutManagerFour;
    RecommendTheaterAdapter recommendTheaterAdapter;
    LinearItemDecoration linearItemDecorationFour;

    @BindView(R.id.tv_five_title)
    TextView tvFiveTitle;
    @BindView(R.id.tv_five_whole)
    TextView tvFiveWhole;
    @BindView(R.id.rl_five)
    RelativeLayout rlFive;
    @BindView(R.id.rcv_five)
    RecyclerView rcvFive;
    LinearLayoutManager linearLayoutManagerFive;
    ProfessionalPersonAdapter professionalPersonAdapter;
    LinearItemDecoration linearItemDecorationFive;


    @BindView(R.id.csiv)
    ChinaShowImageView csiv;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_eg_name)
    TextView tvEgName;
    @BindView(R.id.tv_ch_name)
    TextView tvChName;

    public ShowCircleFragment() {
        // Required empty public constructor
    }

    public static ShowCircleFragment newInstance() {
        ShowCircleFragment fragment = new ShowCircleFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_show_circle;
    }

    @Override
    public void initView(View rootView) {
        //处理状态栏遮挡问题
        LinearLayout llyContent = (LinearLayout) rootView.findViewById(R.id.lly_content);
        int statusBarHeight = DeviceUtils.getStatusBarHeight(getContext());
        llyContent.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    public void setView() {

        initData();
    }

    private void initData() {
        lists.clear();
        for (int i = 0; i < 8; i++) {
            lists.add("position=" + i);
        }
        if (null == typeOneAdapter) {
            typeOneAdapter = new IndustryNewsAdapter(getContext(), lists);
        }
        if (null == linearLayoutManagerOne) {
            linearLayoutManagerOne = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
//      实现屏蔽recyclerview的滑动效果
        rcvOne.setNestedScrollingEnabled(false);
//        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(10);
        rcvOne.addItemDecoration(itemDecoration);
        rcvOne.setLayoutManager(linearLayoutManagerOne);
        rcvOne.setAdapter(typeOneAdapter);


        if (null == typeTwoAdapter) {
            typeTwoAdapter = new CooperationOpportunitiesAdapter(getContext(), lists);
            linearLayoutManagerTwo = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        rcvTwo.setNestedScrollingEnabled(false);
        if (null == linearItemDecorationTwo) {
            linearItemDecorationTwo = new LinearItemDecoration(10);
        }
        rcvTwo.addItemDecoration(linearItemDecorationTwo);
        rcvTwo.setLayoutManager(linearLayoutManagerTwo);
        rcvTwo.setAdapter(typeTwoAdapter);


        if (null == excellentWorksAdapter) {
            excellentWorksAdapter = new ExcellentWorksAdapter(getContext(), lists);
            linearLayoutManagerThree = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        }
//        rcvThree.setNestedScrollingEnabled(false);
        if (null == linearItemDecorationThree) {
            linearItemDecorationThree = new LinearItemDecoration(10);
        }
        rcvThree.addItemDecoration(linearItemDecorationThree);
        rcvThree.setLayoutManager(linearLayoutManagerThree);
        rcvThree.setAdapter(excellentWorksAdapter);

        if (null == recommendTheaterAdapter) {
            recommendTheaterAdapter = new RecommendTheaterAdapter(getContext(), lists);
            linearLayoutManagerFour = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        }
        if (null == linearItemDecorationFour) {
            linearItemDecorationFour = new LinearItemDecoration(10);
        }
        rcvFour.addItemDecoration(linearItemDecorationFour);
        rcvFour.setLayoutManager(linearLayoutManagerFour);
        rcvFour.setAdapter(recommendTheaterAdapter);

        if (null == professionalPersonAdapter) {
            professionalPersonAdapter = new ProfessionalPersonAdapter(getContext(), lists);
            linearLayoutManagerFive = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        }
        if (null == linearItemDecorationFive) {
            linearItemDecorationFive = new LinearItemDecoration(10);
        }
        rcvFive.addItemDecoration(linearItemDecorationFive);
        rcvFive.setLayoutManager(linearLayoutManagerFive);
        rcvFive.setAdapter(professionalPersonAdapter);

        csiv.setImageURI(Uri.parse("asset:///test.png"));


//      实现不在除此加载界面的时候显示recyclerview的第一个item
        sv.smoothScrollTo(0, 0);


    }

}
