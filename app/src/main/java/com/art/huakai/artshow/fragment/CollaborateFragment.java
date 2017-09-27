package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 合作Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class CollaborateFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = CollaborateFragment.class.getSimpleName();

    public CollaborateFragment() {
        // Required empty public constructor
    }

    public static CollaborateFragment newInstance() {
        CollaborateFragment fragment = new CollaborateFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_collaborate;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }
}
