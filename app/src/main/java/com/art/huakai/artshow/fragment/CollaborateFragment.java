package com.art.huakai.artshow.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 合作Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class CollaborateFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = CollaborateFragment.class.getSimpleName();
    private EditText edtSearch;

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
    public void initView(View rootView) {
        Drawable drawableLeft = getResources().getDrawable(R.mipmap.chinashow_search);
        drawableLeft.setBounds(
                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX),
                0,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX)
                        + getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX));
        edtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        edtSearch.setCompoundDrawables(drawableLeft, null, null, null);

    }

    @Override
    public void setView() {

    }
}
