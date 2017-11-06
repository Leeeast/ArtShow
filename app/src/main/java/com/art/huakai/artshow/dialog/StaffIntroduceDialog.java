package com.art.huakai.artshow.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.entity.Staff;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class StaffIntroduceDialog extends BaseDialogFragment {

    public static final String PARAMS_STAFF = "PARAMS_STAFF";
    public static final String PARAMS_POSITION = "PARAMS_POSITION";
    @BindView(R.id.lly_close)
    LinearLayout llyClose;
    @BindView(R.id.sdv_member)
    SimpleDraweeView sdvMember;
    @BindView(R.id.tv_actor_name)
    TextView tvActorName;
    @BindView(R.id.tv_actor_role)
    TextView tvActorRole;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    Unbinder unbinder;

    private Staff mStaff;


    public static StaffIntroduceDialog newInstence() {
        StaffIntroduceDialog typeConfirmDialog = new StaffIntroduceDialog();
        return typeConfirmDialog;
    }

    public static StaffIntroduceDialog newInstence(Staff staff) {
        StaffIntroduceDialog typeConfirmDialog = new StaffIntroduceDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_STAFF, staff);
        typeConfirmDialog.setArguments(bundle);
        return typeConfirmDialog;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mStaff = (Staff) bundle.getSerializable(PARAMS_STAFF);
        } else {
            mStaff = new Staff();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_staff_introduce;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {
      if(mStaff!=null&& !TextUtils.isEmpty(mStaff.getPhoto())){
          sdvMember.setImageURI(Uri.parse(mStaff.getPhoto()));
      }
        if(mStaff!=null){
            tvActorName.setText(mStaff.getName());
            tvActorRole.setText(mStaff.getRoleName());
            tvIntroduce.setText(mStaff.getDescpt());
        }


    }

    @OnClick(R.id.lly_close)
    public void closeDialog() {
        this.dismiss();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
