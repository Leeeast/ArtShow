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
import com.art.huakai.artshow.activity.TheatreDetailMessageActivity;
import com.art.huakai.artshow.adapter.LookingTheatreAdapter;
import com.art.huakai.artshow.adapter.TheatreFilterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.CitySelectUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.SmartRecyclerview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qqtheme.framework.entity.CityBean;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

public class FoundTheatreFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {

    @BindView(R.id.ll_complex_ranking)
    LinearLayout llComplexRanking;
    @BindView(R.id.ll_city_choose)
    LinearLayout llCityChoose;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    Unbinder unbinder;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private String TAG = "FoundTheatreFragment";

    @BindView(R.id.iv_choose_price)
    ImageView ivComplexRanking;
    @BindView(R.id.iv_choose_number)
    ImageView ivChooseCity;
    @BindView(R.id.iv_real_choose)
    ImageView ivFiter;
    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    @BindView(R.id.tv_whole_ranking)
    TextView tvComplexRanking;
    @BindView(R.id.tv_city_choose)
    TextView tvCityChoose;
    @BindView(R.id.tv_real_filter)
    TextView tvFilter;


    private LookingTheatreAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private PopupWindow popupWindow;
    private LayoutInflater mLayoutInflater;
    private int complexRankingRule = 0;
    private int theatreSize = -1;
    private int theatrefee = -1;
    private int locationId = 0;
    private int page = 1;
    private String time;
    private ArrayList<String> months = new ArrayList<String>();
    private ArrayList<String> lists = new ArrayList<String>();
    private int monthPosition = -1;
    private String address;
    private String mRegionId = "";


    public FoundTheatreFragment() {
        // Required empty public constructor
    }


    List<Theatre> theatres = new ArrayList<Theatre>();

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
                ivLoading.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
            }
        }
    };

    public static FoundTheatreFragment newInstance() {
        FoundTheatreFragment fragment = new FoundTheatreFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        _GetDate();
        getList();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_found_theatre;
    }

    @Override
    public void initView(View rootView) {
        llComplexRanking.setOnClickListener(this);
        llCityChoose.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
    }

    @Override
    public void setView() {

    }

    private void setData() {

        linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        lookingWorksAdapter = new LookingTheatreAdapter(getContext(), theatres);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingTheatreAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                if (theatres.get(position) != null && !TextUtils.isEmpty(theatres.get(position).getId())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, theatres.get(position).getId());
                    bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, false);
                    invokActivity(getContext(), TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
                }
                Log.e(TAG, "onItemClickListener: position==" + position);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mLayoutInflater = inflater;
        unbinder = ButterKnife.bind(this, rootView);
        showCityDialog();
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
                tvCityChoose.setTextColor(0xffe93c2c);
                ivChooseCity.setImageResource(R.mipmap.arrow_active);
//                tvFilter.setTextColor(0xff5a4b41);
//                ivFiter.setImageResource(R.mipmap.filter_default);

                showAddressSelect();
                break;

            case R.id.ll_complex_ranking:

                tvComplexRanking.setTextColor(0xffe93c2c);
                ivComplexRanking.setImageResource(R.mipmap.arrow_active);
//                tvCityChoose.setTextColor(0xff5a4b41);
//                ivChooseCity.setImageResource(R.mipmap.arrow_down_icon);
//                tvFilter.setTextColor(0xff5a4b41);
//                ivFiter.setImageResource(R.mipmap.filter_default);
                showPopuwindow(1);
//                Toast.makeText(getContext(), "iv_choose_price", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_filter:

//                tvComplexRanking.setTextColor(0xff5a4b41);
//                ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
//                tvCityChoose.setTextColor(0xff5a4b41);
//                ivChooseCity.setImageResource(R.mipmap.arrow_down_icon);
                tvFilter.setTextColor(0xffe93c2c);
                ivFiter.setImageResource(R.mipmap.filter_active);
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
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (complexRankingRule != 0) {
                    ivComplexRanking.setImageResource(R.mipmap.arrow_down_active);
                    tvComplexRanking.setTextColor(0xffe93c2c);
                } else {
                    ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
                    tvComplexRanking.setTextColor(0xff5a4b41);
                }
                if (!TextUtils.isEmpty(mRegionId)) {
                    tvCityChoose.setTextColor(0xffe93c2c);
                    ivChooseCity.setImageResource(R.mipmap.arrow_down_active);
                } else {
                    tvCityChoose.setTextColor(0xff5a4b41);
                    ivChooseCity.setImageResource(R.mipmap.arrow_down_icon);
                }

                if (theatreSize != -1 || theatrefee != -1 || monthPosition != -1) {
                    tvFilter.setTextColor(0xffe93c2c);
                    ivFiter.setImageResource(R.mipmap.filter_active);
                } else {
                    tvFilter.setTextColor(0xff5a4b41);
                    ivFiter.setImageResource(R.mipmap.filter_default);
                }


                Log.e(TAG, "onDismiss: ");
            }
        });
        if (type == 1) {
            View content = mLayoutInflater.inflate(R.layout.found_theatre_whole_ranking_popuwindow_item, null);
            if (complexRankingRule == 1) {
                content.findViewById(R.id.iv_one).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_one);
                view.setTextColor(0xffe93c2c);
            } else if (complexRankingRule == 2) {
                content.findViewById(R.id.iv_two).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_two);
                view.setTextColor(0xffe93c2c);
            } else if (complexRankingRule == 3) {
                content.findViewById(R.id.iv_three).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_three);
                view.setTextColor(0xffe93c2c);
            } else if (complexRankingRule == 4) {
                content.findViewById(R.id.iv_four).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_four);
                view.setTextColor(0xffe93c2c);
            }
            popupWindow.setContentView(content);
            content.findViewById(R.id.tv_one).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complexRankingRule = 1;
                    tvComplexRanking.setText("费用由高到低");
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    theatres.clear();
                }
            });
            content.findViewById(R.id.tv_two).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complexRankingRule = 2;
                    tvComplexRanking.setText("费用由低到高");
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    theatres.clear();
                }
            });
            content.findViewById(R.id.tv_three).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complexRankingRule = 3;
                    tvComplexRanking.setText("座位数由高到低");
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    theatres.clear();
                }
            });
            content.findViewById(R.id.tv_four).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complexRankingRule = 4;
                    tvComplexRanking.setText("座位数由低到高");
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    theatres.clear();
                }
            });
        } else if (type == 3) {
            View content = mLayoutInflater.inflate(R.layout.found_theatre_real_filter_popuwindow_item, null);
            ArrayList<String> stringsone = new ArrayList<String>();
            stringsone.add("不限");
            stringsone.add("小剧场 （400人以内）");
            stringsone.add("中剧场 (400-800人)");
            stringsone.add("大剧场 （800-1500人）");
            stringsone.add("超大剧场 (1500人以上)");
            RecyclerView recyclerViewone = (RecyclerView) content.findViewById(R.id.rcv_one);
            final TheatreFilterAdapter singleChooseAdapterone = new TheatreFilterAdapter(getContext(), stringsone, theatreSize);
            singleChooseAdapterone.setOnItemClickListener(new TheatreFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    theatreSize = position;
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
            stringstwo.add("3万以内");
            stringstwo.add("3万至5万");
            stringstwo.add("5万至8万");
            stringstwo.add("8万至10万");
            stringstwo.add("10万以上");
            final TheatreFilterAdapter singleChooseAdaptertwo = new TheatreFilterAdapter(getContext(), stringstwo, theatrefee);
            singleChooseAdaptertwo.setOnItemClickListener(new TheatreFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    theatrefee = position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationtwo = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewtwo.setItemAnimator(null);
            GridLayoutManager gridLayoutManagertwo = new GridLayoutManager(getContext(), 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewtwo.addItemDecoration(gridLayoutItemDecorationtwo);
            recyclerViewtwo.setLayoutManager(gridLayoutManagertwo);
            recyclerViewtwo.setAdapter(singleChooseAdaptertwo);
            recyclerViewtwo.setNestedScrollingEnabled(false);


            RecyclerView recyclerViewthree = (RecyclerView) content.findViewById(R.id.rcv_three);
            final TheatreFilterAdapter singleChooseAdapterthree = new TheatreFilterAdapter(getContext(), months, monthPosition);
            singleChooseAdapterthree.setOnItemClickListener(new TheatreFilterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    monthPosition = position;


                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationthree = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewthree.setItemAnimator(null);
            GridLayoutManager gridLayoutManagerthree = new GridLayoutManager(getContext(), 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewthree.addItemDecoration(gridLayoutItemDecorationthree);
            recyclerViewthree.setLayoutManager(gridLayoutManagerthree);
            recyclerViewthree.setAdapter(singleChooseAdapterthree);
            recyclerViewthree.setNestedScrollingEnabled(false);


            content.findViewById(R.id.but_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (singleChooseAdapterone != null) {
                        theatreSize = -1;
                        singleChooseAdapterone.resetData();
                    }
                    if (singleChooseAdaptertwo != null) {
                        theatrefee = -1;
                        singleChooseAdaptertwo.resetData();
                    }
                    if (singleChooseAdapterthree != null) {
                        monthPosition = -1;
                        singleChooseAdapterthree.resetData();
                    }
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    page = 1;
                    getList();
                    theatres.clear();
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
                    theatres.clear();
                }
            });

            popupWindow.setContentView(content);

        }
        if (popupWindow != null && !popupWindow.isShowing()) {
            if (Build.VERSION.SDK_INT < 24) {
                popupWindow.showAsDropDown(llComplexRanking, 0, 0);
            } else {
                int[] Location = new int[2];
                llComplexRanking.getLocationInWindow(Location);
                int x = Location[0];
                int y = Location[1];
                popupWindow.showAtLocation(llComplexRanking, Gravity.NO_GRAVITY, 0, y + llComplexRanking.getHeight());
            }
        }

    }


    private void getList() {

        final Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_THEATRES);
        if (complexRankingRule == 1) {
            params.put("order", "expense");
            params.put("orderType", "desc");
        } else if (complexRankingRule == 2) {
            params.put("order", "expense");
            params.put("orderType", "asc");
        } else if (complexRankingRule == 3) {
            params.put("order", "seating");
            params.put("orderType", "desc");
        } else if (complexRankingRule == 4) {
            params.put("order", "seating");
            params.put("orderType", "asc");
        }
        if (theatreSize == 1) {
            params.put("seatingMax", "400");
        } else if (theatreSize == 2) {
            params.put("seatingMin", "400");
            params.put("seatingMax", "800");
        } else if (theatreSize == 3) {
            params.put("seatingMin", "800");
            params.put("seatingMax", "1500");
        } else if (theatreSize == 4) {
            params.put("seatingMin", "1500");
        }
        if (theatrefee == 1) {
            params.put("expenseMin", "30000");
        } else if (theatrefee == 2) {
            params.put("expenseMin", "30000");
            params.put("expenseMax", "50000");
        } else if (theatrefee == 2) {
            params.put("expenseMin", "50000");
            params.put("expenseMax", "80000");
        } else if (theatrefee == 2) {
            params.put("expenseMin", "80000");
            params.put("expenseMax", "100000");
        } else if (theatrefee == 2) {
            params.put("expenseMin", "100000");
        }
        if (!(monthPosition == 0 || monthPosition == -1)) {
            String month = months.get(monthPosition);
            params.put("enabledMonth", month.substring(0, 4) + "-" + lists.get(monthPosition));
        }
        if (!TextUtils.isEmpty(mRegionId) && !mRegionId.equals("0")) {
            params.put("regionId", mRegionId);
        }

        params.put("page", page + "");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getList: params==" + params.toString());
        RequestUtil.request(true, Constant.URL_GET_THEATRES, params, 105, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 1 && theatres.size() == 0) {
                            ivLoading.setVisibility(View.GONE);
                            llContent.setVisibility(View.GONE);
                            ivNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                }, 500);

                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.e(TAG, "onSuccess: obj==" + obj);
                        Gson gson = new Gson();
                        ArrayList<Theatre> tempTheatres = new ArrayList<Theatre>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<Theatre>>() {
                        }.getType());
                        if (tempTheatres != null && tempTheatres.size() > 0) {
                            if (theatres.size() == 0) {
                                if (theatres.addAll(tempTheatres)) {
                                    uiHandler.removeCallbacksAndMessages(null);
                                    uiHandler.sendEmptyMessage(0);
                                }
                                page++;
                            } else {
                                if (page == 1) {
                                    recyclerView.refreshComplete();
                                    theatres.clear();
                                    if (theatres.addAll(tempTheatres)) {
                                        uiHandler.sendEmptyMessage(0);
                                    }
                                } else {
                                    recyclerView.loadMoreComplete();
                                    theatres.addAll(tempTheatres);
                                    if (lookingWorksAdapter != null) {
                                        lookingWorksAdapter.add(tempTheatres);
                                    }
                                }
                                page++;
                            }
                        } else {
                            if (theatres.size() == 0) {
                                Toast.makeText(getContext(), "未查询到您筛选的数据", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 首次加载数据失败");
                            } else {
                                if (page == 1) {
                                    Log.e(TAG, "onSuccess: 刷新数据失败");
                                    Toast.makeText(getContext(), "刷新数据失败", Toast.LENGTH_SHORT).show();
                                    recyclerView.refreshComplete();
                                } else {
                                    recyclerView.loadMoreComplete();
                                    Toast.makeText(getContext(), "已无更多数据", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败");
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + theatres.size());
                    } else {
                        if (theatres.size() == 0) {
                            Log.e(TAG, "onSuccess: 首次加载数据失败");
                            Toast.makeText(getContext(), "未查询到您筛选的数据", Toast.LENGTH_SHORT).show();

                        } else {
                            if (page == 1) {
                                recyclerView.refreshComplete();
                                Log.e(TAG, "onSuccess: 刷新数据失败");
                                Toast.makeText(getContext(), "刷新数据失败", Toast.LENGTH_SHORT).show();
                            } else {
                                recyclerView.loadMoreComplete();
                                Log.e(TAG, "onSuccess: 加载更多数据失败");
                                Toast.makeText(getContext(), "已无更多数据", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {

                    if (theatres.size() == 0) {
                        Log.e(TAG, "onSuccess: 首次加载数据失败");
                        Toast.makeText(getContext(), "未查询到您筛选的数据", Toast.LENGTH_SHORT).show();
                    } else {
                        if (page == 1) {
                            recyclerView.refreshComplete();
                            Log.e(TAG, "onSuccess: 刷新数据失败");
                            Toast.makeText(getContext(), "刷新数据失败", Toast.LENGTH_SHORT).show();
                        } else {
                            recyclerView.loadMoreComplete();
                            Log.e(TAG, "onSuccess: 加载更多数据失败");
                            Toast.makeText(getContext(), "已无更多数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (theatres.size() == 0) {
                    Log.e(TAG, "onSuccess: 首次加载数据失败");
                    Toast.makeText(getContext(), "未查询到您筛选的数据", Toast.LENGTH_SHORT).show();
                    ivLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.GONE);
                    ivNoContent.setVisibility(View.VISIBLE);
                } else {
                    if (page == 1) {
                        recyclerView.refreshComplete();
                        Log.e(TAG, "onSuccess: 刷新数据失败");
                        Toast.makeText(getContext(), "刷新数据失败", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerView.loadMoreComplete();
                        Log.e(TAG, "onSuccess: 加载更多数据失败");
                        Toast.makeText(getContext(), "已无更多数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void _GetDate() {
//        TimeZone tz = TimeZone.getTimeZone("GMT");
//        Calendar c = Calendar.getInstance(tz);
//        Log.e(TAG, "_GetDate:year== " + c.get(Calendar.YEAR) + "month==" + c.get(Calendar.MONTH));
//        return "year = " + c.get(Calendar.YEAR) + "\n month = " + c.get(Calendar.MONTH) + "\n day = " + c.get(Calendar.DAY_OF_MONTH);

        months.add("不限");
        lists.add("0");
        Calendar c = Calendar.getInstance();//
        Log.e(TAG, "_GetDate: mYear==" + c.get(Calendar.YEAR) + "--mMonth==" + (c.get(Calendar.MONTH) + 1));
        int year = c.get(Calendar.YEAR);
        int month = (c.get(Calendar.MONTH) + 1);
        if (month < 10) {
            months.add(year + "年" + "0" + month + "月");
        } else {
            months.add(year + "年" + month + "月");
        }
        lists.add("" + month);
        for (int i = 1; i <= 11; i++) {
            if ((month + 1) > 12) {
                month = month + 1 - 12;
                year = year + 1;
            } else {
                month = month + 1;
                year = year;
            }
            if (month < 10) {
                months.add(year + "年" + "0" + month + "月");
            } else {
                months.add(year + "年" + month + "月");
            }
            lists.add("" + month);
        }
    }


    private void showCityDialog() {

        CitySelectUtil.getCityJson(new CitySelectUtil.CityDataRequestListener() {
            @Override
            public void onSuccess(String s) {
                address = s;
//                showAddressSelect(s);
            }

            @Override
            public void onFail() {

            }
        });

    }


    public void showAddressSelect() {
        if (TextUtils.isEmpty(address)) {
            showCityDialog();
            return;
        }
        try {
            List<ProvinceBean> provinceBeen = GsonTools.parseDatas(address, ProvinceBean.class);
            parseAddress(provinceBeen);
            final ProvincePicker picker = new ProvincePicker(getActivity(), provinceBeen);
            picker.setDividerRatio(WheelView.DividerConfig.FILL);
            picker.setCanceledOnTouchOutside(false);
            picker.setCycleDisable(true);
            picker.setSelectedIndex(0);
            picker.setAnimationStyle(R.style.Animation_CustomPopup);
            picker.setTextSize(23);

            WheelView.DividerConfig dividerConfig = new WheelView.DividerConfig();
            dividerConfig.setRatio(WheelView.DividerConfig.FILL);
            dividerConfig.setThick(1);
            picker.setDividerConfig(dividerConfig);
            picker.setOnItemPickListener(new ProvincePicker.OnItemProvincePickListener() {
                @Override
                public void onPicked(String province, String city, int regionId) {
//                    tvLiveCity.setText(province + "  " + city);
                    tvCityChoose.setText(province + "" + city);
                    mRegionId = String.valueOf(regionId);
                    page = 1;
                    getList();
                    theatres.clear();
                    if (complexRankingRule != 0) {
                        ivComplexRanking.setImageResource(R.mipmap.arrow_down_active);
                        tvComplexRanking.setTextColor(0xffe93c2c);
                    }
                    if (!TextUtils.isEmpty(mRegionId)) {

                        tvCityChoose.setTextColor(0xffe93c2c);
                        ivChooseCity.setImageResource(R.mipmap.arrow_down_active);
                    }

                    if (theatreSize != -1 || theatrefee != -1 || monthPosition != -1) {
                        tvFilter.setTextColor(0xffe93c2c);
                        ivFiter.setImageResource(R.mipmap.filter_active);
                    }

                }
            });
            picker.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对地区数据添加全部
     *
     * @param provinceBeens
     */
    private void parseAddress(List<ProvinceBean> provinceBeens) {
        ProvinceBean provinceBean = new ProvinceBean();
        provinceBean.setName("全国");
        provinceBean.setId(0);
        CityBean city = new CityBean();
        city.setId(0);
        city.setName("不限");
        ArrayList<CityBean> citys = new ArrayList<>();
        citys.add(city);
        provinceBean.setChildren(citys);

        provinceBeens.add(0, provinceBean);
        for (int i = 1; i < provinceBeens.size(); i++) {
            ProvinceBean province = provinceBeens.get(i);
            List<CityBean> children = province.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            CityBean cityBean = new CityBean();
            cityBean.setId((int) province.getId());
            cityBean.setName("全部");
            children.add(0, cityBean);
        }
    }

}
