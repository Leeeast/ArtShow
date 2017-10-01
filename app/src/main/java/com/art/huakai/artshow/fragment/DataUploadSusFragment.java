package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.TypeConfirmDialog;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DataUploadSusFragment extends BaseFragment implements View.OnClickListener {

    public DataUploadSusFragment() {
        // Required empty public constructor
    }

    public static DataUploadSusFragment newInstance() {
        DataUploadSusFragment fragment = new DataUploadSusFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_data_upload_suc;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_return_main).setOnClickListener(this);
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
            case R.id.btn_return_main:
                getActivity().finish();
                break;
        }
    }
}
