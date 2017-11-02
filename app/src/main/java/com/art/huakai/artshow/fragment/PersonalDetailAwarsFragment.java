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
import com.art.huakai.artshow.entity.TalentDetailBean;
import com.art.huakai.artshow.entity.TalentDetailInfo;


public class PersonalDetailAwarsFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_TALENT = "PARAMS_TALENT";
    private View scrollView;
    private TalentDetailBean mTalentDetailBean;
    private ImageView ivPageEmpty;
    private TextView tvTalentWorks;

    public static PersonalDetailAwarsFragment newInstance(TalentDetailBean talentDetailBean) {
        PersonalDetailAwarsFragment fragment = new PersonalDetailAwarsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_TALENT, talentDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTalentDetailBean = (TalentDetailBean) getArguments().getSerializable(PARAMS_TALENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_scrollview, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        tvTalentWorks = (TextView) view.findViewById(R.id.tv_talent_works);
        if (mTalentDetailBean != null && !TextUtils.isEmpty(mTalentDetailBean.getAwardsDescpt())) {
            ivPageEmpty.setVisibility(View.GONE);
            tvTalentWorks.setVisibility(View.VISIBLE);
            tvTalentWorks.setText(mTalentDetailBean.getAwardsDescpt());
        } else {
            ivPageEmpty.setVisibility(View.VISIBLE);
            tvTalentWorks.setVisibility(View.GONE);
        }
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
