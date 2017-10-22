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

public class ShareDialog extends BaseDialogFragment implements View.OnClickListener {
    private CallBack mCallBack;

    public static ShareDialog newInstence() {
        ShareDialog typeConfirmDialog = new ShareDialog();
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

        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_share;
    }

    @Override
    public void initView(View rootView) {
        Button btnCancel = (Button) rootView.findViewById(R.id.btn_cancle);

        btnCancel.setOnClickListener(this);
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
