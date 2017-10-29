package com.art.huakai.artshow.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.art.huakai.artshow.adapter.LookingTheatreAdapter;
import com.art.huakai.artshow.adapter.SingleChooseAdapter;
import com.art.huakai.artshow.adapter.TheatreFilterAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.Theatre;
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
import java.util.TimeZone;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class FoundTheatreFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {

    @BindView(R.id.ll_complex_ranking)
    LinearLayout llComplexRanking;
    @BindView(R.id.ll_city_choose)
    LinearLayout llCityChoose;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    Unbinder unbinder;
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
    private int theatreSize =-1;
    private int theatrefee = -1;
    private int locationId=0;
    private int page = 1;
    private String time;
    public FoundTheatreFragment() {
        // Required empty public constructor
    }


    List<Theatre> theatres=new ArrayList<Theatre>();

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
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

                Toast.makeText(getContext(), "iv_choose_number", Toast.LENGTH_SHORT).show();
                tvComplexRanking.setTextColor(0xff5a4b41);
                ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
                tvCityChoose.setTextColor(0xffe93c2c);
                ivChooseCity.setImageResource(R.mipmap.arrow_active);
                tvFilter.setTextColor(0xff5a4b41);
                ivFiter.setImageResource(R.mipmap.filter_default);

                break;

            case R.id.ll_complex_ranking:

                tvComplexRanking.setTextColor(0xffe93c2c);
                ivComplexRanking.setImageResource(R.mipmap.arrow_active);
                tvCityChoose.setTextColor(0xff5a4b41);
                ivChooseCity.setImageResource(R.mipmap.arrow_down_icon);
                tvFilter.setTextColor(0xff5a4b41);
                ivFiter.setImageResource(R.mipmap.filter_default);
                showPopuwindow(1);
                Toast.makeText(getContext(), "iv_choose_price", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_filter:

                tvComplexRanking.setTextColor(0xff5a4b41);
                ivComplexRanking.setImageResource(R.mipmap.arrow_down_icon);
                tvCityChoose.setTextColor(0xff5a4b41);
                ivChooseCity.setImageResource(R.mipmap.arrow_down_icon);
                tvFilter.setTextColor(0xffe93c2c);
                ivFiter.setImageResource(R.mipmap.filter_active);
                showPopuwindow(3);
                Toast.makeText(getContext(), "iv_real_choose", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onRefresh() {
        page=1;
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
                if(complexRankingRule!=0){
                    ivComplexRanking.setImageResource(R.mipmap.arrow_down_active);
                }
                Log.e(TAG, "onDismiss: " );
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
                }
            });

            content.findViewById(R.id.but_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });

            popupWindow.setContentView(content);

        }
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(llComplexRanking, 0, 0);
        }

    }


    private void getList() {

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_THEATRES);
        if(complexRankingRule==1){
            params.put("order","expense");
            params.put("orderType","desc");
        }else if(complexRankingRule==2){
            params.put("order","expense");
            params.put("orderType","asc");
        }else if(complexRankingRule==3){
            params.put("order","seating");
            params.put("orderType","desc");
        }else if(complexRankingRule==4){
            params.put("order","seating");
            params.put("orderType","asc");
        }
        if(theatreSize==1){
            params.put("seatingMax","400");
        }else if(theatreSize==2){
            params.put("seatingMin","400");
            params.put("seatingMax","800");
        }else if(theatreSize==3){
            params.put("seatingMin","800");
            params.put("seatingMax","1500");
        }else if(theatreSize==4){
            params.put("seatingMin","1500");
        }
        if(theatrefee==1){
            params.put("expenseMin","30000");
        }else if(theatrefee==2){
            params.put("expenseMin","30000");
            params.put("expenseMax","50000");
        }else if(theatrefee==2){
            params.put("expenseMin","50000");
            params.put("expenseMax","80000");
        }else if(theatrefee==2){
            params.put("expenseMin","80000");
            params.put("expenseMax","100000");
        }else if(theatrefee==2){
            params.put("expenseMin","100000");
        }
        params.put("page", page+"");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign==" + sign);
        Log.e(TAG, "getList: params=="+params.toString() );
        RequestUtil.request(true, Constant.URL_GET_THEATRES, params, 105, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess){
                    if(!TextUtils.isEmpty(obj)){
                        Log.e(TAG, "onSuccess: obj=="+obj );
                        Gson gson = new Gson();
                        ArrayList<Theatre> tempTheatres=new ArrayList<Theatre>();
//                        theatres.clear();
                        tempTheatres = gson.fromJson(obj, new TypeToken<List<Theatre>>() {
                        }.getType());
                        if(tempTheatres!=null&&tempTheatres.size()>0){
                          if(theatres.size()==0){
                              if(theatres.addAll(tempTheatres)){
                                  uiHandler.sendEmptyMessage(0);
                              }
                              page++;
                          }else{
                              if(page==1){
                                  recyclerView.refreshComplete();
                                  theatres.clear();
                                  if(theatres.addAll(tempTheatres)){
                                      uiHandler.sendEmptyMessage(0);
                                  }
                              }else{
                                  recyclerView.loadMoreComplete();
                                  theatres.addAll(tempTheatres);
                                  if(lookingWorksAdapter!=null){
                                      lookingWorksAdapter.add(tempTheatres);
                                  }
                              }
                              page++;
                          }
                        }else{
                            if(theatres.size()==0){
                                Log.e(TAG, "onSuccess: 首次加载数据失败" );
                            }else{
                                if(page==1){
                                    Log.e(TAG, "onSuccess: 刷新数据失败" );
                                    recyclerView.refreshComplete();
                                }else{
                                    recyclerView.loadMoreComplete();
                                    Log.e(TAG, "onSuccess: 加载更多数据失败" );
                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: theatres.size==" + theatres.size());
                    }else{
                        if(theatres.size()==0){
                            Log.e(TAG, "onSuccess: 首次加载数据失败" );
                        }else{
                            if(page==1){
                                recyclerView.refreshComplete();
                                Log.e(TAG, "onSuccess: 刷新数据失败" );
                            }else{
                                recyclerView.loadMoreComplete();
                                Log.e(TAG, "onSuccess: 加载更多数据失败" );
                            }
                        }
                    }
                } else {
                    if(theatres.size()==0){
                        Log.e(TAG, "onSuccess: 首次加载数据失败" );
                    }else{
                        if(page==1){
                            recyclerView.refreshComplete();
                            Log.e(TAG, "onSuccess: 刷新数据失败" );
                        }else{
                            recyclerView.loadMoreComplete();
                            Log.e(TAG, "onSuccess: 加载更多数据失败" );
                        }
                    }
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if(theatres.size()==0){
                    Log.e(TAG, "onSuccess: 首次加载数据失败" );
                }else{
                    if(page==1){
                        recyclerView.refreshComplete();
                        Log.e(TAG, "onSuccess: 刷新数据失败" );
                    }else{
                        recyclerView.loadMoreComplete();
                        Log.e(TAG, "onSuccess: 加载更多数据失败" );
                    }
                }

            }
        });
    }


    private String _GetDate(){
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(tz);
        Log.e(TAG, "_GetDate:year== "+c.get(Calendar.YEAR)+"month=="+ c.get(Calendar.MONTH));
        return "year = "+c.get(Calendar.YEAR)+"\n month = "+c.get(Calendar.MONTH)+"\n day = "+c.get(Calendar.DAY_OF_MONTH);
    }


}
