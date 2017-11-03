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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.KeywordSearchAllActivity;
import com.art.huakai.artshow.activity.MainActivity;
import com.art.huakai.artshow.activity.NewsShowActivity;
import com.art.huakai.artshow.activity.PersonalDetailMessageActivity;
import com.art.huakai.artshow.activity.TheatreDetailMessageActivity;
import com.art.huakai.artshow.activity.WorksDetailMessageActivity;
import com.art.huakai.artshow.adapter.CooperationOpportunitiesAdapter;
import com.art.huakai.artshow.adapter.ExcellentWorksAdapter;
import com.art.huakai.artshow.adapter.IndustryNewsAdapter;
import com.art.huakai.artshow.adapter.ProfessionalPersonAdapter;
import com.art.huakai.artshow.adapter.RecommendTheaterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.widget.ChinaShowImageView;
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
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.lly_content)
    LinearLayout llyContent;
    private HomePageDetails homePageDetails;

    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_one_title)
    TextView tvIndustryNewsTitle;
    @BindView(R.id.tv_one_whole)
    TextView tvIndustryNewsWhole;
    @BindView(R.id.rl_one)
    RelativeLayout rlIndustryNews;
    @BindView(R.id.rcv_one)
    RecyclerView rcvIndustryNews;
    IndustryNewsAdapter industryNewsAdapter;
    LinearLayoutManager industryNewsLayoutManager;

    @BindView(R.id.sv)
    ScrollView scrollView;
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

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
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
        llyContent.setPadding(0, statusBarHeight, 0, 0);
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
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
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
                ivLoading.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
            }
        });
    }


    private void setData() {

        if (homePageDetails == null) {
            Log.e(TAG, "setData: nullnull");
            return;
        }

        if (homePageDetails.getNewses() != null && homePageDetails.getNewses().size() > 0) {
            if (null == industryNewsAdapter) {
                industryNewsAdapter = new IndustryNewsAdapter(getContext(), homePageDetails.getNewses());
            }
            if (null == industryNewsLayoutManager) {
                industryNewsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
            industryNewsAdapter.setOnItemClickListener(new IndustryNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Log.e(TAG, "onItemClickListener: position==" + position);
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
        chinaShowImageView.setImageURI(Uri.parse("asset:///test.png"));
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
        entities.add(new BannerEntity(0, "http://logo", "image"));
        banner.setEntities(entities);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "position==" + position, Toast.LENGTH_SHORT).show();


            }
        });
        if (homePageDetails.getBanners().size() > 1) {
            banner.startAutoScroll();
        }


    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: 11111111111");
        MainActivity mainActivity = (MainActivity) getActivity();
        if (v.getId() == R.id.tv_one_whole) {
            if (null != mainActivity) {
//                mainActivity.setWholeItemPosition(1);
//                mainActivity.setCheckId(R.id.rdobtn_me);

//                Intent intent = new Intent(getContext(), SearchNewsActivity.class);
//                startActivity(intent);

                Intent intent = new Intent(getContext(), NewsShowActivity.class);
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

        } else if (v.getId() == R.id.iv_search) {
            Intent intent = new Intent(getContext(), KeywordSearchAllActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
