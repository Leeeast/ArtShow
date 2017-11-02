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
import com.art.huakai.artshow.entity.WorksDetailBean;


public class ProjectDetailPoltFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_PROJECT = "PARAMS_PROJECT";

    private View scrollView;
    private WorksDetailBean mWorksDatailBean;
    private ImageView ivPageEmpty;
    private TextView tvContent;

    public static ProjectDetailPoltFragment newInstance(WorksDetailBean worksDetailBean) {
        ProjectDetailPoltFragment fragment = new ProjectDetailPoltFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_PROJECT, worksDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorksDatailBean = (WorksDetailBean) getArguments().getSerializable(PARAMS_PROJECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_project_detail_polt, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        if (mWorksDatailBean != null && !TextUtils.isEmpty(mWorksDatailBean.getPlot())) {
            ivPageEmpty.setVisibility(View.GONE);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(mWorksDatailBean.getPlot());
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
