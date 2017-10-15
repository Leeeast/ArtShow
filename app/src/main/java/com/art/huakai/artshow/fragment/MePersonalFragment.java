package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.DataUploadActivity;
import com.art.huakai.artshow.activity.ResumeActivity;
import com.art.huakai.artshow.adapter.MeUnloginAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.JumpCode;

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
                invokActivity(getContext(), ResumeActivity.class, null, JumpCode.FLAG_REQ_RESUME_MY);
                break;
            case R.id.rly_pro_auth:
                invokActivity(getContext(), DataUploadActivity.class, null, JumpCode.FLAG_REQ_DATA_UPLOAD);
                break;
        }
    }
}
