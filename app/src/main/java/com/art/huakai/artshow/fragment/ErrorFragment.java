package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.utils.Utils;


public class ErrorFragment extends HeaderViewPagerFragment {

    private View scrollView;

    public static ErrorFragment newInstance() {
        return new ErrorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_scrollview, container, false);
        return scrollView;
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
