package com.art.huakai.artshow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.MainActivity;
import com.art.huakai.artshow.adapter.CooperationOpportunitiesAdapter;
import com.art.huakai.artshow.adapter.ExcellentWorksAdapter;
import com.art.huakai.artshow.adapter.IndustryNewsAdapter;
import com.art.huakai.artshow.adapter.ProfessionalPersonAdapter;
import com.art.huakai.artshow.adapter.RecommendTheaterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.BannersBean;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.entity.NewsesBean;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.entity.TalentClassifyMessage;
import com.art.huakai.artshow.entity.TalentsBean;
import com.art.huakai.artshow.entity.TheatersBean;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.flyco.tablayout.widget.MsgView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 演圈Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class ShowCircleFragment extends BaseFragment implements View.OnClickListener {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = ShowCircleFragment.class.getSimpleName();
    private HomePageDetails homePageDetails;

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





    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                setData();

            }
        }
    };

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
        tvOneWhole.setOnClickListener(this);
        tvTwoWhole.setOnClickListener(this);
        tvThreeWhole.setOnClickListener(this);
        tvFourWhole.setOnClickListener(this);
        tvFiveWhole.setOnClickListener(this);

    }

    private void initData() {
        getHomePageDetails();
//        getList();
    }


    private void getHomePageDetails() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_HOMEPAGE_INFOS==" + Constant.URL_GET_HOMEPAGE_INFOS);
        RequestUtil.request(true, Constant.URL_GET_HOMEPAGE_INFOS, params, 100, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    Gson gson = new Gson();
                    homePageDetails = gson.fromJson(obj, HomePageDetails.class);
                    Log.e(TAG, "onSuccess: getid" + homePageDetails.getAdvert().getId());
                    Log.e(TAG, "onSuccess: getid" + homePageDetails.getAdvert().getLogo());
                uiHandler.sendEmptyMessage(0);
            } else {
                Log.e(TAG, "onSuccess: code=="+code );
                ResponseCodeCheck.showErrorMsg(code);
            }
            }
            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);

            }
        });

    }


    private void setData() {

        if(homePageDetails==null){
            Log.e(TAG, "setData: nullnull" );
            return;
        }
        if (homePageDetails.getNewses() != null&&homePageDetails.getNewses().size()>0) {
            if (null == typeOneAdapter) {
                typeOneAdapter = new IndustryNewsAdapter(getContext(), homePageDetails.getNewses());
                if (null == linearLayoutManagerOne) {
                    linearLayoutManagerOne = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                }
                typeOneAdapter.setOnItemClickListener(new IndustryNewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Log.e(TAG, "onItemClickListener: position=="+position );
                    }
                });
//              实现屏蔽recyclerview的滑动效果
                rcvOne.setNestedScrollingEnabled(false);
//              gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                LinearItemDecoration itemDecoration = new LinearItemDecoration(10);
                rcvOne.addItemDecoration(itemDecoration);
                rcvOne.setLayoutManager(linearLayoutManagerOne);
                rcvOne.setAdapter(typeOneAdapter);
            }
        }else{
            rcvOne.setVisibility(View.GONE);
            rlOne.setVisibility(View.GONE);
        }

        if(homePageDetails.getEnrolls()!=null&&homePageDetails.getEnrolls().size()>0){
            if (null == typeTwoAdapter) {
                typeTwoAdapter = new CooperationOpportunitiesAdapter(getContext(), homePageDetails.getEnrolls());
                linearLayoutManagerTwo = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
            rcvTwo.setNestedScrollingEnabled(false);
            if (null == linearItemDecorationTwo) {
                linearItemDecorationTwo = new LinearItemDecoration(10);
            }
            typeTwoAdapter.setOnItemClickListener(new CooperationOpportunitiesAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position=="+position );
                }
            });
            rcvTwo.addItemDecoration(linearItemDecorationTwo);
            rcvTwo.setLayoutManager(linearLayoutManagerTwo);
            rcvTwo.setAdapter(typeTwoAdapter);
        }else{
            rcvTwo.setVisibility(View.GONE);
            rlTwo.setVisibility(View.GONE);
        }

        if(homePageDetails.getRepertorys()!=null&&homePageDetails.getRepertorys().size()>0){
            if (null == excellentWorksAdapter) {
                excellentWorksAdapter = new ExcellentWorksAdapter(getContext(), homePageDetails.getRepertorys());
                linearLayoutManagerThree = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
//        rcvThree.setNestedScrollingEnabled(false);
            if (null == linearItemDecorationThree) {
                linearItemDecorationThree = new LinearItemDecoration(10);
            }
            excellentWorksAdapter.setOnItemClickListener(new ExcellentWorksAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                    Log.e(TAG, "onItemClickListener: position=="+position );

                }
            });
            rcvThree.addItemDecoration(linearItemDecorationThree);
            rcvThree.setLayoutManager(linearLayoutManagerThree);
            rcvThree.setAdapter(excellentWorksAdapter);
        }else{
            rcvThree.setVisibility(View.GONE);
            rlThree.setVisibility(View.GONE);
        }


        if(homePageDetails.getTheaters()!=null&&homePageDetails.getTheaters().size()>0){
            if (null == recommendTheaterAdapter) {
                recommendTheaterAdapter = new RecommendTheaterAdapter(getContext(), homePageDetails.getTheaters());
                linearLayoutManagerFour = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
            if (null == linearItemDecorationFour) {
                linearItemDecorationFour = new LinearItemDecoration(10);
            }
            recommendTheaterAdapter.setOnItemClickListener(new RecommendTheaterAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position=="+position );
                }
            });
            rcvFour.addItemDecoration(linearItemDecorationFour);
            rcvFour.setLayoutManager(linearLayoutManagerFour);
            rcvFour.setAdapter(recommendTheaterAdapter);
        }else{
            rcvFour.setVisibility(View.GONE);
            rlFour.setVisibility(View.GONE);
        }


        if(homePageDetails.getTalents()!=null&&homePageDetails.getTalents().size()>0){
            if (null == professionalPersonAdapter) {
                professionalPersonAdapter = new ProfessionalPersonAdapter(getContext(), homePageDetails.getTalents());
                linearLayoutManagerFive = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
            if (null == linearItemDecorationFive) {
                linearItemDecorationFive = new LinearItemDecoration(10);
            }
            professionalPersonAdapter.setOnItemClickListener(new ProfessionalPersonAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position"+position );
                }
            });
            rcvFive.addItemDecoration(linearItemDecorationFive);
            rcvFive.setLayoutManager(linearLayoutManagerFive);
            rcvFive.setAdapter(professionalPersonAdapter);
        }else{
            rcvFive.setVisibility(View.GONE);
            rlFive.setVisibility(View.GONE);
        }


        csiv.setImageURI(Uri.parse("asset:///test.png"));
//        csiv.setImageURI(Uri.parse("file:///storage/emulated/0/DCIM/Camera/IMG_20171002_150026.jpg"));
//      实现不在除此加载界面的时候显示recyclerview的第一个item
        sv.smoothScrollTo(0, 0);

    }

    @Override
    public void onClick(View v) {
        MainActivity mainActivity= (MainActivity) getActivity();

        if(v.getId()==R.id.tv_one_whole){
            if(null!=mainActivity){
                mainActivity.setWholeItemPosition(1);
                mainActivity.setCheckId(R.id.rdobtn_me);
            }
        }else if(v.getId()==R.id.tv_two_whole){
            if(null!=mainActivity){
                mainActivity.setWholeItemPosition(2);
                mainActivity.setCheckId(R.id.rdobtn_collaborate);
            }
        }else if(v.getId()==R.id.tv_three_whole){

            if(null!=mainActivity){
                mainActivity.setWholeItemPosition(3);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        }else if(v.getId()==R.id.tv_four_whole){

            if(null!=mainActivity){
                mainActivity.setWholeItemPosition(4);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        }else if(v.getId()==R.id.tv_five_whole){

            if(null!=mainActivity){
                mainActivity.setWholeItemPosition(5);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        }

    }


//    private void getList(){
//
//        Map<String, String> params = new TreeMap<>();
//        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_CLASSFY_LIST);
//
////        repertory  type  就是获取剧场下面的类型
//        params.put("type", "talent");
//        String sign = SignUtil.getSign(params);
//        params.put("sign", sign);
//        Log.e(TAG, "getList: sign=="+sign );
//        RequestUtil.request(true, Constant.URL_GET_CLASSFY_LIST, params, 101, new RequestUtil.RequestListener() {
//            @Override
//            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
//                LogUtil.i(TAG, obj);
//                    if (isSuccess) {
//                        Gson gson = new Gson();
//                        TalentClassifyMessage talent = gson.fromJson(obj, TalentClassifyMessage.class);
//
//                         Log.e(TAG, "onSuccess: obj=="+obj );
//
//                         uiHandler.sendEmptyMessage(0);
//                } else {
//                    ResponseCodeCheck.showErrorMsg(code);
//                }
//            }
//
//            @Override
//            public void onFailed(Call call, Exception e, int id) {
//                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
//
//            }
//        });
//    }



}
