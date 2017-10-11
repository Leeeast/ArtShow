package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.MeUnloginAdapter;
import com.art.huakai.artshow.base.BaseFragment;

import java.util.ArrayList;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class MePersonalFragment extends BaseFragment implements View.OnClickListener {

    public MePersonalFragment() {
        // Required empty public constructor
    }

    public static MePersonalFragment newInstance() {
        MePersonalFragment fragment = new MePersonalFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me_personal;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.rly_my_resume).setOnClickListener(this);
        rootView.findViewById(R.id.rly_pro_auth).setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_my_resume:
                Toast.makeText(getContext(), "我的简历", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rly_pro_auth:
                Toast.makeText(getContext(), "专业认证", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
