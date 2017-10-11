package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class PerfectInfoFragment extends BaseFragment implements View.OnClickListener {
    private final int
            USER_TYPE_PERSONAL = 3,
            USER_TYPE_THEATRE = 1,
            USER_TYPE_PUBLISHER = 2;
    private LocalUserInfo localUserInfo;
    private EditText edtName;
    private TextView tvPerfectDes;
    private ShowProgressDialog showProgressDialog;
    private TextInputLayout tilyName;

    public PerfectInfoFragment() {
        // Required empty public constructor
    }

    public static PerfectInfoFragment newInstance() {
        PerfectInfoFragment fragment = new PerfectInfoFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        localUserInfo = LocalUserInfo.getInstance();
        showProgressDialog = new ShowProgressDialog(getContext());
        //TODO 头像选择做好后记得去掉
        localUserInfo.setDp("http://139.224.47.213/image//8a999cce5ef5afd7015efb9c150c000d@thumb.png");
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_perfect_info;
    }

    @Override
    public void initView(View rootView) {

        edtName = (EditText) rootView.findViewById(R.id.edt_name);
        tvPerfectDes = (TextView) rootView.findViewById(R.id.tv_perfect_des);
        tilyName = (TextInputLayout) rootView.findViewById(R.id.tily_name);

        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_next_step).setOnClickListener(this);
    }

    @Override
    public void setView() {
        switch (localUserInfo.getUserType()) {
            case USER_TYPE_PERSONAL:
                tilyName.setHint(getString(R.string.perfect_name_personal));
                tvPerfectDes.setText(R.string.perfect_des_other);
                break;
            case USER_TYPE_THEATRE:
                tilyName.setHint(getString(R.string.perfect_name_theatre));
                tvPerfectDes.setText(R.string.perfect_des_other);
                break;
            case USER_TYPE_PUBLISHER:
                tilyName.setHint(getString(R.string.perfect_name_publisher));
                tvPerfectDes.setText(R.string.perfect_des_publisher);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_next_step:
                doPerfectInfo();
                break;
        }
    }

    /**
     * 提交信息
     */
    private void doPerfectInfo() {
        String userName = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(localUserInfo.getDp())) {
            showToast(getString(R.string.tip_set_avatar));
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            showToast(getString(R.string.tip_set_name));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", localUserInfo.getId());
        params.put("accessToken", localUserInfo.getAccessToken());
        params.put("name", userName);
        params.put("dp", localUserInfo.getDp());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        LogUtil.i(TAG, "params=" + params);
        RequestUtil.request(true, Constant.URL_USER_EDITINFO, params, 15, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String dp = jsonObject.getString("dp");
                        String name = jsonObject.getString("name");
                        int status = jsonObject.getInt("status");
                        localUserInfo.setDp(dp);
                        localUserInfo.setName(name);
                        localUserInfo.setStatus(status);
                        SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_PERFECT_INFO_SUC));
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(getString(R.string.tip_data_parsing_failure));
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }
}
