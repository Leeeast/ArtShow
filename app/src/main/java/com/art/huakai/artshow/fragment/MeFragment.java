package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.LoginActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LoginUtil;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 我Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = MeFragment.class.getSimpleName();
    public final int CODE_STATUS_UNLOGIN = 0;
    public final int CODE_STATUS_PERSONAL = 3;
    public final int CODE_STATUS_PUBLISHER = 2;
    public final int CODE_STATUS_THEATRE = 1;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView tvNmae, tvType;
    private SimpleDraweeView sdvAvatar;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView(View rootView) {
        int statusBarHeight = DeviceUtils.getStatusBarHeight(getContext());
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        RelativeLayout rLyHead = (RelativeLayout) rootView.findViewById(R.id.rly_head);
        rLyHead.setPadding(0, statusBarHeight, 0, 0);
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_barlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mRefreshLayout.setEnabled(true);
                } else {
                    mRefreshLayout.setEnabled(false);
                }
            }
        });
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        if (false) {
            //设置不滑动
            layoutParams.setScrollFlags(0);
        } else {
            //设置滑动
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        }
        toolbarLayout.setLayoutParams(layoutParams);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        sdvAvatar = (SimpleDraweeView) rootView.findViewById(R.id.sdv_avatar);
        tvType = (TextView) rootView.findViewById(R.id.tv_type);
        tvNmae = (TextView) rootView.findViewById(R.id.tv_name);
        tvNmae.setOnClickListener(this);
    }

    /**
     * 显示Fragment
     *
     * @param baseFragment
     */
    private void showFragment(BaseFragment baseFragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.fly_content, baseFragment, baseFragment.getTAG()).commit();
    }

    @Override
    public void setView() {
        switchFragment(0);
        try {
            if (LoginUtil.checkUserLogin(getContext(), false)) {
                tvNmae.setText(LocalUserInfo.getInstance().getName());
                sdvAvatar.setImageURI(LocalUserInfo.getInstance().getDp());
                switch (LocalUserInfo.getInstance().getUserType()) {
                    case LocalUserInfo.USER_TYPE_PERSONAL:
                        tvType.setText(getString(R.string.account_type_personal));
                        break;
                    case LocalUserInfo.USER_TYPE_PUBLISHER:
                        tvType.setText(getString(R.string.account_type_publisher));
                        break;
                    case LocalUserInfo.USER_TYPE_THEATRE:
                        tvType.setText(getString(R.string.account_type_theatre));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchFragment(int type) {
        BaseFragment baseFragment = null;
        switch (type) {
            case CODE_STATUS_UNLOGIN:
                baseFragment = MeUnloginFragment.newInstance();
                break;
            case CODE_STATUS_THEATRE:
                break;
            case CODE_STATUS_PUBLISHER:
                break;
            case CODE_STATUS_PERSONAL:
                break;
        }
        showFragment(baseFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_name:
                if (TextUtils.isEmpty(LocalUserInfo.getInstance().getMobile()) ||
                        TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            //没有隐藏的时候
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}
