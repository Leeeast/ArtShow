package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectEditActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dataitem_base)
    DataItem dataItemBase;
    @BindView(R.id.dataitem_intro)
    DataItem dataItemIntro;

    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_project_edit;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
    }


    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_edit);


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
     * 基本资料
     */
    @OnClick(R.id.dataitem_base)
    public void jumpProjectDataBase() {
        invokActivity(this, ProjectBaseActivity.class, null, JumpCode.FLAG_REQ_PROJECT_BASE);
    }

    /**
     * 项目介绍
     */
    @OnClick(R.id.dataitem_intro)
    public void jumpProjectIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 演出介绍
     */
    @OnClick(R.id.dataitem_show_intro)
    public void jumpProjectShowIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 获奖记录
     */
    @OnClick(R.id.dataitem_award_record)
    public void jumpProjectAward() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_AWARD);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 技术要求
     */
    @OnClick(R.id.dataitem_tech_require)
    public void jumpProjectTech() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_TECH_REQUIRE);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 主创介绍
     */
    @OnClick(R.id.dataitem_creator_intro)
    public void jumpCreatorIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_CREATOR_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }
}
