package com.art.huakai.artshow.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.art.huakai.artshow.adapter.LookingWorksAdapter;
import com.art.huakai.artshow.adapter.SingleChooseAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.entity.Work;
import com.art.huakai.artshow.okhttp.utils.L;
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

public class FoundProductionFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {
    private String TAG="FoundProductionFragment";
    @BindView(R.id.iv_choose_price)
    ImageView ivChoosePrice;
    @BindView(R.id.iv_choose_number)
    ImageView ivChooseNumber;
    @BindView(R.id.iv_real_choose)
    ImageView ivRealChoose;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    @BindView(R.id.tv_whole_ranking)
    TextView tvWholeRanking;
    @BindView(R.id.tv_city_choose)
    TextView tvCityChoose;
    @BindView(R.id.tv_real_filter)
    TextView tvRealFilter;
    @BindView(R.id.ll_choose)
    LinearLayout llChoose;
    private ArrayList<Work> list;
    private LookingWorksAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private LinearItemDecoration linearItemDecoration;

    private PopupWindow popupWindow;
    private LayoutInflater mLayoutInflater;
    private int WholeRankingRule = 0;
    private int theatreSize=-1;
    private int theatrefee=-1;
    private int showActorAccount=-1;
    private int workskind=-1;

    public FoundProductionFragment() {
        Log.e(TAG, "FoundProductionFragment: " );


        // Required empty public constructor
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                setData();

            }
        }
    };

    public static FoundProductionFragment newInstance() {

        FoundProductionFragment fragment = new FoundProductionFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        Log.e(TAG, "initData: " );

    }

    private void setData(){

        linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearItemDecoration = new LinearItemDecoration((int) getContext().getResources().getDimension(R.dimen.DIMEN_14PX));
        recyclerView.setLayoutManager(linearlayoutManager);
        recyclerView.addItemDecoration(linearItemDecoration);
        lookingWorksAdapter = new LookingWorksAdapter(getContext(), list);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingWorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                Toast.makeText(getContext(), "itemclick", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getLayoutID() {
        Log.e(TAG, "getLayoutID: " );
        return R.layout.fragment_found_production;
    }

    @Override
    public void initView(View rootView) {
        Log.e(TAG, "initView: " );
        getList();
    }

    @Override
    public void setView() {
        ivChoosePrice.setOnClickListener(this);
        ivChooseNumber.setOnClickListener(this);
        ivRealChoose.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        recyclerView.refresh();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        Log.e(TAG, "onCreateView: " );
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mLayoutInflater=inflater;
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

            case R.id.iv_choose_number:

                Toast.makeText(getContext(), "iv_choose_number", Toast.LENGTH_SHORT).show();
                tvWholeRanking.setTextColor(0xff5a4b41);
                ivChoosePrice.setImageResource(R.mipmap.arrow_down_icon);
                tvCityChoose.setTextColor(0xffe93c2c);
                ivChooseNumber.setImageResource(R.mipmap.arrow_active);
                tvRealFilter.setTextColor(0xff5a4b41);
                ivRealChoose.setImageResource(R.mipmap.filter_default);
                showPopuwindow(2);

                break;

            case R.id.iv_choose_price:



                tvWholeRanking.setTextColor(0xffe93c2c);
                ivChoosePrice.setImageResource(R.mipmap.arrow_active);
                tvCityChoose.setTextColor(0xff5a4b41);
                ivChooseNumber.setImageResource(R.mipmap.arrow_down_icon);
                tvRealFilter.setTextColor(0xff5a4b41);
                ivRealChoose.setImageResource(R.mipmap.filter_default);
                showPopuwindow(1);
                Toast.makeText(getContext(), "iv_choose_price", Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_real_choose:

                tvWholeRanking.setTextColor(0xff5a4b41);
                ivChoosePrice.setImageResource(R.mipmap.arrow_down_icon);
                tvCityChoose.setTextColor(0xff5a4b41);
                ivChooseNumber.setImageResource(R.mipmap.arrow_down_icon);
                tvRealFilter.setTextColor(0xffe93c2c);
                ivRealChoose.setImageResource(R.mipmap.filter_active);
                showPopuwindow(3);
                Toast.makeText(getContext(), "iv_real_choose", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onRefresh() {



    }

    @Override
    public void onLoadMore() {



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
        if (type == 1) {
            View content = mLayoutInflater.inflate(R.layout.found_theatre_whole_ranking_popuwindow_item, null);
            if(WholeRankingRule==1){
                content.findViewById(R.id.iv_one).setVisibility(View.VISIBLE);
                TextView view=(TextView)content.findViewById(R.id.tv_one);
                view.setTextColor(0xffe93c2c);
            }else if(WholeRankingRule==2){
                content.findViewById(R.id.iv_two).setVisibility(View.VISIBLE);
                TextView view=(TextView)content.findViewById(R.id.tv_two);
                view.setTextColor(0xffe93c2c);
            }else if(WholeRankingRule==3){
                content.findViewById(R.id.iv_three).setVisibility(View.VISIBLE);
                TextView view=(TextView)content.findViewById(R.id.tv_three);
                view.setTextColor(0xffe93c2c);
            }else if(WholeRankingRule==4){
                content.findViewById(R.id.iv_four).setVisibility(View.VISIBLE);
                TextView view=(TextView)content.findViewById(R.id.tv_four);
                view.setTextColor(0xffe93c2c);
            }
            popupWindow.setContentView(content);
            content.findViewById(R.id.tv_one).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WholeRankingRule = 1;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            content.findViewById(R.id.tv_two).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WholeRankingRule = 2;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            content.findViewById(R.id.tv_three).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WholeRankingRule = 3;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
            content.findViewById(R.id.tv_four).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WholeRankingRule = 4;
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        }else if(type==3){
            View content = mLayoutInflater.inflate(R.layout.found_works_real_filter_popuwindow_item, null);

            ArrayList<String> stringsone=new ArrayList<String>();
            stringsone.add("不限");
            stringsone.add("小剧场 （400人以内）");
            stringsone.add("中剧场 (400-800人)");
            stringsone.add("大剧场 （800-1500人）");
            stringsone.add("超大剧场 (1500人以上)");
            RecyclerView recyclerViewone= (RecyclerView) content.findViewById(R.id.rcv_one);
            final SingleChooseAdapter singleChooseAdapterone=new SingleChooseAdapter(getContext(),stringsone,theatreSize);
            singleChooseAdapterone.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    theatreSize=position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationone=new GridLayoutItemDecoration(2, GridLayoutManager.VERTICAL,40,20);
            recyclerViewone.setItemAnimator(null);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewone.addItemDecoration(gridLayoutItemDecorationone);
            recyclerViewone.setLayoutManager(gridLayoutManager);
            recyclerViewone.setAdapter(singleChooseAdapterone);
            recyclerViewone.setNestedScrollingEnabled(false);

            RecyclerView recyclerViewtwo= (RecyclerView) content.findViewById(R.id.rcv_two);
            ArrayList<String> stringstwo=new ArrayList<String>();
            stringstwo.add("不限");
            stringstwo.add("3万以内");
            stringstwo.add("3万至5万");
            stringstwo.add("5万至8万");
            stringstwo.add("8万至10万");
            stringstwo.add("10万以上");
            final SingleChooseAdapter singleChooseAdaptertwo=new SingleChooseAdapter(getContext(),stringstwo,theatrefee);
            singleChooseAdaptertwo.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    theatrefee=position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationtwo=new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL,40,20);
            recyclerViewtwo.setItemAnimator(null);
            GridLayoutManager gridLayoutManagertwo=new GridLayoutManager(getContext(),3);
            gridLayoutManagertwo.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewtwo.addItemDecoration(gridLayoutItemDecorationtwo);
            recyclerViewtwo.setLayoutManager(gridLayoutManagertwo);
            recyclerViewtwo.setAdapter(singleChooseAdaptertwo);
            recyclerViewtwo.setNestedScrollingEnabled(false);


            RecyclerView recyclerViewthree= (RecyclerView) content.findViewById(R.id.rcv_three);
            ArrayList<String> stringsthree=new ArrayList<String>();
            stringsthree.add("不限");
            stringsthree.add("10人以内");
            stringsthree.add("10-20人");
            stringsthree.add("20-30人");
            stringsthree.add("30-50人");
            stringsthree.add("50人以上");
            final SingleChooseAdapter singleChooseAdapterthree=new SingleChooseAdapter(getContext(),stringsthree,showActorAccount);
            singleChooseAdapterthree.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    showActorAccount=position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationthree=new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL,40,20);
            recyclerViewthree.setItemAnimator(null);
            GridLayoutManager gridLayoutManagerthree=new GridLayoutManager(getContext(),3);
            gridLayoutManagerthree.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewthree.addItemDecoration(gridLayoutItemDecorationthree);
            recyclerViewthree.setLayoutManager(gridLayoutManagerthree);
            recyclerViewthree.setAdapter(singleChooseAdapterthree);
            recyclerViewthree.setNestedScrollingEnabled(false);

            content.findViewById(R.id.but_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(singleChooseAdapterone!=null){
                        theatreSize=-1;
                        singleChooseAdapterone.resetData();
                    }
                    if(singleChooseAdaptertwo!=null){
                        theatrefee=-1;
                        singleChooseAdaptertwo.resetData();
                    }
                    if(singleChooseAdapterthree!=null){
                        showActorAccount=-1;
                        singleChooseAdapterthree.resetData();
                    }
                }
            });

            content.findViewById(R.id.but_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null&&popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                }
            });

            popupWindow.setContentView(content);

        }else if(type==2){

            View content = mLayoutInflater.inflate(R.layout.found_works_kind_filter_popuwindow_item, null);

            ArrayList<String> stringsone=new ArrayList<String>();
            stringsone.add("不限");
            stringsone.add("话剧");
            stringsone.add("儿童剧");
            stringsone.add("音乐剧");
            stringsone.add("爆笑喜剧");
            stringsone.add("音乐会");
            stringsone.add("演唱会");
            stringsone.add("戏剧国粹");
            stringsone.add("相声曲艺");
            stringsone.add("杂技魔术");
            stringsone.add("实验当代");
            RecyclerView recyclerViewone= (RecyclerView) content.findViewById(R.id.rcv_one);
            final SingleChooseAdapter singleChooseAdapterone=new SingleChooseAdapter(getContext(),stringsone,workskind);
            singleChooseAdapterone.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    workskind=position;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationone=new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL,40,20);
            recyclerViewone.setItemAnimator(null);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewone.addItemDecoration(gridLayoutItemDecorationone);
            recyclerViewone.setLayoutManager(gridLayoutManager);
            recyclerViewone.setAdapter(singleChooseAdapterone);
            recyclerViewone.setNestedScrollingEnabled(false);

            popupWindow.setContentView(content);
        }
        if(popupWindow!=null&&!popupWindow.isShowing()){
            popupWindow.showAsDropDown(llChoose,0,0);
        }

    }

    private void getList(){

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_WORKS);
        params.put("page", "1");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign=="+sign );
        RequestUtil.request(true, Constant.URL_GET_WORKS, params, 106, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    Gson gson = new Gson();

                    if(list==null){
                        list=new ArrayList<Work>();
                    }
                    list.clear();
                    list=gson.fromJson(obj,new TypeToken<List<Work>>(){}.getType());

                    Log.e(TAG, "onSuccess: theatres.size=="+list.size());

                    uiHandler.sendEmptyMessage(0);
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
