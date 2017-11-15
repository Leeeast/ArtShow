package com.art.huakai.artshow.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.PersonalDetailMessageActivity;
import com.art.huakai.artshow.adapter.LookingProfessionalAdapter;
import com.art.huakai.artshow.adapter.LookingWorksAdapter;
import com.art.huakai.artshow.adapter.SkillChooseAdapter;
import com.art.huakai.artshow.adapter.TalentFilterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.ChildrenBean;
import com.art.huakai.artshow.entity.SkillBean;
import com.art.huakai.artshow.entity.TalentBean;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.SmartRecyclerview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class FoundTalentsFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {
    @BindView(R.id.ll_complex_ranking)
    LinearLayout llComplexRanking;
    @BindView(R.id.ll_city_choose)
    LinearLayout llSkillChoose;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private String TAG = "FoundTalentsFragment";

    @BindView(R.id.iv_choose_price)
    ImageView ivComplexRanking;
    @BindView(R.id.iv_choose_number)
    ImageView ivChooseSkill;
    @BindView(R.id.iv_real_choose)
    ImageView ivFilter;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    @BindView(R.id.tv_whole_ranking)
    TextView tvComplexRanking;
    @BindView(R.id.tv_city_choose)
    TextView tvSkillChoose;
    @BindView(R.id.tv_real_filter)
    TextView tvFilter;
    private ArrayList<TalentBean> talentLists = new ArrayList<TalentBean>();
    private LookingProfessionalAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private LinearItemDecoration linearItemDecoration;

    private PopupWindow popupWindow;
    private LayoutInflater mLayoutInflater;
    private int complexRankingRule = 0;
    private int yearPosition = -1;
    private String collegeName = "";
    private int certificationPosition = -1;
    private int noRequest = -1;
    private int others = -1;

    private String skillParentId = "";
    private String skillChildId = "";
    private int skillChildPosition = -1;

    private int page = 1;

    private List<SkillBean> skillBeanList = new ArrayList<SkillBean>();
    private boolean isLoading=false;

    public FoundTalentsFragment() {
        // Required empty public constructor
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
                setData();
            }
        }
    };

    public static FoundTalentsFragment newInstance() {
        FoundTalentsFragment fragment = new FoundTalentsFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        getList();
        getSkillClassify();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_found_talents;
    }

    @Override
    public void initView(View rootView) {
        llComplexRanking.setOnClickListener(this);
        llSkillChoose.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
    }

    private void setData() {

        linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearItemDecoration = new LinearItemDecoration((int) getContext().getResources().getDimension(R.dimen.DIMEN_14PX));
        recyclerView.setLayoutManager(linearlayoutManager);
        lookingWorksAdapter = new LookingProfessionalAdapter(getContext(), talentLists);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingProfessionalAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                if(talentLists.get(position)!=null&&!TextUtils.isEmpty(talentLists.get(position).getId())){
                    Bundle bundle = new Bundle();
                    bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, talentLists.get(position).getId());
                    invokActivity(getContext(), PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PERSONAL);
                }

            }
        });
    }

    @Override
    public void setView() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mLayoutInflater = inflater;
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: v.getId==" + v.getId());
        switch (v.getId()) {

            case R.id.ll_city_choose:

//                Toast.makeText(getContext(), "iv_choose_number", Toast.LENGTH_SHORT).show();
//                tvComplexRanking.setTextColor(0xff5a4b41);
//                ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
                tvSkillChoose.setTextColor(0xffe93c2c);
                ivChooseSkill.setImageResource(R.mipmap.arrow_active);
//                tvFilter.setTextColor(0xff5a4b41);
//                ivFilter.setImageResource(R.mipmap.filter_default);
                showPopuwindow(2);

                break;

            case R.id.ll_complex_ranking:


                tvComplexRanking.setTextColor(0xffe93c2c);
                ivComplexRanking.setImageResource(R.mipmap.arrow_active);
//                tvSkillChoose.setTextColor(0xff5a4b41);
//                ivChooseSkill.setImageResource(R.mipmap.arrow_down_icon);
//                tvFilter.setTextColor(0xff5a4b41);
//                ivFilter.setImageResource(R.mipmap.filter_default);
                showPopuwindow(1);
//                Toast.makeText(getContext(), "iv_choose_price", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_filter:

//                tvComplexRanking.setTextColor(0xff5a4b41);
//                ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
//                tvSkillChoose.setTextColor(0xff5a4b41);
//                ivChooseSkill.setImageResource(R.mipmap.arrow_down_icon);
                tvFilter.setTextColor(0xffe93c2c);
                ivFilter.setImageResource(R.mipmap.filter_active);
                showPopuwindow(3);
//                Toast.makeText(getContext(), "iv_real_choose", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onRefresh() {

        page = 1;
        getList();

    }

    @Override
    public void onLoadMore() {

        getList();

    }

    private void showPopuwindow(int type) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow();
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (complexRankingRule != 0) {
                    ivComplexRanking.setImageResource(R.mipmap.arrow_down_active);
                    tvComplexRanking.setTextColor(0xffe93c2c);
                }else{
                    ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
                    tvComplexRanking.setTextColor(0xff5a4b41);
                }

                if (!TextUtils.isEmpty(skillChildId)) {
                    ivChooseSkill.setImageResource(R.mipmap.arrow_down_active);
                    tvSkillChoose.setTextColor(0xffe93c2c);
                }else{
                    ivChooseSkill.setImageResource(R.mipmap.arrow_down_icon);
                    tvSkillChoose.setTextColor(0xff5a4b41);
                }

                if(yearPosition !=-1||!TextUtils.isEmpty(collegeName )||certificationPosition !=-1){
                    tvFilter.setTextColor(0xffe93c2c);
                    ivFilter.setImageResource(R.mipmap.filter_active);
                }else{
                    tvFilter.setTextColor(0xff5a4b41);
                    ivFilter.setImageResource(R.mipmap.filter_default);
                }


            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        if (type == 1) {
            View content = mLayoutInflater.inflate(R.layout.found_talent_whole_ranking_popuwindow_item, null);
            if (complexRankingRule == 1) {
                content.findViewById(R.id.iv_one).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_one);
                view.setTextColor(0xffe93c2c);

            } else if (complexRankingRule == 2) {
                content.findViewById(R.id.iv_two).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_two);
                view.setTextColor(0xffe93c2c);

            }
            popupWindow.setContentView(content);
            content.findViewById(R.id.tv_one).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(complexRankingRule==1){
                        complexRankingRule = 0;
                        tvComplexRanking.setText("综合排序");
                    }else{
                        complexRankingRule = 1;
                        tvComplexRanking.setText("年龄由高到低");
                    }

                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    talentLists.clear();
                }
            });
            content.findViewById(R.id.tv_two).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(complexRankingRule==2){
                        complexRankingRule = 0;
                        tvComplexRanking.setText("综合排序");
                    }else{
                        complexRankingRule = 2;
                        tvComplexRanking.setText("年龄由低到高");
                    }

                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    talentLists.clear();
                }
            });
        } else if (type == 3) {
            final View content = mLayoutInflater.inflate(R.layout.found_talents_real_filter_popuwindow_item, null);

            ArrayList<String> stringsone = new ArrayList<String>();
            stringsone.add("不限");
            stringsone.add("10岁以下");
            stringsone.add("10-20岁");
            stringsone.add("21-30岁");
            stringsone.add("31-45岁");
            stringsone.add("46-60岁");
            stringsone.add("60岁以上");
            RecyclerView recyclerViewone = (RecyclerView) content.findViewById(R.id.rcv_one);
            final TalentFilterAdapter singleChooseAdapterone = new TalentFilterAdapter(getContext(), stringsone, yearPosition);
            singleChooseAdapterone.setOnItemClickListener(new TalentFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    yearPosition = position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(2, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewone.setItemAnimator(null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewone.addItemDecoration(gridLayoutItemDecorationone);
            recyclerViewone.setLayoutManager(gridLayoutManager);
            recyclerViewone.setAdapter(singleChooseAdapterone);
            recyclerViewone.setNestedScrollingEnabled(false);

            RecyclerView recyclerViewtwo = (RecyclerView) content.findViewById(R.id.rcv_two);
            ArrayList<String> stringstwo = new ArrayList<String>();
            stringstwo.add("不限");
            stringstwo.add("中央戏剧学院");
            stringstwo.add("北京电影学院");
            stringstwo.add("中央戏曲学院");
            stringstwo.add("北京舞蹈学院");
            stringstwo.add("上海戏剧学院");
            stringstwo.add("中国传媒大学");
            stringstwo.add("中央音乐学院");
            stringstwo.add("解放军艺术学校");
            stringstwo.add("其他");
            int position = stringstwo.indexOf(collegeName);
            final TalentFilterAdapter singleChooseAdaptertwo = new TalentFilterAdapter(getContext(), stringstwo, position);
            singleChooseAdaptertwo.setOnItemClickListener(new TalentFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String name) {
                    collegeName = name;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationtwo = new GridLayoutItemDecoration(2, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewtwo.setItemAnimator(null);
            GridLayoutManager gridLayoutManagertwo = new GridLayoutManager(getContext(), 2);
            gridLayoutManagertwo.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewtwo.addItemDecoration(gridLayoutItemDecorationtwo);
            recyclerViewtwo.setLayoutManager(gridLayoutManagertwo);
            recyclerViewtwo.setAdapter(singleChooseAdaptertwo);
            recyclerViewtwo.setNestedScrollingEnabled(false);


            RecyclerView recyclerViewthree = (RecyclerView) content.findViewById(R.id.rcv_three);
            ArrayList<String> stringsthree = new ArrayList<String>();
            stringsthree.add("不限");
            stringsthree.add("专业认证");

            final TalentFilterAdapter singleChooseAdapterthree = new TalentFilterAdapter(getContext(), stringsthree, certificationPosition);
            singleChooseAdapterthree.setOnItemClickListener(new TalentFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    certificationPosition = position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationthree = new GridLayoutItemDecoration(2, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewthree.setItemAnimator(null);
            GridLayoutManager gridLayoutManagerthree = new GridLayoutManager(getContext(), 2);
            gridLayoutManagerthree.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewthree.addItemDecoration(gridLayoutItemDecorationthree);
            recyclerViewthree.setLayoutManager(gridLayoutManagerthree);
            recyclerViewthree.setAdapter(singleChooseAdapterthree);
            recyclerViewthree.setNestedScrollingEnabled(false);

            content.findViewById(R.id.but_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (singleChooseAdapterone != null) {
                        yearPosition = -1;
                        singleChooseAdapterone.resetData();
                    }
                    if (singleChooseAdaptertwo != null) {
                        collegeName = "";
                        singleChooseAdaptertwo.resetData();
                    }
                    if (singleChooseAdapterthree != null) {
                        certificationPosition = -1;
                        singleChooseAdapterthree.resetData();
                    }
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    talentLists.clear();

                }
            });

            content.findViewById(R.id.but_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    talentLists.clear();
                }
            });

            popupWindow.setContentView(content);

        } else if (type == 2) {

            View content = mLayoutInflater.inflate(R.layout.found_talents_skill_kind_popuwindow_item, null);
            LinearLayout rl_whole = (LinearLayout) content.findViewById(R.id.rl_whole);
            final TextView tv_no_request = (TextView) content.findViewById(R.id.tv_no_request);
            final TextView tv_others = (TextView) content.findViewById(R.id.tv_others);
            if (noRequest == 0) {
                tv_no_request.setTextColor(0xffe93c2c);
                tv_no_request.setBackgroundResource(R.drawable.red_rectang);
            } else {
                tv_no_request.setTextColor(0xff9b9b9b);
                tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
            }

            if (others == 0) {
                tv_others.setTextColor(0xffe93c2c);
                tv_others.setBackgroundResource(R.drawable.red_rectang);
            } else {
                tv_others.setTextColor(0xff9b9b9b);
                tv_others.setBackgroundResource(R.drawable.grey_rectang);
            }

            tv_no_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSkillChoose.setText("不限");
                    others = 0;
                    tv_others.setTextColor(0xffe93c2c);
                    tv_others.setBackgroundResource(R.drawable.red_rectang);
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest = -1;
                    skillChildId = "";
                    skillParentId = "";
                    skillChildPosition = -1;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    talentLists.clear();

                }
            });

            for (int i = 0; i < skillBeanList.size(); i++) {
                final List<ChildrenBean> tempChildrenBeans = skillBeanList.get(i).getChildren();
                if (tempChildrenBeans == null && tempChildrenBeans.size() == 0) {
                    Log.e(TAG, "showPopuwindow: hahaha00000");
                }
                View view = LayoutInflater.from(getContext()).inflate(R.layout.skill_choose_item, null);
                TextView tv = (TextView) view.findViewById(R.id.tv_name);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv);
                final String tempSkillParentId = skillBeanList.get(i).getId() + "";
                boolean tempWhetherChoosen = skillParentId.equals(tempSkillParentId);
                tv.setText(skillBeanList.get(i).getName());
                final SkillChooseAdapter skillChooseAdapter = new SkillChooseAdapter(getContext(), tempChildrenBeans, skillChildPosition, tempWhetherChoosen);
                skillChooseAdapter.setOnItemClickListener(new SkillChooseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String url) {
                        skillParentId = tempSkillParentId;
                        skillChildId = tempChildrenBeans.get(position).getId() + "";
                        tvSkillChoose.setText(tempChildrenBeans.get(position).getName());
                        skillChildPosition = position;
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        page = 1;
                        getList();
                        talentLists.clear();
                    }
                });
                GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
                recyclerView.setItemAnimator(null);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(gridLayoutItemDecorationone);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(skillChooseAdapter);
                recyclerView.setNestedScrollingEnabled(false);
                rl_whole.addView(view, i + 2);
            }

            popupWindow.setContentView(content);
        }
        if (popupWindow != null && !popupWindow.isShowing()) {
            if(Build.VERSION.SDK_INT<24){
                popupWindow.showAsDropDown(llComplexRanking, 0, 0);
            }else{
                int [] Location =new int [2];
                llComplexRanking.getLocationInWindow(Location);
                int x=Location[0];
                int y=Location[1];
                popupWindow.showAtLocation(llComplexRanking, Gravity.NO_GRAVITY,0,y+llComplexRanking.getHeight());
            }
        }
    }


    private void getList() {
        if(isLoading){
            return;
        }
        isLoading=true;
        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_TALENTS);
        if (!TextUtils.isEmpty(skillChildId)) {
            params.put("classifyId", skillChildId);
        }
        if (yearPosition == 1) {
            params.put("ageMax", "10");
        } else if (yearPosition == 2) {
            params.put("ageMin", "10");
            params.put("ageMax", "20");
        } else if (yearPosition == 3) {
            params.put("ageMin", "21");
            params.put("ageMax", "30");
        } else if (yearPosition == 4) {
            params.put("ageMin", "31");
            params.put("ageMax", "45");
        } else if (yearPosition == 5) {
            params.put("ageMin", "46");
            params.put("ageMax", "60");
        } else if (yearPosition == 6) {
            params.put("ageMin", "60");
        }
        if (!TextUtils.isEmpty(collegeName) && !collegeName.equals("不限")) {
            params.put("school", collegeName);
        }
        if (complexRankingRule == 1) {
            params.put("order", "age");
            params.put("orderType", "desc");
        } else if (complexRankingRule == 2) {
            params.put("order", "age");
            params.put("orderType", "asc");
        }
        params.put("page", page + "");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getList: params==" + params.toString());

        RequestUtil.request(true, Constant.URL_GET_TALENTS, params, 110, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                isLoading=false;
                LogUtil.i(TAG, obj);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(page==1&&talentLists.size() == 0){
                            ivLoading.setVisibility(View.GONE);
                            llContent.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                },500);
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj==" + obj);
                        Gson gson = new Gson();
                        ArrayList<TalentBean> tempTheatres = new ArrayList<TalentBean>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<TalentBean>>() {
                        }.getType());
                        if (tempTheatres != null && tempTheatres.size() > 0) {
                            if (talentLists.size() == 0) {
                                if (talentLists.addAll(tempTheatres)) {
                                    uiHandler.removeCallbacksAndMessages(null);
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                            } else {
                                if (page == 1) {
                                    recyclerView.refreshComplete();
                                    talentLists.clear();
                                    if (talentLists.addAll(tempTheatres)) {
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                } else {
                                    recyclerView.loadMoreComplete();
                                    talentLists.addAll(tempTheatres);
                                    if (lookingWorksAdapter != null) {
//                                        lookingWorksAdapter.add(tempTheatres);
                                        lookingWorksAdapter.notifyDataSetChanged();
                                    }
                                }
                                page++;
                            }
                        } else {
                            if (talentLists.size() == 0) {
                                Toast.makeText(getContext(),"未查询到您筛选的数据",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 首次加载数据失败");
                            } else {

                                if (page == 1) {
                                    Toast.makeText(getContext(),"刷新数据失败",Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onSuccess: 刷新数据失败");
                                    recyclerView.refreshComplete();
                                } else {
                                    recyclerView.loadMoreComplete();
                                    Toast.makeText(getContext(),"已无更多数据",Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败");
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + talentLists.size());
                    } else {
                        if (talentLists.size() == 0) {
                            Toast.makeText(getContext(),"未查询到您筛选的数据",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onSuccess: 首次加载数据失败");
                        } else {

                            if (page == 1) {
                                recyclerView.refreshComplete();
                                Toast.makeText(getContext(),"刷新数据失败",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 刷新数据失败");
                            } else {
                                recyclerView.loadMoreComplete();
                                Toast.makeText(getContext(),"已无更多数据",Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 加载更多数据失败");
                            }
                        }
                    }
                } else {
                    if (talentLists.size() == 0) {
                        Toast.makeText(getContext(),"未查询到您筛选的数据",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onSuccess: 首次加载数据失败");
                        ivLoading.setVisibility(View.GONE);
                        llContent.setVisibility(View.GONE);
                        ivNoContent.setVisibility(View.VISIBLE);
                    } else {

                        if (page == 1) {
                            recyclerView.refreshComplete();
                            Toast.makeText(getContext(),"刷新数据失败",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onSuccess: 刷新数据失败");
                        } else {
                            recyclerView.loadMoreComplete();
                            Toast.makeText(getContext(),"已无更多数据",Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onSuccess: 加载更多数据失败");
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                isLoading=false;
                if (talentLists.size() == 0) {
                    Toast.makeText(getContext(),"未查询到您筛选的数据",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onSuccess: 首次加载数据失败");
                    ivLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                } else {

                    if (page == 1) {
                        recyclerView.refreshComplete();
                        Toast.makeText(getContext(),"刷新数据失败",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onSuccess: 刷新数据失败");
                    } else {
                        recyclerView.loadMoreComplete();
                        Toast.makeText(getContext(),"已无更多数据",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onSuccess: 加载更多数据失败");
                    }
                }
            }
        });
    }


    private void getSkillClassify() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_CLASSFY_LIST);
        params.put("type", "talent");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getRepertoryClassify: " + params.toString());
        RequestUtil.request(true, Constant.URL_GET_CLASSFY_LIST, params, 108, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj222=" + obj);
                        Gson gson = new Gson();
                        if (skillBeanList.size() > 0) {
                            skillBeanList.clear();
                        }
                        skillBeanList = gson.fromJson(obj, new TypeToken<List<SkillBean>>() {
                        }.getType());
                        Log.e(TAG, "onSuccess: skillBeanList.size==" + skillBeanList.size());
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
