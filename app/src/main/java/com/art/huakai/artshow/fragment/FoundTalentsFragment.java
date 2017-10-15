package com.art.huakai.artshow.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoundTalentsFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {


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
    private ArrayList<String> list;
    private LookingWorksAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private LinearItemDecoration linearItemDecoration;

    private PopupWindow popupWindow;
    private LayoutInflater mLayoutInflater;
    private int WholeRankingRule = 0;
    private int theatreSize = -1;
    private int theatrefee = -1;
    private int college = -1;
    private int workskind = -1;

    private int actorType = -1;
    private int directorType = -1;
    private int skillKind = -1;
    private int managerKind = -1;
    private int noRequest=-1;
    private int others=-1;

    public FoundTalentsFragment() {
        // Required empty public constructor
    }

    public static FoundTalentsFragment newInstance() {
        FoundTalentsFragment fragment = new FoundTalentsFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_found_talents;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {
        ivChoosePrice.setOnClickListener(this);
        ivChooseNumber.setOnClickListener(this);
        ivRealChoose.setOnClickListener(this);

        linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearItemDecoration = new LinearItemDecoration((int) getContext().getResources().getDimension(R.dimen.DIMEN_14PX));
        recyclerView.setLoadingListener(this);
        recyclerView.setLayoutManager(linearlayoutManager);
//        recyclerView.addItemDecoration(linearItemDecoration);
        list = new ArrayList<String>();
        lookingWorksAdapter = new LookingWorksAdapter(getContext(), list);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingWorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                Toast.makeText(getContext(), "itemclick", Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView.refresh();
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

        new Handler().postDelayed(new Runnable() {
            public void run() {
                list.clear();
                for (int i = 0; i < 50; i++) {
                    list.add("文字" + i);
                }
                recyclerView.refreshComplete();
            }

        }, 2000);

    }

    @Override
    public void onLoadMore() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < 30; i++) {
                    list.add("文字" + i);
                }
                recyclerView.loadMoreComplete();
            }

        }, 2000);

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
            View content = mLayoutInflater.inflate(R.layout.found_talent_whole_ranking_popuwindow_item, null);
            if (WholeRankingRule == 1) {
                content.findViewById(R.id.iv_one).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_one);
                view.setTextColor(0xffe93c2c);
            } else if (WholeRankingRule == 2) {
                content.findViewById(R.id.iv_two).setVisibility(View.VISIBLE);
                TextView view = (TextView) content.findViewById(R.id.tv_two);
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
        } else if (type == 3) {
            View content = mLayoutInflater.inflate(R.layout.found_talents_real_filter_popuwindow_item, null);

            ArrayList<String> stringsone = new ArrayList<String>();
            stringsone.add("不限");
            stringsone.add("10岁以下");
            stringsone.add("10-20岁");
            stringsone.add("21-30岁");
            stringsone.add("31-45岁");
            stringsone.add("46-60岁");
            stringsone.add("60岁以上");
            RecyclerView recyclerViewone = (RecyclerView) content.findViewById(R.id.rcv_one);
            final SingleChooseAdapter singleChooseAdapterone = new SingleChooseAdapter(getContext(), stringsone, theatreSize);
            singleChooseAdapterone.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
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
            stringstwo.add("中央戏剧学院");
            stringstwo.add("北京电影学院");
            stringstwo.add("中央戏曲学院");
            stringstwo.add("北京舞蹈学院");
            stringstwo.add("上海戏剧学院");
            stringstwo.add("中国传媒大学");
            stringstwo.add("中央音乐学院");
            stringstwo.add("解放军艺术学校");
            stringstwo.add("其他");
            final SingleChooseAdapter singleChooseAdaptertwo = new SingleChooseAdapter(getContext(), stringstwo, theatrefee);
            singleChooseAdaptertwo.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    theatrefee = position;
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

            final SingleChooseAdapter singleChooseAdapterthree = new SingleChooseAdapter(getContext(), stringsthree, college);
            singleChooseAdapterthree.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    college = position;
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
                        theatreSize = -1;
                        singleChooseAdapterone.resetData();
                    }
                    if (singleChooseAdaptertwo != null) {
                        theatrefee = -1;
                        singleChooseAdaptertwo.resetData();
                    }
                    if (singleChooseAdapterthree != null) {
                        college = -1;
                        singleChooseAdapterthree.resetData();
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

        } else if (type == 2) {

            View content = mLayoutInflater.inflate(R.layout.found_talents_skill_kind_popuwindow_item, null);
            final TextView tv_no_request= (TextView) content.findViewById(R.id.tv_no_request);
            final TextView tv_others= (TextView) content.findViewById(R.id.tv_others);
            if(noRequest==0){
                tv_no_request.setTextColor(0xffe93c2c);
                tv_no_request.setBackgroundResource(R.drawable.red_rectang);
            }else{
                tv_no_request.setTextColor(0xff9b9b9b);
                tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
            }




            if(others==0){
                tv_others.setTextColor(0xffe93c2c);
                tv_others.setBackgroundResource(R.drawable.red_rectang);
            }else{
                tv_others.setTextColor(0xff9b9b9b);
                tv_others.setBackgroundResource(R.drawable.grey_rectang);
            }

            tv_no_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    others=0;
                    tv_others.setTextColor(0xffe93c2c);
                    tv_others.setBackgroundResource(R.drawable.red_rectang);
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest=-1;
                }
            });

            ArrayList<String> stringsone = new ArrayList<String>();
            stringsone.add("话剧演员");
            stringsone.add("影视演员");
            stringsone.add("戏曲演员");
            stringsone.add("舞蹈演员");
            stringsone.add("歌唱演员");
            stringsone.add("器乐演员");
            stringsone.add("曲艺演员");
            stringsone.add("杂技演员");
            RecyclerView recyclerViewone = (RecyclerView) content.findViewById(R.id.rcv_one);
            final SingleChooseAdapter singleChooseAdapterone = new SingleChooseAdapter(getContext(), stringsone, actorType);
            singleChooseAdapterone.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    actorType = position;
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest=-1;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewone.setItemAnimator(null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewone.addItemDecoration(gridLayoutItemDecorationone);
            recyclerViewone.setLayoutManager(gridLayoutManager);
            recyclerViewone.setAdapter(singleChooseAdapterone);
            recyclerViewone.setNestedScrollingEnabled(false);

            RecyclerView recyclerViewtwo = (RecyclerView) content.findViewById(R.id.rcv_two);
            ArrayList<String> stringstwo = new ArrayList<String>();
            stringstwo.add("编剧");
            stringstwo.add("导演");
            stringstwo.add("编舞");
            stringstwo.add("作词作曲");
            stringstwo.add("戏剧构作");

            final SingleChooseAdapter singleChooseAdaptertwo = new SingleChooseAdapter(getContext(), stringstwo, directorType);
            singleChooseAdaptertwo.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    directorType = position;
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest=-1;

                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationtwo = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewtwo.setItemAnimator(null);
            GridLayoutManager gridLayoutManagertwo = new GridLayoutManager(getContext(), 3);
            gridLayoutManagertwo.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewtwo.addItemDecoration(gridLayoutItemDecorationtwo);
            recyclerViewtwo.setLayoutManager(gridLayoutManagertwo);
            recyclerViewtwo.setAdapter(singleChooseAdaptertwo);
            recyclerViewtwo.setNestedScrollingEnabled(false);


            RecyclerView recyclerViewthree = (RecyclerView) content.findViewById(R.id.rcv_three);
            ArrayList<String> stringsthree = new ArrayList<String>();
            stringsthree.add("舞美设计");
            stringsthree.add("灯光设计");
            stringsthree.add("服装设计");
            stringsthree.add("音乐音响");
            stringsthree.add("多媒体视频");
            stringsthree.add("舞台特效");
            stringsthree.add("平面设计");

            final SingleChooseAdapter singleChooseAdapterthree = new SingleChooseAdapter(getContext(), stringsthree, skillKind);
            singleChooseAdapterthree.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    skillKind = position;
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest=-1;
                }
            });
            GridLayoutItemDecoration gridLayoutItemDecorationthree = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
            recyclerViewthree.setItemAnimator(null);
            GridLayoutManager gridLayoutManagerthree = new GridLayoutManager(getContext(), 3);
            gridLayoutManagerthree.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewthree.addItemDecoration(gridLayoutItemDecorationthree);
            recyclerViewthree.setLayoutManager(gridLayoutManagerthree);
            recyclerViewthree.setAdapter(singleChooseAdapterthree);
            recyclerViewthree.setNestedScrollingEnabled(false);


            RecyclerView recyclerViewfour = (RecyclerView) content.findViewById(R.id.rcv_four);
            ArrayList<String> stringsfour = new ArrayList<String>();
            stringsfour.add("制作人");
            stringsfour.add("宣传推广");
            stringsfour.add("票务营销");
            stringsfour.add("商务合作");
            stringsfour.add("舞台监督");
            stringsfour.add("剧场经理");

            final SingleChooseAdapter singleChooseAdapterfour = new SingleChooseAdapter(getContext(), stringsfour, managerKind);
            singleChooseAdapterfour.setOnItemClickListener(new SingleChooseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String url) {
                    managerKind = position;
                    tv_no_request.setTextColor(0xff9b9b9b);
                    tv_no_request.setBackgroundResource(R.drawable.grey_rectang);
                    noRequest=-1;
                }
        });
        GridLayoutItemDecoration gridLayoutItemDecorationfour = new GridLayoutItemDecoration(3, GridLayoutManager.VERTICAL, 40, 20);
        recyclerViewfour.setItemAnimator(null);
        GridLayoutManager gridLayoutManagerfour = new GridLayoutManager(getContext(), 3);
        gridLayoutManagerfour.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewfour.addItemDecoration(gridLayoutItemDecorationfour);
        recyclerViewfour.setLayoutManager(gridLayoutManagerfour);
        recyclerViewfour.setAdapter(singleChooseAdapterfour);
        recyclerViewfour.setNestedScrollingEnabled(false);


        content.findViewById(R.id.but_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (singleChooseAdapterone != null) {
                        actorType = -1;
                        singleChooseAdapterone.resetData();
                    }
                    if (singleChooseAdaptertwo != null) {
                        directorType = -1;
                        singleChooseAdaptertwo.resetData();
                    }
                    if (singleChooseAdapterthree != null) {
                        skillKind = -1;
                        singleChooseAdapterthree.resetData();
                    }
                    if (singleChooseAdapterfour != null) {
                        managerKind = -1;
                        singleChooseAdapterfour.resetData();
                    }
                    others=-1;
                    noRequest=-1;
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

            tv_no_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noRequest=0;
                    tv_no_request.setTextColor(0xffe93c2c);
                    tv_no_request.setBackgroundResource(R.drawable.red_rectang);
                    tv_others.setTextColor(0xff9b9b9b);
                    tv_others.setBackgroundResource(R.drawable.grey_rectang);
                    others=-1;

                    if (singleChooseAdapterone != null) {
                        actorType = -1;
                        singleChooseAdapterone.resetData();
                    }
                    if (singleChooseAdaptertwo != null) {
                        directorType = -1;
                        singleChooseAdaptertwo.resetData();
                    }
                    if (singleChooseAdapterthree != null) {
                        skillKind = -1;
                        singleChooseAdapterthree.resetData();
                    }
                    if (singleChooseAdapterfour != null) {
                        managerKind = -1;
                        singleChooseAdapterfour.resetData();
                    }

                }
            });


            popupWindow.setContentView(content);
        }
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(llChoose, 0, 0);
        }

    }


}
