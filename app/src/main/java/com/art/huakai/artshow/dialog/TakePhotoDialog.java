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

public class TakePhotoDialog extends BaseDialogFragment implements View.OnClickListener {
    private CallBack mCallBack;

    public static TakePhotoDialog newInstence() {
        TakePhotoDialog typeConfirmDialog = new TakePhotoDialog();
        return typeConfirmDialog;
    }


    public interface CallBack {
        void onTakePhoto(DialogFragment dialogFragment);

        void onAlbuml(DialogFragment dialogFragment);
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
        return R.layout.dialog_take_photo;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        rootView.findViewById(R.id.btn_photo_album).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (mCallBack != null) {
                    mCallBack.onTakePhoto(this);
                }
                break;
            case R.id.btn_photo_album:
                if (mCallBack != null) {
                    mCallBack.onAlbuml(this);
                }
                break;
            case R.id.btn_cancle:
                this.dismiss();
                break;
        }
    }

    @Override
    public void setView() {

    }
}
