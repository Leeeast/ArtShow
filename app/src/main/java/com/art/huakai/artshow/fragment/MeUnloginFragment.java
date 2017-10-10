package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.MeUnloginAdapter;
import com.art.huakai.artshow.base.BaseFragment;

import java.util.ArrayList;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class MeUnloginFragment extends BaseFragment {

    private RecyclerView rlvRecommend;

    public MeUnloginFragment() {
        // Required empty public constructor
    }

    public static MeUnloginFragment newInstance() {
        MeUnloginFragment fragment = new MeUnloginFragment();
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me_unlogin;
    }

    @Override
    public void initView(View rootView) {
        rlvRecommend = (RecyclerView) rootView.findViewById(R.id.rlv_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvRecommend.setLayoutManager(linearLayoutManager);

        ArrayList<String> strings = new ArrayList<>();
        strings.add(null);
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        MeUnloginAdapter meUnloginAdapter = new MeUnloginAdapter(strings);
        rlvRecommend.setAdapter(meUnloginAdapter);
    }

    @Override
    public void setView() {

    }
}
