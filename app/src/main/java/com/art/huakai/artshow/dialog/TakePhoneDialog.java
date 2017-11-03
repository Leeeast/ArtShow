package com.art.huakai.artshow.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.config.PhotoConfig;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class TakePhoneDialog extends BaseDialogFragment implements View.OnClickListener {
    private static final String PARAMS_LINKMAN = "PARAMS_LINKMAN";
    private static final String PARAMS_LINKPHONE = "PARAMS_LINKPHONE";
    private CallBack mCallBack;
    private String mLinkMan, mLinkPhone;
    private TextView tvLinkMan;
    private TextView tvLinkPhone;

    public static TakePhoneDialog newInstence(String linkName, String linkPhone) {
        TakePhoneDialog typeConfirmDialog = new TakePhoneDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_LINKMAN, linkName);
        bundle.putString(PARAMS_LINKPHONE, linkPhone);
        typeConfirmDialog.setArguments(bundle);
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
            mLinkMan = bundle.getString(PARAMS_LINKMAN);
            mLinkPhone = bundle.getString(PARAMS_LINKPHONE);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_take_phone;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_cancle).setOnClickListener(this);
        rootView.findViewById(R.id.lly_type_select).setOnClickListener(this);

        tvLinkMan = (TextView) rootView.findViewById(R.id.tv_linkman);
        tvLinkPhone = (TextView) rootView.findViewById(R.id.tv_linkphone);
        if (!TextUtils.isEmpty(mLinkMan)) {
            tvLinkMan.setText(mLinkMan);
        }
        if (!TextUtils.isEmpty(mLinkPhone)) {
            tvLinkPhone.setText(mLinkPhone);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                this.dismiss();
                break;
            case R.id.lly_type_select:
                callPhone();
                break;
        }
    }

    /**
     * 打电话
     */
    private void callPhone() {
        try {
            Uri uri = Uri.parse("tel:" + mLinkPhone);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setView() {
    }
}
