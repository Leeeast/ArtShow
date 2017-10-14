package com.art.huakai.artshow.activity;

import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountNameActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    private Unbinder mUnbinder;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_account_name;
    }

    @Override
    public void initData() {
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_name);

        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText(R.string.account_name_change);
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

    @OnClick(R.id.tv_subtitle)
    public void changeAccountName() {
        invokActivity(this, AccountNameChangeActivity.class, null, JumpCode.FLAG_REQ_ACCOUNT_NAME_CHANGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(NameChangeEvent nameChangeEvent) {
        if (nameChangeEvent == null) {
            return;
        }
        if (tvAccountName != null) {
            tvAccountName.setText(nameChangeEvent.getAccountName());
        }
    }
}
