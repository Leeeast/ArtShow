package com.art.huakai.artshow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/4.
 */

public class DisPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[] mTabArray;

    public DisPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabArray) {
        super(fm);
        this.mFragments = fragments;
        this.mTabArray = tabArray;
    }

    public DisPagerAdapter(FragmentManager fm) {
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
