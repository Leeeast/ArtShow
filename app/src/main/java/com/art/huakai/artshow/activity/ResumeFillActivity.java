package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.fragment.FillIntroduceFragment;
import com.art.huakai.artshow.fragment.FillWorksFragment;
import com.art.huakai.artshow.fragment.TalentHonourFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

/**
 * 简历填写Activity
 * Created by lidongliang on 2017/10/14.
 */

public class ResumeFillActivity extends BaseActivity {
    public static final String PARAMS_ACTION = "PARAMS_ACTION";
    public static final int CODE_ACTION_FILL_INTRODUCE = 0;
    public static final int CODE_ACTION_FILL_AWARD = 1;
    public static final int CODE_ACTION_FILL_WORKS = 3;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_resume_fill;
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
            case CODE_ACTION_FILL_INTRODUCE:
                baseFragment = FillIntroduceFragment.newInstance();
                break;
            case CODE_ACTION_FILL_AWARD:
                baseFragment = TalentHonourFragment.newInstance();
                break;
            case CODE_ACTION_FILL_WORKS:
                baseFragment = FillWorksFragment.newInstance();
                break;
            default:
                baseFragment = FillIntroduceFragment.newInstance();
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
