package com.art.huakai.artshow.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;

/**
 * Created by lidongliang on 2017/10/1.
 */

public class TypeConfirmDialog extends BaseDialogFragment implements View.OnClickListener {
    private CallBack mCallBack;


    public static TypeConfirmDialog newInstence() {
        TypeConfirmDialog typeConfirmDialog = new TypeConfirmDialog();
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

    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_type_confirm;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_cancle).setOnClickListener(this);
        rootView.findViewById(R.id.btn_ture_choose).setOnClickListener(this);
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
