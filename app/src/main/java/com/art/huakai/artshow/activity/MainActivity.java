package com.art.huakai.artshow.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.fragment.CollaborateFragment;
import com.art.huakai.artshow.fragment.DiscoverFragment;
import com.art.huakai.artshow.fragment.MeFragment;
import com.art.huakai.artshow.fragment.ShowCircleFragment;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

/**
 * 主界面
 * Created by lidongliang on 2017/9/27.
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    private String[] fragmentTags = new String[]{ShowCircleFragment.TAG_FRAGMENT,
            DiscoverFragment.TAG_FRAGMENT, CollaborateFragment.TAG_FRAGMENT, MeFragment.TAG_FRAGMENT};

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.redd3);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void initView() {
        ((RadioGroup) findViewById(R.id.rdogp_main_tab)).setOnCheckedChangeListener(this);
    }

    @Override
    public void setView() {
        showFragment(ShowCircleFragment.TAG_FRAGMENT);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rdobtn_show_circle:
                showFragment(ShowCircleFragment.TAG_FRAGMENT);
                break;
            case R.id.rdobtn_discover:
                showFragment(DiscoverFragment.TAG_FRAGMENT);
                break;
            case R.id.rdobtn_collaborate:
                showFragment(CollaborateFragment.TAG_FRAGMENT);
                break;
            case R.id.rdobtn_me:
                showFragment(MeFragment.TAG_FRAGMENT);
                break;
        }
    }

    /**
     * 显示Fragment
     *
     * @param tag
     */
    protected void showFragment(String tag) {
        ft = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragment = addFragment(tag);
        }
        if (!fragment.isAdded()) {
            ft.add(R.id.fly_content, fragment, tag);
        } else {
            ft.show(fragment);
            // ft.addToBackStack(tag);
        }
        hideOtherFragment(tag, ft);
        ft.commitAllowingStateLoss();
    }

    /**
     * 添加Fragment
     *
     * @param tag
     * @return
     */
    private Fragment addFragment(String tag) {
        Fragment fragment = null;
        if (tag.equals(ShowCircleFragment.TAG_FRAGMENT)) {
            fragment = ShowCircleFragment.newInstance();
        } else if (tag.equals(DiscoverFragment.TAG_FRAGMENT)) {
            fragment = DiscoverFragment.newInstance();
        } else if (tag.equals(CollaborateFragment.TAG_FRAGMENT)) {
            fragment = CollaborateFragment.newInstance();
        } else if (tag.equals(MeFragment.TAG_FRAGMENT)) {
            fragment = MeFragment.newInstance();
        }
        return fragment;
    }

    /**
     * 隐藏别的Fragment
     *
     * @param tag
     * @param ft
     */
    protected void hideOtherFragment(String tag, FragmentTransaction ft) {
        for (String t : fragmentTags) {
            if (!t.equals(tag)) {
                Fragment otherFragment = fragmentManager.findFragmentByTag(t);
                if (otherFragment != null) {
                    ft.hide(otherFragment);
                }
            }
        }
    }
}
