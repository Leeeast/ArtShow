package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class DataUploadFragment extends BaseFragment implements View.OnClickListener {
    private static final String PARAMS_FROM = "PARAMS_FROM";
    public static final String FROM_LOGIN = "FROM_LOGIN";
    public static final String FROM_ME = "FROM_ME";
    private int mUserType;
    private TextInputLayout tiLyAuthName, tiLyIdentityNumber, tiLyIdentityConnectName, tiLyIdentityConnectPhone;
    private EditText edtAuthName, edtIdentityNumber, edtIdentityConnectName, edtIdentityConnectPhone;
    private TextView tvDataUploadTip;
    private String mFrom;

    public DataUploadFragment() {
        // Required empty public constructor
    }

    public static DataUploadFragment newInstance(String from) {
        DataUploadFragment fragment = new DataUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mUserType = LocalUserInfo.getInstance().getUserType();
        if (bundle != null) {
            mFrom = bundle.getString(PARAMS_FROM, "");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_data_upload;
    }

    @Override
    public void initView(View rootView) {
        tiLyAuthName = (TextInputLayout) rootView.findViewById(R.id.tily_auth_name);
        tiLyIdentityNumber = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_number);
        tiLyIdentityConnectName = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_connect_name);
        tiLyIdentityConnectPhone = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_connect_phone);
        edtAuthName = (EditText) rootView.findViewById(R.id.edt_auth_name);
        edtIdentityNumber = (EditText) rootView.findViewById(R.id.edt_identity_number);
        edtIdentityConnectName = (EditText) rootView.findViewById(R.id.edt_identity_connect_name);
        edtIdentityConnectPhone = (EditText) rootView.findViewById(R.id.edt_identity_connect_phone);
        tvDataUploadTip = (TextView) rootView.findViewById(R.id.tv_data_upload_tip);
        TextView tvSubtitle = (TextView) rootView.findViewById(R.id.tv_subtitle);
        if (mFrom.equals(FROM_LOGIN)) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(R.string.jump_auth);
            tvSubtitle.setOnClickListener(this);
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.btn_commit_data).setOnClickListener(this);
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);

    }

    @Override
    public void setView() {
        String hintName = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_name_personal) :
                getString(R.string.data_auth_name_institution);
        String hintIdentifyID = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_identify_personal) :
                getString(R.string.data_auth_identify_institution);
        String uploadTip = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_upload_tip_personal) :
                getString(R.string.data_auth_upload_tip_institution);

        tiLyAuthName.setHint(hintName);
        tiLyIdentityNumber.setHint(hintIdentifyID);
        tiLyIdentityConnectName.setHint(getString(R.string.data_auth_connect_name));
        tiLyIdentityConnectPhone.setHint(getString(R.string.data_auth_connect_phone));
        tvDataUploadTip.setText(uploadTip);
        if (mUserType == LocalUserInfo.USER_TYPE_PERSONAL) {
            tiLyIdentityConnectName.setVisibility(View.GONE);
            tiLyIdentityConnectPhone.setVisibility(View.GONE);
        } else {
            tiLyIdentityConnectName.setVisibility(View.VISIBLE);
            tiLyIdentityConnectPhone.setVisibility(View.VISIBLE);
        }
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
            case R.id.tv_subtitle:
                Toast.makeText(getContext(), "跳过", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
