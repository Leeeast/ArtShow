package com.art.huakai.artshow.activity;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
        String accountName = edtName.getText().toString().trim();

        //TODO 修改成功后，通过eventBus更改各个页面数据
        EventBus.getDefault().post(new NameChangeEvent(accountName));
        Toast.makeText(this, "提交资料", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
