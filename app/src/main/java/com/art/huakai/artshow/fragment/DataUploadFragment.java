package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DataUploadFragment extends BaseFragment implements View.OnClickListener {

    public DataUploadFragment() {
        // Required empty public constructor
    }

    public static DataUploadFragment newInstance() {
        DataUploadFragment fragment = new DataUploadFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_data_upload;
    }

    @Override
    public void initView(View rootView) {
        TextView tvDataUploadDes = (TextView) rootView.findViewById(R.id.tv_data_upload_des);
        String des = String.format(getString(R.string.data_commit_des), getString(R.string.app_name));
        tvDataUploadDes.setText(des);

        rootView.findViewById(R.id.btn_commit_data).setOnClickListener(this);
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);

    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().setResult(JumpCode.FLAG_RES_DATA_AUTH);
                getActivity().finish();
                break;
            case R.id.btn_commit_data:
                DataUploadSusFragment dataUploadSusFragment = DataUploadSusFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, dataUploadSusFragment, dataUploadSusFragment.getTAG()).commit();
                break;
        }
    }
}
