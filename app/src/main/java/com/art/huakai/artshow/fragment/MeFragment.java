package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 我Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class MeFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = MeFragment.class.getSimpleName();
    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }
}
