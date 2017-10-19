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
import com.art.huakai.artshow.adapter.LookingTheatreAdapter;
import com.art.huakai.artshow.adapter.LookingWorksAdapter;
import com.art.huakai.artshow.adapter.SingleChooseAdapter;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class FoundTheatreFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {

    private String TAG="FoundTheatreFragment";

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

    private LookingTheatreAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private LinearItemDecoration linearItemDecoration;
    private PopupWindow popupWindow;
    private LayoutInflater mLayoutInflater;
    private int WholeRankingRule = 0;
    private int theatreSize=-1;
    private int theatrefee=-1;
    public FoundTheatreFragment() {
        // Required empty public constructor
    }

    private String order;
    private String orderType;
    private String seatingMin;
    private String seatingMax;
    private String expenseMin;
    private String expenseMax;
    private int page=1;

    List<Theatre> theatres;

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

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_found_theatre;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {

        ivChoosePrice.setOnClickListener(this);
        ivChooseNumber.setOnClickListener(this);
        ivRealChoose.setOnClickListener(this);
        recyclerView.setLoadingListener(this);
        recyclerView.refresh();



    }

    private void setData(){

        linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearItemDecoration = new LinearItemDecoration((int) getContext().getResources().getDimension(R.dimen.DIMEN_14PX));
        recyclerView.setLayoutManager(linearlayoutManager);
//        recyclerView.addItemDecoration(linearItemDecoration);
        lookingWorksAdapter = new LookingTheatreAdapter(getContext(), theatres);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingTheatreAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                Log.e(TAG, "onItemClickListener: position=="+position );

            }
        });

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

            case R.id.iv_choose_number:

                Toast.makeText(getContext(), "iv_choose_number", Toast.LENGTH_SHORT).show();
                tvWholeRanking.setTextColor(0xff5a4b41);
                ivChoosePrice.setImageResource(R.mipmap.arrow_down_icon);
                tvCityChoose.setTextColor(0xffe93c2c);
                ivChooseNumber.setImageResource(R.mipmap.arrow_active);
                tvRealFilter.setTextColor(0xff5a4b41);
                ivRealChoose.setImageResource(R.mipmap.filter_default);


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

        getList();

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
            View content = mLayoutInflater.inflate(R.layout.found_theatre_real_filter_popuwindow_item, null);

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
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewtwo.addItemDecoration(gridLayoutItemDecorationtwo);
            recyclerViewtwo.setLayoutManager(gridLayoutManagertwo);
            recyclerViewtwo.setAdapter(singleChooseAdaptertwo);
            recyclerViewtwo.setNestedScrollingEnabled(false);
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

        }
        if(popupWindow!=null&&!popupWindow.isShowing()){
            popupWindow.showAsDropDown(llChoose,0,0);
        }

    }


        private void getList(){

        Map<String, String> params = new TreeMap<>();
        Log.e(TAG, "getMessage: Constant.URL_GET_CLASSFY_LIST==" + Constant.URL_GET_THEATRES);
        params.put("page", "1");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.e(TAG, "getList: sign=="+sign );
        RequestUtil.request(true, Constant.URL_GET_THEATRES, params, 105, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                    if (isSuccess) {
                         Gson gson = new Gson();
                        if(theatres==null){
                            theatres=new ArrayList<Theatre>();
                        }
                        theatres.clear();
                        theatres=gson.fromJson(obj,new TypeToken<List<Theatre>>(){}.getType());


                        Log.e(TAG, "onSuccess: theatres.size=="+theatres.size());

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
