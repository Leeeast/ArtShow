package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.TypeConfirmDialog;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class AccountTypeSelectFragment extends BaseFragment implements View.OnClickListener {

    private TypeConfirmDialog mTypeConfirmDialog;

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
                if (mTypeConfirmDialog == null) {
                    mTypeConfirmDialog = TypeConfirmDialog.newInstence();
                    mTypeConfirmDialog.setOnCallBack(new TypeConfirmDialog.CallBack() {
                        @Override
                        public void onChoose(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            Toast.makeText(AccountTypeSelectFragment.this.getContext(), "onChoose", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            Toast.makeText(AccountTypeSelectFragment.this.getContext(), "onCancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mTypeConfirmDialog.show(getFragmentManager(), "TYPECONFIRM.DIALOG");
                break;
        }
    }
}
