package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.MainActivity;
import com.art.huakai.artshow.adapter.CircleShowAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.FrescoHelper;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.widget.SmartRecyclerview;
import com.google.gson.Gson;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 演圈Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class ShowCircleTestFragment extends BaseFragment implements View.OnClickListener , SmartRecyclerview.LoadingListener{
    //Frament添加TAG
    public static final String TAG_FRAGMENT = ShowCircleTestFragment.class.getSimpleName();
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.scv_content)
    SmartRecyclerview recyclerView;
    private HomePageDetails homePageDetails;
    private CircleShowAdapter circleShowAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isReferesh=false;
    private boolean isLoadingData=false;
     MainActivity mainActivity;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
            }
        }
    };

    public ShowCircleTestFragment() {
        // Required empty public constructor
    }

    public static ShowCircleTestFragment newInstance() {
        ShowCircleTestFragment fragment = new ShowCircleTestFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mainActivity= (MainActivity) getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            if (homePageDetails == null) {
//                getHomePageDetails();
//            } else {
//                if (banner != null) {
//                    if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 1) {
//                        banner.startAutoScroll();
//                    }
//                }
//            }
//            if (scrollView != null) {
//                if (scrollDistance == 0) {
//                    scrollView.smoothScrollTo(0, 0);
//                }
//            }
//        } else {
//            if (banner != null && homePageDetails != null) {
//                if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 1) {
//                    banner.stopAutoScroll();
//                }
//            }
//            scrollDistance = scrollView.getScrollY();
//        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_show_circle_test;
    }

    @Override
    public void initView(View rootView) {
        //处理状态栏遮挡问题
//        LinearLayout llyContent = (LinearLayout) rootView.findViewById(R.id.lly_content);
        int statusBarHeight = DeviceUtils.getStatusBarHeight(getContext());
        recyclerView.setPadding(0, -statusBarHeight, 0, 0);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setLoadingListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    FrescoHelper.resume();
                }else{
                    FrescoHelper.pause();
                }

            }
        });
    }

    @Override
    public void setView() {
        initData();
    }

    private void initData() {
        getHomePageDetails();
    }

    private void getHomePageDetails() {
        if(isLoadingData)return;
        isLoadingData=true;
        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_HOMEPAGE_INFOS==" + Constant.URL_GET_HOMEPAGE_INFOS);
        RequestUtil.request(true, Constant.URL_GET_HOMEPAGE_INFOS, params, 100, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                isLoadingData=false;
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Gson gson = new Gson();
                        homePageDetails = gson.fromJson(obj, HomePageDetails.class);
                        Log.e(TAG, "onSuccess: getid" + homePageDetails.getAdvert().getLogo());
                        if(isReferesh){
                            if(ivLoading==null){
                                return;
                            }
                            recyclerView.refreshComplete();
                            Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                            isReferesh=false;
                        }
                        uiHandler.sendEmptyMessage(0);
                        return;
                    } else {
                        Log.e(TAG, "onSuccess: obj=nullnull");
                    }
                } else {
                    Log.e(TAG, "onSuccess: code==" + code);
//                    ResponseCodeCheck.showErrorMsg(code);
                }
                if(isReferesh){
                    Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    isReferesh=false;
                }else{
                    if(ivLoading==null){
                        return;
                    }
                    recyclerView.refreshComplete();
                    ivLoading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                isLoadingData=false;
                if(isReferesh){
                    Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    isReferesh=false;
                }else{
                    if(ivLoading==null){
                        return;
                    }
                    recyclerView.refreshComplete();
                    ivLoading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                }
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
            }
        });
    }


    private void setData() {

        circleShowAdapter =new CircleShowAdapter(getContext(),homePageDetails,mainActivity,this);
        linearLayoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(circleShowAdapter);

    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: 11111111111");
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (v.getId() == R.id.tv_one_whole) {
//            if (null != mainActivity) {
//
//                Intent intent = new Intent(getContext(), KeywordSearchNewsActivity.class);
//                intent.putExtra("searchType", "news");
//                intent.putExtra("keyword", "*");
//                startActivity(intent);
//
//            }
//        } else if (v.getId() == R.id.tv_two_whole) {
//            if (null != mainActivity) {
//                mainActivity.setWholeItemPosition(2);
//                mainActivity.setCheckId(R.id.rdobtn_collaborate);
//            }
//        } else if (v.getId() == R.id.tv_three_whole) {
//            if (null != mainActivity) {
//                mainActivity.setWholeItemPosition(3);
//                mainActivity.setCheckId(R.id.rdobtn_discover);
//            }
//
//        } else if (v.getId() == R.id.tv_four_whole) {
//            if (null != mainActivity) {
//                mainActivity.setWholeItemPosition(4);
//                mainActivity.setCheckId(R.id.rdobtn_discover);
//            }
//
//        } else if (v.getId() == R.id.tv_five_whole) {
//            if (null != mainActivity) {
//                mainActivity.setWholeItemPosition(5);
//                mainActivity.setCheckId(R.id.rdobtn_discover);
//            }
//
//        } else if (v.getId() == R.id.iv_search) {
//            Intent intent = new Intent(getContext(), KeywordSearchAllActivity.class);
//            startActivity(intent);
//        }else if(v.getId()==R.id.csiv){
//            if(homePageDetails!=null&&homePageDetails.getAdvert()!=null){
//                AdvertJumpUtil.invoke((BaseActivity) getActivity(),getContext(),homePageDetails.getAdvert());
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onRefresh() {
        isReferesh=true;
        getHomePageDetails();
    }

    @Override
    public void onLoadMore() {

    }
}
