package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.LoginActivity;
import com.art.huakai.artshow.activity.SetActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LoginUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fabAddProduction;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        EventBus.getDefault().register(this);
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

        fabAddProduction = (FloatingActionButton) rootView.findViewById(R.id.fab_add_production);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_barlayout);
        toolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);

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
        rootView.findViewById(R.id.iv_setting).setOnClickListener(this);
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
        LocalUserInfo.getInstance().setUserType(1);
        try {
            if (LoginUtil.checkUserLogin(getContext(), false)) {
                tvNmae.setText(LocalUserInfo.getInstance().getName());
                sdvAvatar.setImageURI(LocalUserInfo.getInstance().getDp());
                switch (LocalUserInfo.getInstance().getUserType()) {
                    case LocalUserInfo.USER_TYPE_PERSONAL:
                        tvType.setText(getString(R.string.account_type_personal));
                        switchFragment(CODE_STATUS_PERSONAL);
                        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
                        //设置不滑动
                        layoutParams.setScrollFlags(0);
                        toolbarLayout.setLayoutParams(layoutParams);
                        mRefreshLayout.setEnabled(false);
                        break;
                    case LocalUserInfo.USER_TYPE_PUBLISHER:
                        tvType.setText(getString(R.string.account_type_publisher));
                        switchFragment(CODE_STATUS_PUBLISHER);
                        setSRLayout();

                        break;
                    case LocalUserInfo.USER_TYPE_THEATRE:
                        tvType.setText(getString(R.string.account_type_theatre));
                        switchFragment(CODE_STATUS_THEATRE);
                        setSRLayout();
                        break;
                }
            } else {
                switchFragment(CODE_STATUS_UNLOGIN);
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
            case CODE_STATUS_PUBLISHER:
                baseFragment = MeTheatreFragment.newInstance();
                break;
            case CODE_STATUS_PERSONAL:
                baseFragment = MePersonalFragment.newInstance();
                break;
        }
        showFragment(baseFragment);
    }

    //现实添加按钮，添加滑动
    public void setSRLayout() {
        fabAddProduction.setVisibility(View.VISIBLE);
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

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        //设置滑动
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
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
            case R.id.iv_setting:
                startActivity(new Intent(getContext(), SetActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(NameChangeEvent nameChangeEvent) {
        if (nameChangeEvent == null) {
            return;
        }
        if (tvNmae != null) {
            tvNmae.setText(nameChangeEvent.getAccountName());
        }
    }
}
