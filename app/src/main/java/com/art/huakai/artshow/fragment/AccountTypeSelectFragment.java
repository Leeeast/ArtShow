package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.TypeConfirmDialog;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class AccountTypeSelectFragment extends BaseFragment implements View.OnClickListener {
    public AccountTypeSelectFragment() {
        // Required empty public constructor
    }

    public static AccountTypeSelectFragment newInstance() {
        AccountTypeSelectFragment fragment = new AccountTypeSelectFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_account_type_select;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_next_step).setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                TypeConfirmDialog typeConfirmDialog = new TypeConfirmDialog();
                typeConfirmDialog.show(getFragmentManager(), "TYPECONFIRM.DIALOG");
                break;
        }
    }
}
