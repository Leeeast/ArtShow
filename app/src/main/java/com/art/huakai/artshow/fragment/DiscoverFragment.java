package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 发现Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DiscoverFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = DiscoverFragment.class.getSimpleName();

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {

    }
}
