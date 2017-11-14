package com.art.huakai.artshow.activity;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class AccountNameChangeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account_des)
    TextView tvAccountDes;
    @BindView(R.id.tily_name)
    TextInputLayout tilyName;
    @BindView(R.id.edt_name)
    TextView edtName;
    @BindView(R.id.tv_data_upload_des)
    TextView tvDataUploadDes;

    private Unbinder mUnbinder;
    private LocalUserInfo localUserInfo;
    private ShowProgressDialog showProgressDialog;
    private RequestCall requestCall;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_account_name_change;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        mUnbinder = ButterKnife.bind(this);
        localUserInfo = LocalUserInfo.getInstance();
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_name_change);

        String des = String.format(getString(R.string.data_commit_des), getString(R.string.app_name));
        tvDataUploadDes.setText(des);

        switch (localUserInfo.getUserType()) {
            case LocalUserInfo.USER_TYPE_PERSONAL:
                tilyName.setHint(getString(R.string.perfect_type_personal));
                tvAccountDes.setText(R.string.perfect_des_other);
                break;
            case LocalUserInfo.USER_TYPE_INSTITUTION:
                tilyName.setHint(getString(R.string.perfect_name_institution));
                tvAccountDes.setText(R.string.perfect_des_institution);
                break;
        }

    }

    @Override
    public void setView() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 提交资料
     */
    @OnClick(R.id.btn_commit_data)
    public void commintData() {
        String name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(localUserInfo.getId())) {
            showToast(getString(R.string.tip_data_error));
            return;
        }
        if (TextUtils.isEmpty(name)) {
            showToast(getString(R.string.tip_resume_name_input));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", localUserInfo.getId());
        params.put("accessToken", localUserInfo.getAccessToken());
        params.put("name", name);
        params.put("dp", localUserInfo.getDp());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        LogUtil.i(TAG, "params=" + params);
        requestCall = RequestUtil.request(true, Constant.URL_USER_EDITINFO, params, 15, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String name = jsonObject.getString("name");
                        localUserInfo.setName(name);
                        SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                        EventBus.getDefault().post(new NameChangeEvent(name));
                        showToast(getString(R.string.change_name_suc));
                        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (requestCall != null) {
            requestCall.cancel();
        }
    }
}
