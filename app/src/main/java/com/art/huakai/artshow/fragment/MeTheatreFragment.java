package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.MeTheatreAdapter;
import com.art.huakai.artshow.adapter.MeUnloginAdapter;
import com.art.huakai.artshow.base.BaseFragment;

import java.util.ArrayList;


/**
 * 我Fragment,剧场，出品方
 * Created by lidongliang on 2017/9/27.
 */
public class MeTheatreFragment extends BaseFragment{

    private RecyclerView rlvTheatre;

    public MeTheatreFragment() {
        // Required empty public constructor
    }

    public static MeTheatreFragment newInstance() {
        MeTheatreFragment fragment = new MeTheatreFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me_theatre;
    }

    @Override
    public void initView(View rootView) {
        rlvTheatre = (RecyclerView) rootView.findViewById(R.id.rlv_theatre);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvTheatre.setLayoutManager(linearLayoutManager);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        MeTheatreAdapter meTheatreAdapter = new MeTheatreAdapter(strings);
        rlvTheatre.setAdapter(meTheatreAdapter);
    }

    @Override
    public void setView() {

    }
}
