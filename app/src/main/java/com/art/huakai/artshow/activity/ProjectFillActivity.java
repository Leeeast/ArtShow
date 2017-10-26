package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.fragment.PhotoUploadFragment;
import com.art.huakai.artshow.fragment.ProjectAwardFragment;
import com.art.huakai.artshow.fragment.ProjectCreatorFragment;
import com.art.huakai.artshow.fragment.ProjectEnableShowTimeFragment;
import com.art.huakai.artshow.fragment.ProjectIntroFragment;
import com.art.huakai.artshow.fragment.ProjectShowIntroFragment;
import com.art.huakai.artshow.fragment.ProjectTechFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

/**
 * 简历填写Activity
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectFillActivity extends BaseActivity {
    public static final String PARAMS_ACTION = "PARAMS_ACTION";
    public static final int CODE_ACTION_PROJECT_INTRO = 0;
    public static final int CODE_ACTION_PROJECT_DETAIL_INTRO = 1;
    public static final int CODE_ACTION_PROJECT_AWARD = 2;
    public static final int CODE_ACTION_PROJECT_TECH_REQUIRE = 3;
    public static final int CODE_ACTION_PROJECT_ENABLE_SHOW_TIME = 4;
    public static final int CODE_ACTION_PROJECT_CREATOR_INTRO = 5;
    public static final int CODE_ACTION_PROJECT_PHOTO_UPLOAD = 6;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_project_fill;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            int action = params.getInt(PARAMS_ACTION, 0);
            showFragment(action);
        }
    }

    /**
     * 根据action现实相应的Fragment
     *
     * @param action
     */
    private void showFragment(int action) {
        BaseFragment baseFragment = null;
        switch (action) {
            case CODE_ACTION_PROJECT_INTRO:
                baseFragment = ProjectIntroFragment.newInstance();
                break;
            case CODE_ACTION_PROJECT_DETAIL_INTRO:
                baseFragment = ProjectShowIntroFragment.newInstance();
                break;
            case CODE_ACTION_PROJECT_AWARD:
                baseFragment = ProjectAwardFragment.newInstance();
                break;
            case CODE_ACTION_PROJECT_TECH_REQUIRE:
                baseFragment = ProjectTechFragment.newInstance();
                break;
            case CODE_ACTION_PROJECT_ENABLE_SHOW_TIME:
                baseFragment = ProjectEnableShowTimeFragment.newInstance();
            case CODE_ACTION_PROJECT_CREATOR_INTRO:
                baseFragment = ProjectCreatorFragment.newInstance();
                break;
            case CODE_ACTION_PROJECT_PHOTO_UPLOAD:
                baseFragment = PhotoUploadFragment.newInstance(PhotoUploadFragment.TYPE_PROJECT);
                break;
            default:
                baseFragment = ProjectIntroFragment.newInstance();
                break;
        }
        initFragment(baseFragment);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {

    }

    //显示fragment
    public void initFragment(BaseFragment baseFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fly_resume_content, baseFragment, baseFragment.getTAG()).commit();
    }


}
