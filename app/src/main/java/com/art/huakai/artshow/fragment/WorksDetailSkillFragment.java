package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;


public class WorksDetailSkillFragment extends HeaderViewPagerFragment {

    private View scrollView;

    public static WorksDetailSkillFragment newInstance() {
        return new WorksDetailSkillFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_works_detail_show, container, false);
//        LinearLayout views = (LinearLayout) scrollView.findViewById(R.id.container);
//        for (int i = 0; i < 10; i++) {
//            View view = new View(getActivity());
//            view.setBackgroundColor(Utils.generateBeautifulColor());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
//            view.setLayoutParams(params);
//            views.addView(view);
//        }
        return scrollView;
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
