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

public class MemberAddDialog extends BaseDialogFragment {

    public static MemberAddDialog newInstence() {
        MemberAddDialog typeConfirmDialog = new MemberAddDialog();
        return typeConfirmDialog;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_member_add;
    }

    @Override
    public void initView(View rootView) {
    }


    @Override
    public void setView() {

    }
}
