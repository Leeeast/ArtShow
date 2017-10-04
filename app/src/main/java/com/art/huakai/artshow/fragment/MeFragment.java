package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.utils.DeviceUtils;

/**
 * 我Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class MeFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = MeFragment.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;

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
    public void initView(View rootView) {
        int statusBarHeight = DeviceUtils.getStatusBarHeight(getContext());
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setPadding(0, statusBarHeight, 0, 0);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void setView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
