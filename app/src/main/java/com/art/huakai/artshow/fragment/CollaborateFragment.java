package com.art.huakai.artshow.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.CollAdapter;
import com.art.huakai.artshow.base.BaseFragment;

import java.util.ArrayList;

/**
 * 合作Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class CollaborateFragment extends BaseFragment {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = CollaborateFragment.class.getSimpleName();
    private EditText edtSearch;
    private RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyvleview_coll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setView() {
        ArrayList<String> collList = new ArrayList<>();
        collList.add(null);
        for (int i = 0; i < 10; i++) {
            collList.add("item ：" + i);
        }
        CollAdapter collAdapter = new CollAdapter(collList);
        recyclerView.setAdapter(collAdapter);
    }
}
