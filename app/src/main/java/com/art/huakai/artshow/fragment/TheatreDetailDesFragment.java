package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.entity.TheatreDetailBean;


public class TheatreDetailDesFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_THEATRE = "PARAMS_THEATRE";
    private View scrollView;
    private TheatreDetailBean mTheatreDetailBean;
    private ImageView ivPageEmpty;
    private TextView tvContent;

    public static TheatreDetailDesFragment newInstance(TheatreDetailBean theatreDetailBean) {
        TheatreDetailDesFragment fragment = new TheatreDetailDesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_THEATRE, theatreDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTheatreDetailBean = (TheatreDetailBean) getArguments().getSerializable(PARAMS_THEATRE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_theatre_detail_des, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        if (mTheatreDetailBean != null && !TextUtils.isEmpty(mTheatreDetailBean.getDetailedIntroduce())) {
            ivPageEmpty.setVisibility(View.GONE);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(mTheatreDetailBean.getDetailedIntroduce());
        } else {
            ivPageEmpty.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
        }
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
