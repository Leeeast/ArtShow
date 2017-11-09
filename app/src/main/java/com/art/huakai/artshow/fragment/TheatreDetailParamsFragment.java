package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EquipAdapter;
import com.art.huakai.artshow.adapter.SkillParamsAdapter;
import com.art.huakai.artshow.adapter.holder.SkillParamsHolder;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.entity.SkillBean;
import com.art.huakai.artshow.entity.TechParamsBean;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreNotifyEvent;
import com.art.huakai.artshow.utils.TheatreTechParamsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TheatreDetailParamsFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_THEATRE = "PARAMS_THEATRE";
    private View scrollView;
    private TheatreDetailBean mTheatreDetailBean;
    private RecyclerView recyclerViewParams;
    private TextView tvSkillParams;
    private List<TechParamsBean> techParamsBeans;
    private RecyclerView recyclerViewEquip;
    private ArrayList<String> techParamsAdded;

    public static TheatreDetailParamsFragment newInstance(TheatreDetailBean theatreDetailBean) {
        TheatreDetailParamsFragment fragment = new TheatreDetailParamsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_THEATRE, theatreDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mTheatreDetailBean = (TheatreDetailBean) getArguments().getSerializable(PARAMS_THEATRE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_theatre_detail_params, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSkillParams = (TextView) view.findViewById(R.id.tv_skill_params);
        recyclerViewParams = (RecyclerView) view.findViewById(R.id.recycle_params);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewParams.setLayoutManager(gridLayoutManager);
        GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(
                2,
                GridLayoutManager.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_5PX));
        recyclerViewParams.addItemDecoration(gridLayoutItemDecorationone);

        techParamsBeans = new ArrayList<>();
        TheatreTechParamsUtil.getTheatreTechParamsAddedList(techParamsBeans, mTheatreDetailBean);
        SkillParamsAdapter skillParamsAdapter = new SkillParamsAdapter(techParamsBeans);
        recyclerViewParams.setAdapter(skillParamsAdapter);

        ArrayList<String> techParams = new ArrayList<>();
        techParamsAdded = new ArrayList<>();
        TheatreTechParamsUtil.getTheatreEquipAddedList(techParamsAdded, mTheatreDetailBean);
        String[] stringArray = getResources().getStringArray(R.array.theatre_tech_param);
        techParams.addAll(Arrays.asList(stringArray));

        recyclerViewEquip = (RecyclerView) view.findViewById(R.id.recycle_equip);
        GridLayoutManager gridLayoutEquip = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewParams.setLayoutManager(gridLayoutManager);
        GridLayoutItemDecoration gridLayoutItemDecoration = new GridLayoutItemDecoration(
                4,
                GridLayoutManager.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX));
        recyclerViewParams.addItemDecoration(gridLayoutItemDecorationone);
        recyclerViewEquip.setLayoutManager(gridLayoutEquip);
        recyclerViewEquip.addItemDecoration(gridLayoutItemDecoration);
        EquipAdapter equipAdapter = new EquipAdapter(techParams, techParamsAdded);
        recyclerViewEquip.setAdapter(equipAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(TheatreNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        TheatreDetailInfo t = TheatreDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TheatreNotifyEvent.NOTIFY_THEATRE_TECH:
                TheatreTechParamsUtil.getTheatreTechParamsAddedList(techParamsBeans, t);
                recyclerViewParams.getAdapter().notifyDataSetChanged();

                TheatreTechParamsUtil.getTheatreTechParamsAddedList(techParamsAdded);
                recyclerViewEquip.getAdapter().notifyDataSetChanged();
                break;
        }
    }
}
