package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.DisPagerAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

/**
 * 发现Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DiscoverFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = DiscoverFragment.class.getSimpleName();
    private String[] mTabArray;
    private ArrayList<Fragment> mFragments;
    private SlidingTabLayout stlDisTab;
    private ViewPager viewPager;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mTabArray = getResources().getStringArray(R.array.discover_tab);
        mFragments = new ArrayList<>();
        FoundTheatreFragment foundTheatreFragment = FoundTheatreFragment.newInstance();
        FoundProductionFragment foundProductionFragment = FoundProductionFragment.newInstance();
        FoundTalentsFragment foundTalentsFragment = FoundTalentsFragment.newInstance();
        mFragments.add(foundTheatreFragment);
        mFragments.add(foundProductionFragment);
        mFragments.add(foundTalentsFragment);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initView(View rootView) {
        int statusBarHeight = DeviceUtils.getStatusBarHeight(getContext());
        LinearLayout lLyRoot = (LinearLayout) rootView.findViewById(R.id.lly_root);
        lLyRoot.setPadding(0, statusBarHeight, 0, 0);

        stlDisTab = (SlidingTabLayout) rootView.findViewById(R.id.stl_dis_tab);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
    }

    @Override
    public void setView() {
        DisPagerAdapter disPagerAdapter = new DisPagerAdapter(getChildFragmentManager(), mFragments, mTabArray);
        viewPager.setAdapter(disPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        stlDisTab.setViewPager(viewPager);
    }
}
