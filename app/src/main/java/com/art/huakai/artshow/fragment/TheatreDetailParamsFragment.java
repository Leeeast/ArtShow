package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;


public class TheatreDetailParamsFragment extends HeaderViewPagerFragment {

    private View scrollView;

    public static TheatreDetailParamsFragment newInstance() {
        return new TheatreDetailParamsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_theatre_detail_params, container, false);

        return scrollView;
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
