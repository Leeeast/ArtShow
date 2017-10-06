package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TypeConfirmDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.MD5;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class AccountTypeSelectFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TypeConfirmDialog mTypeConfirmDialog;
    private LocalUserInfo localUserInfo;
    private CheckBox chkTypePublisher, chkTypeTheatre, chkTypePersonal;

    private final int
            USER_TYPE_PERSONAL = 3,
            USER_TYPE_THEATRE = 1,
            USER_TYPE_PUBLISHER = 2,
            USER_TYPE_NONE = -1;
    private ShowProgressDialog showProgressDialog;

    public AccountTypeSelectFragment() {
        // Required empty public constructor
    }

    public static AccountTypeSelectFragment newInstance() {
        AccountTypeSelectFragment fragment = new AccountTypeSelectFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        localUserInfo = LocalUserInfo.instance();
        localUserInfo.setUserType(USER_TYPE_PUBLISHER);
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_account_type_select;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_next_step).setOnClickListener(this);
        chkTypePublisher = (CheckBox) rootView.findViewById(R.id.chk_type_publisher);
        chkTypeTheatre = (CheckBox) rootView.findViewById(R.id.chk_type_theatre);
        chkTypePersonal = (CheckBox) rootView.findViewById(R.id.chk_type_personal);

        rootView.findViewById(R.id.lly_publisher).setOnClickListener(this);
        rootView.findViewById(R.id.lly_theatre).setOnClickListener(this);
        rootView.findViewById(R.id.lly_personal).setOnClickListener(this);


        chkTypePublisher.setOnCheckedChangeListener(this);
        chkTypeTheatre.setOnCheckedChangeListener(this);
        chkTypePersonal.setOnCheckedChangeListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                if (localUserInfo.getUserType() == -1) {
                    showToast(getString(R.string.tip_user_type_select));
                    return;
                }
                if (mTypeConfirmDialog == null) {
                    mTypeConfirmDialog = TypeConfirmDialog.newInstence();
                    mTypeConfirmDialog.setOnCallBack(new TypeConfirmDialog.CallBack() {
                        @Override
                        public void onChoose(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            doBindPhone();
                        }

                        @Override
                        public void onCancel(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                        }
                    });
                }
                mTypeConfirmDialog.show(getFragmentManager(), "TYPECONFIRM.DIALOG");
                break;
            case R.id.lly_publisher:
                chkTypePublisher.setChecked(!chkTypePublisher.isChecked());
                break;
            case R.id.lly_theatre:
                chkTypeTheatre.setChecked(!chkTypeTheatre.isChecked());
                break;
            case R.id.lly_personal:
                chkTypePersonal.setChecked(!chkTypePersonal.isChecked());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chk_type_publisher:
                if (isChecked) {
                    chkTypeTheatre.setChecked(false);
                    chkTypePersonal.setChecked(false);
                    localUserInfo.setUserType(USER_TYPE_PUBLISHER);
                } else {
                    if (localUserInfo.getUserType() == USER_TYPE_PUBLISHER) {
                        localUserInfo.setUserType(USER_TYPE_NONE);
                    }
                }
                break;
            case R.id.chk_type_theatre:
                if (isChecked) {
                    chkTypePersonal.setChecked(false);
                    chkTypePublisher.setChecked(false);
                    localUserInfo.setUserType(USER_TYPE_THEATRE);
                } else {
                    if (localUserInfo.getUserType() == USER_TYPE_THEATRE) {
                        localUserInfo.setUserType(USER_TYPE_NONE);
                    }
                }
                break;
            case R.id.chk_type_personal:
                if (isChecked) {
                    chkTypeTheatre.setChecked(false);
                    chkTypePublisher.setChecked(false);
                    localUserInfo.setUserType(USER_TYPE_PERSONAL);
                } else {
                    if (localUserInfo.getUserType() == USER_TYPE_PERSONAL) {
                        localUserInfo.setUserType(USER_TYPE_NONE);
                    }
                }
                break;
        }
    }

    /**
     * 绑定账户类型
     */
    public void doBindPhone() {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", localUserInfo.getId());
        params.put("accessToken", localUserInfo.getAccessToken());
        params.put("userType", String.valueOf(localUserInfo.getUserType()));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "parmas:" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_BIND_TYPE, params, 12, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_ACCOUNT_TYPE_AFFIRM));
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
