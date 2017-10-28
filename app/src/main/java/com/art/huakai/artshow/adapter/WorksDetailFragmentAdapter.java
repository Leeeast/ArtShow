package com.art.huakai.artshow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.art.huakai.artshow.base.HeaderViewPagerFragment;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/4.
 */

public class WorksDetailFragmentAdapter extends FragmentPagerAdapter {
    private List<HeaderViewPagerFragment> mFragments;
    private String[] mTabArray;

    public WorksDetailFragmentAdapter(FragmentManager fm, List<HeaderViewPagerFragment> fragments, String[] tabArray) {
        super(fm);
        this.mFragments = fragments;
        this.mTabArray = tabArray;
    }

    public WorksDetailFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabArray[position];
    }
}
