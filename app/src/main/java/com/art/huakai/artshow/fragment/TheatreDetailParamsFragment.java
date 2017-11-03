package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.SkillParamsAdapter;
import com.art.huakai.artshow.adapter.holder.SkillParamsHolder;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.entity.SkillBean;
import com.art.huakai.artshow.entity.TechParamsBean;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.utils.TheatreTechParamsUtil;

import java.util.ArrayList;
import java.util.List;


public class TheatreDetailParamsFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_THEATRE = "PARAMS_THEATRE";
    private View scrollView;
    private TheatreDetailBean mTheatreDetailBean;
    private RecyclerView recyclerViewParams;
    private TextView tvSkillParams;

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

        List<TechParamsBean> techParamsBeans = new ArrayList<>();
        TheatreTechParamsUtil.getTheatreTechParamsAddedList(techParamsBeans, mTheatreDetailBean);
        SkillParamsAdapter skillParamsAdapter = new SkillParamsAdapter(techParamsBeans);
        recyclerViewParams.setAdapter(skillParamsAdapter);

    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
