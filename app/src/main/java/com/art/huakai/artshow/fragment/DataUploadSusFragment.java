package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DataUploadSusFragment extends BaseFragment implements View.OnClickListener {

    private int mUserStatus;

    public DataUploadSusFragment() {
        // Required empty public constructor
    }

    public static DataUploadSusFragment newInstance() {
        DataUploadSusFragment fragment = new DataUploadSusFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mUserStatus = LocalUserInfo.getInstance().getStatus();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_data_upload_suc;
    }

    @Override
    public void initView(View rootView) {
        TextView tvDataCommit = (TextView) rootView.findViewById(R.id.tv_data_commit);
        TextView tvDataCommitDes = (TextView) rootView.findViewById(R.id.tv_data_commit_des);
        if (mUserStatus == LocalUserInfo.USER_STATUS_IDENTIFY_SUC) {
            tvDataCommit.setText(R.string.data_check_success);
            tvDataCommitDes.setText(R.string.data_check_success_des);
        } else {
            tvDataCommit.setText(R.string.data_commit_success);
            tvDataCommitDes.setText(R.string.data_commit_success_des);
        }
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
                getActivity().setResult(JumpCode.FLAG_RES_DATA_AUTH);
                getActivity().finish();
                break;
        }
    }
}
