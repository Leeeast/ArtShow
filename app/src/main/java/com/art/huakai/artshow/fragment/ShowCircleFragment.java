package com.art.huakai.artshow.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.AllNewsShowActivity;
import com.art.huakai.artshow.activity.EnrollDetailActivity;
import com.art.huakai.artshow.activity.KeywordSearchAllActivity;
import com.art.huakai.artshow.activity.KeywordSearchNewsActivity;
import com.art.huakai.artshow.activity.MainActivity;
import com.art.huakai.artshow.activity.NewsDetailActivity;
import com.art.huakai.artshow.activity.PersonalDetailMessageActivity;
import com.art.huakai.artshow.activity.TheatreDetailMessageActivity;
import com.art.huakai.artshow.activity.WorksDetailMessageActivity;
import com.art.huakai.artshow.adapter.CooperationOpportunitiesAdapter;
import com.art.huakai.artshow.adapter.ExcellentWorksAdapter;
import com.art.huakai.artshow.adapter.IndustryNewsAdapter;
import com.art.huakai.artshow.adapter.IndustryNewsOneAdapter;
import com.art.huakai.artshow.adapter.ProfessionalPersonAdapter;
import com.art.huakai.artshow.adapter.RecommendTheaterAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.utils.AdvertJumpUtil;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.art.huakai.artshow.widget.PullToRefreshScroll.PullToRefreshLayout;
import com.art.huakai.artshow.widget.PullToRefreshScroll.PullableScrollView;
import com.art.huakai.artshow.widget.banner.BannerEntity;
import com.art.huakai.artshow.widget.banner.BannerView;
import com.art.huakai.artshow.widget.banner.OnBannerClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 演圈Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class ShowCircleFragment extends BaseFragment implements View.OnClickListener {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = ShowCircleFragment.class.getSimpleName();
    @BindView(R.id.news_divider)
    View newsDivider;
    @BindView(R.id.cooperation_divider)
    View cooperationDivider;
    @BindView(R.id.works_divider)
    View worksDivider;
    @BindView(R.id.theatre_divider)
    View theatreDivider;
    @BindView(R.id.talents_divider)
    View talentsDivider;
    @BindView(R.id.banner)
    BannerView banner;
    Unbinder unbinder;
    @BindView(R.id.pull)
    PullToRefreshLayout pull;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.rcv_six)
    RecyclerView rcvSix;
    @BindView(R.id.lly_content)
    LinearLayout llyContent;

    private HomePageDetails homePageDetails;

    @BindView(R.id.search)
    LinearLayout ivSearch;
    @BindView(R.id.tv_one_title)
    TextView tvIndustryNewsTitle;
    @BindView(R.id.tv_one_whole)
    TextView tvIndustryNewsWhole;
    @BindView(R.id.rl_one)
    RelativeLayout rlIndustryNews;
    @BindView(R.id.rcv_one)
    RecyclerView rcvIndustryNews;
    IndustryNewsOneAdapter industryNewsAdapter;
    LinearLayoutManager industryNewsLayoutManager;

    IndustryNewsAdapter industryNewsAdapterTwo;
    LinearLayoutManager industryNewsLayoutManagerTwo;

    @BindView(R.id.sv)
    PullableScrollView scrollView;
    @BindView(R.id.tv_two_title)
    TextView tvCooperationTitle;
    @BindView(R.id.tv_two_whole)
    TextView tvCooperationWhole;
    @BindView(R.id.rl_two)
    RelativeLayout rlCooperation;
    @BindView(R.id.rcv_two)
    RecyclerView rcvCooperation;
    CooperationOpportunitiesAdapter cooperationAdapter;
    LinearLayoutManager cooperationLayoutManager;
    LinearItemDecoration cooperationItemDecoration;

    @BindView(R.id.tv_three_title)
    TextView tvWorksTitle;
    @BindView(R.id.tv_three_whole)
    TextView tvWorksWhole;
    @BindView(R.id.rl_three)
    RelativeLayout rlWorks;
    @BindView(R.id.rcv_three)
    RecyclerView rcvWorks;
    LinearLayoutManager worksLayoutManager;
    ExcellentWorksAdapter excellentWorksAdapter;
    LinearItemDecoration worksItemDecorationThree;

    @BindView(R.id.tv_four_title)
    TextView tvTheatreTitle;
    @BindView(R.id.tv_four_whole)
    TextView tvTheatreWhole;
    @BindView(R.id.rl_four)
    RelativeLayout rlTheatre;
    @BindView(R.id.rcv_four)
    RecyclerView rcvTheatre;
    LinearLayoutManager theatreLayoutManagerFour;
    RecommendTheaterAdapter recommendTheaterAdapter;
    LinearItemDecoration theatreItemDecoration;

    @BindView(R.id.tv_five_title)
    TextView tvProfessionalTitle;
    @BindView(R.id.tv_five_whole)
    TextView tvProfessionalWhole;
    @BindView(R.id.rl_five)
    RelativeLayout rlProfessional;
    @BindView(R.id.rcv_five)
    RecyclerView rcvProfesional;
    LinearLayoutManager professionalLayoutManager;
    ProfessionalPersonAdapter professionalPersonAdapter;
    LinearItemDecoration professionalItemDecoration;

    @BindView(R.id.csiv)
    ChinaShowImageView chinaShowImageView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_eg_name)
    TextView tvEgName;
    @BindView(R.id.tv_ch_name)
    TextView tvChName;
    private int scrollDistance;
    final List<BannerEntity> entities = new ArrayList<>();
    private boolean isRefresh = false;
    private boolean isLoadingData = false;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (ivLoading == null) return;
                if (isRefresh) {
//                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                }
                pull.refreshFinish(PullToRefreshLayout.SUCCEED);
                ivLoading.setVisibility(View.GONE);
                pull.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (homePageDetails == null) {
                getHomePageDetails();
            } else {
                if (banner != null) {
                    if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 1) {
                        banner.startAutoScroll();
                    }
                }
            }
            if (scrollView != null) {
                if (scrollDistance == 0) {
                    scrollView.smoothScrollTo(0, 0);
                }
            }
        } else {
            if (banner != null && homePageDetails != null) {
                if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 1) {
                    banner.stopAutoScroll();
                }
            }
            scrollDistance = scrollView.getScrollY();
        }
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
        llyContent.setPadding(0, -statusBarHeight, 0, 0);
    }

    @Override
    public void setView() {
        initData();
        tvIndustryNewsWhole.setOnClickListener(this);
        tvCooperationWhole.setOnClickListener(this);
        tvWorksWhole.setOnClickListener(this);
        tvTheatreWhole.setOnClickListener(this);
        tvProfessionalWhole.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
//        scrollView.setVisibility(View.GONE);
        chinaShowImageView.setOnClickListener(this);
        pull.setOnPullListener(new MyPullListener());
        pull.setPullUpEnable(false);
    }

    private void initData() {
        getHomePageDetails();

        ivLoading.setVisibility(View.VISIBLE);
        ivNoContent.setVisibility(View.GONE);
        pull.setVisibility(View.GONE);
        AnimUtils.rotate(ivLoading);
//        getList();
    }

    private void getHomePageDetails() {
        if (isLoadingData) return;
        isLoadingData = true;
        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_HOMEPAGE_INFOS==" + Constant.URL_GET_HOMEPAGE_INFOS);
        RequestUtil.request(true, Constant.URL_GET_HOMEPAGE_INFOS, params, 100, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                isLoadingData = false;
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Gson gson = new Gson();
                        homePageDetails = gson.fromJson(obj, HomePageDetails.class);
                        Log.e(TAG, "onSuccess: getid" + homePageDetails.getAdvert().getLogo());
                        uiHandler.sendEmptyMessage(0);
                        return;
                    } else {
                        Log.e(TAG, "onSuccess: obj=nullnull");
                    }
                } else {
                    Log.e(TAG, "onSuccess: code==" + code);
                    ResponseCodeCheck.showErrorMsg(code);
                }
                if (ivLoading == null) return;
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
                pull.setVisibility(View.GONE);
                if (isRefresh) {
                    Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        pull.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                });
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (ivLoading == null) return;
                isLoadingData = false;
                ivLoading.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
                pull.setVisibility(View.GONE);
                if (isRefresh) {
                    Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    isRefresh = false;
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        pull.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                });
            }
        });
    }


    private void setData() {

        if (homePageDetails == null) {
            Log.e(TAG, "setData: nullnull");
            return;
        }

        if (homePageDetails.getNewsesLine() != null && homePageDetails.getNewsesLine().size() > 0) {
            if (null == industryNewsAdapter) {
                industryNewsAdapter = new IndustryNewsOneAdapter(getContext(), homePageDetails.getNewsesLine());
            }
            if (null == industryNewsLayoutManager) {
                industryNewsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
            industryNewsAdapter.setOnItemClickListener(new IndustryNewsOneAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString(NewsDetailActivity.PARAMS_NEWS_ID, homePageDetails.getNewsesLine().get(position).getId());
                    invokActivity(getContext(), NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);
                }
            });
//              实现屏蔽recyclerview的滑动效果
            rcvIndustryNews.setNestedScrollingEnabled(false);
//              gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            LinearItemDecoration itemDecoration = new LinearItemDecoration(10);
            rcvIndustryNews.addItemDecoration(itemDecoration);
            rcvIndustryNews.setLayoutManager(industryNewsLayoutManager);
            rcvIndustryNews.setAdapter(industryNewsAdapter);
        } else {
            rcvIndustryNews.setVisibility(View.GONE);
            rlIndustryNews.setVisibility(View.GONE);
            newsDivider.setVisibility(View.GONE);
        }

        Log.e(TAG, "setData:getNewsesColumn== "+homePageDetails.getNewsesColumn().size() );
        if(homePageDetails.getNewsesColumn() != null && homePageDetails.getNewsesColumn().size() > 0){
            if (null == industryNewsAdapterTwo) {
                industryNewsAdapterTwo = new IndustryNewsAdapter(getContext(), homePageDetails.getNewsesColumn());
            }
            if (null == industryNewsLayoutManagerTwo) {
                industryNewsLayoutManagerTwo = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
            industryNewsAdapterTwo.setOnItemClickListener(new IndustryNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString(NewsDetailActivity.PARAMS_NEWS_ID, homePageDetails.getNewsesColumn().get(position).getId());
                    invokActivity(getContext(), NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);
                }
            });
//              实现屏蔽recyclerview的滑动效果
            rcvSix.setNestedScrollingEnabled(false);
//              gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            LinearItemDecoration itemDecoration = new LinearItemDecoration(10);
            rcvSix.addItemDecoration(itemDecoration);
            rcvSix.setLayoutManager(industryNewsLayoutManagerTwo);
            rcvSix.setAdapter(industryNewsAdapterTwo);
        }else{
            rcvSix.setVisibility(View.GONE);
        }


        if (homePageDetails.getEnrolls() != null && homePageDetails.getEnrolls().size() > 0) {
            if (null == cooperationAdapter) {
                cooperationAdapter = new CooperationOpportunitiesAdapter(getContext(), homePageDetails.getEnrolls());
                cooperationLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
            rcvCooperation.setNestedScrollingEnabled(false);
            if (null == cooperationItemDecoration) {
                cooperationItemDecoration = new LinearItemDecoration(10);
            }
            cooperationAdapter.setOnItemClickListener(new CooperationOpportunitiesAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position==" + position);
                    if (homePageDetails.getNewses() != null && homePageDetails.getNewses().size() > position && homePageDetails.getNewses().get(position) != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("PARAMS_ENROLL_ID", homePageDetails.getNewses().get(position).getId());
                        invokActivity(getContext(), EnrollDetailActivity.class, bundle, JumpCode.FLAG_REQ_ENROLL_DETAIL);
                    }
                }
            });
            rcvCooperation.addItemDecoration(cooperationItemDecoration);
            rcvCooperation.setLayoutManager(cooperationLayoutManager);
            rcvCooperation.setAdapter(cooperationAdapter);
        } else {
            rcvCooperation.setVisibility(View.GONE);
            rlCooperation.setVisibility(View.GONE);
            cooperationDivider.setVisibility(View.GONE);

        }
        if (homePageDetails.getRepertorys() != null && homePageDetails.getRepertorys().size() > 0) {
            if (null == excellentWorksAdapter) {
                excellentWorksAdapter = new ExcellentWorksAdapter(getContext(), homePageDetails.getRepertorys());
                worksLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
//        rcvWorks.setNestedScrollingEnabled(false);
            if (null == worksItemDecorationThree) {
                worksItemDecorationThree = new LinearItemDecoration(10);
            }
            excellentWorksAdapter.setOnItemClickListener(new ExcellentWorksAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position==" + position);
                    Bundle bundle = new Bundle();
                    bundle.putString(WorksDetailMessageActivity.PARAMS_ID, homePageDetails.getRepertorys().get(position).getId());
                    invokActivity(getContext(), WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
                }
            });
            rcvWorks.addItemDecoration(worksItemDecorationThree);
            rcvWorks.setLayoutManager(worksLayoutManager);
            rcvWorks.setAdapter(excellentWorksAdapter);
        } else {
            rcvWorks.setVisibility(View.GONE);
            rlWorks.setVisibility(View.GONE);
            worksDivider.setVisibility(View.GONE);
        }


        if (homePageDetails.getTheaters() != null && homePageDetails.getTheaters().size() > 0) {
            if (null == recommendTheaterAdapter) {
                recommendTheaterAdapter = new RecommendTheaterAdapter(getContext(), homePageDetails.getTheaters());
                theatreLayoutManagerFour = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
            if (null == theatreItemDecoration) {
                theatreItemDecoration = new LinearItemDecoration(10);
            }
            recommendTheaterAdapter.setOnItemClickListener(new RecommendTheaterAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position==" + position);
                    Bundle bundle = new Bundle();
                    bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, homePageDetails.getTheaters().get(position).getId());
                    bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, false);
                    invokActivity(getContext(), TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
                }
            });
            rcvTheatre.addItemDecoration(theatreItemDecoration);
            rcvTheatre.setLayoutManager(theatreLayoutManagerFour);
            rcvTheatre.setAdapter(recommendTheaterAdapter);
        } else {
            rcvTheatre.setVisibility(View.GONE);
            rlTheatre.setVisibility(View.GONE);
            theatreDivider.setVisibility(View.GONE);
        }


        if (homePageDetails.getTalents() != null && homePageDetails.getTalents().size() > 0) {
            if (null == professionalPersonAdapter) {
                professionalPersonAdapter = new ProfessionalPersonAdapter(getContext(), homePageDetails.getTalents());
                professionalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            }
            if (null == professionalItemDecoration) {
                professionalItemDecoration = new LinearItemDecoration(10);
            }
            professionalPersonAdapter.setOnItemClickListener(new ProfessionalPersonAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position" + position);
                    Bundle bundle = new Bundle();
                    bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, homePageDetails.getTalents().get(position).getId());
                    invokActivity(getContext(), PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PERSONAL);
                }
            });
            rcvProfesional.addItemDecoration(professionalItemDecoration);
            rcvProfesional.setLayoutManager(professionalLayoutManager);
            rcvProfesional.setAdapter(professionalPersonAdapter);
        } else {
            rcvProfesional.setVisibility(View.GONE);
            rlProfessional.setVisibility(View.GONE);
            talentsDivider.setVisibility(View.GONE);
        }
        if (homePageDetails.getAdvert() != null && !TextUtils.isEmpty(homePageDetails.getAdvert().getLogo())) {
//            chinaShowImageView.setImageURI(Uri.parse(homePageDetails.getAdvert().getLogo()));
            chinaShowImageView.setSpecificSizeImageUrl(Uri.parse(homePageDetails.getAdvert().getLogo()), getResources().getDimensionPixelSize(R.dimen.DIMEN_720PX) / 4, getResources().getDimensionPixelSize(R.dimen.DIMEN_155PX) / 2);
        }

//      chinaShowImageView.setImageURI(Uri.parse("file:///storage/emulated/0/DCIM/Camera/IMG_20171002_150026.jpg"));
//      实现不在除此加载界面的时候显示recyclerview的第一个item
        scrollView.smoothScrollTo(0, 0);

        entities.clear();
        if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 0) {
            for (int i = 0; i < homePageDetails.getBanners().size(); i++) {
                entities.add(new BannerEntity(0, homePageDetails.getBanners().get(i).getLogo(), "image"));
            }
        } else {
            entities.add(new BannerEntity(0, "http://logo", "image"));
        }
        banner.setEntities(entities);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "position==" + position, Toast.LENGTH_SHORT).show();
                if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > position && homePageDetails.getBanners().get(position) != null) {
                    AdvertJumpUtil.invoke((BaseActivity) getActivity(), getContext(), homePageDetails.getBanners().get(position));
                }
            }
        });
        if (homePageDetails.getBanners().size() > 1) {
            banner.startAutoScroll();
        }

//        uiHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                FrescoHelper.pause();
//            }
//        },5000);

    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: 11111111111");
        MainActivity mainActivity = (MainActivity) getActivity();
        if (v.getId() == R.id.tv_one_whole) {
            if (null != mainActivity) {

                Intent intent = new Intent(getContext(), AllNewsShowActivity.class);
                intent.putExtra("searchType", "news");
                intent.putExtra("keyword", "*");
                startActivity(intent);

            }
        } else if (v.getId() == R.id.tv_two_whole) {
            if (null != mainActivity) {
                mainActivity.setWholeItemPosition(2);
                mainActivity.setCheckId(R.id.rdobtn_collaborate);
            }
        } else if (v.getId() == R.id.tv_three_whole) {
            if (null != mainActivity) {
                mainActivity.setWholeItemPosition(3);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        } else if (v.getId() == R.id.tv_four_whole) {
            if (null != mainActivity) {
                mainActivity.setWholeItemPosition(4);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        } else if (v.getId() == R.id.tv_five_whole) {
            if (null != mainActivity) {
                mainActivity.setWholeItemPosition(5);
                mainActivity.setCheckId(R.id.rdobtn_discover);
            }

        } else if (v.getId() == R.id.csiv) {
            if (homePageDetails != null && homePageDetails.getAdvert() != null) {
                AdvertJumpUtil.invoke((BaseActivity) getActivity(), getContext(), homePageDetails.getAdvert());
            }
        } else if (v.getId() == R.id.search) {
            Intent intent = new Intent(getContext(), KeywordSearchAllActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        Log.d(TAG, "onCreateView:360px== " + getResources().getDimensionPixelSize(R.dimen.DIMEN_360PX));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class MyPullListener implements PullToRefreshLayout.OnPullListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            isRefresh = true;
            getHomePageDetails();

//            // 下拉刷新操作
//            new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                }
//            }.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载操作
//            new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    // 千万别忘了告诉控件加载完毕了哦！
//                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
//            }.sendEmptyMessageDelayed(0, 1000);
        }

    }

}
