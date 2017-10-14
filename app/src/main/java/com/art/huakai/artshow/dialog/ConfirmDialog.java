package com.art.huakai.artshow.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class ConfirmDialog extends BaseDialogFragment implements View.OnClickListener {
    private static final String PARAMS_CONFIRM = "PARAMS_CONFIRM";
    private static final String PARAMS_CANCEL = "PARAMS_CANCEL";
    private CallBack mCallBack;
    private String mTxtConfirm;
    private String mTxtCancel;

    public static ConfirmDialog newInstence() {
        ConfirmDialog typeConfirmDialog = new ConfirmDialog();
        return typeConfirmDialog;
    }

    public static ConfirmDialog newInstence(String txtConfirm, String txtCancel) {
        ConfirmDialog typeConfirmDialog = new ConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_CONFIRM, txtConfirm);
        bundle.putString(PARAMS_CANCEL, txtCancel);
        typeConfirmDialog.setArguments(bundle);
        return typeConfirmDialog;
    }

    public interface CallBack {
        void onChoose(DialogFragment dialogFragment);

        void onCancel(DialogFragment dialogFragment);
    }

    public void setOnCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mTxtConfirm = bundle.getString(PARAMS_CONFIRM);
            mTxtCancel = bundle.getString(PARAMS_CANCEL);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void initView(View rootView) {
        Button btnTrueChoose = (Button) rootView.findViewById(R.id.btn_ture_choose);
        Button btnCancel = (Button) rootView.findViewById(R.id.btn_cancle);
        if (!TextUtils.isEmpty(mTxtConfirm)) {
            btnTrueChoose.setText(mTxtConfirm);
        }
        if (!TextUtils.isEmpty(mTxtCancel)) {
            btnCancel.setText(mTxtCancel);
        }
        btnCancel.setOnClickListener(this);
        btnTrueChoose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ture_choose:
                if (mCallBack != null) {
                    mCallBack.onChoose(this);
                }
                break;
            case R.id.btn_cancle:
                if (mCallBack != null) {
                    mCallBack.onCancel(this);
                }
                break;
        }
    }

    @Override
    public void setView() {

    }
}
