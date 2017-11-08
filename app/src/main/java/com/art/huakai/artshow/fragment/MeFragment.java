package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.LoginActivity;
import com.art.huakai.artshow.activity.SetActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.ActionTypeEvent;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.DeviceUtils;
import com.art.huakai.artshow.utils.LoginUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 我Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = MeFragment.class.getSimpleName();
    public final int CODE_STATUS_UNLOGIN = 0;
    public final int CODE_STATUS_PERSONAL = 3;
    public final int CODE_STATUS_INSTITUTION = 1;
    private TextView tvNmae, tvType;
    private SimpleDraweeView sdvAvatar;
    @BindView(R.id.nestedscrollview)
    NestedScrollView nestedScrollView;

    private int scrollDistance;
    private boolean disableScroll = false;

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
        RelativeLayout rLyHead = (RelativeLayout) rootView.findViewById(R.id.rly_head);
        rLyHead.setPadding(0, statusBarHeight, 0, 0);


        sdvAvatar = (SimpleDraweeView) rootView.findViewById(R.id.sdv_avatar);
        tvType = (TextView) rootView.findViewById(R.id.tv_type);
        tvNmae = (TextView) rootView.findViewById(R.id.tv_name);
        rootView.findViewById(R.id.iv_setting).setOnClickListener(this);
        tvNmae.setOnClickListener(this);
        sdvAvatar.setOnClickListener(this);
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
        //LocalUserInfo.getInstance().setUserType(1);
        try {
            if (LoginUtil.checkUserLogin(getContext(), false)) {
                tvNmae.setText(LocalUserInfo.getInstance().getName());
                sdvAvatar.setImageURI(LocalUserInfo.getInstance().getDp());
                switch (LocalUserInfo.getInstance().getUserType()) {
                    case LocalUserInfo.USER_TYPE_PERSONAL:
                        tvType.setText(getString(R.string.account_type_personal));
                        switchFragment(CODE_STATUS_PERSONAL);
                        break;
                    case LocalUserInfo.USER_TYPE_INSTITUTION:
                        tvType.setText(getString(R.string.account_type_institution));
                        switchFragment(CODE_STATUS_INSTITUTION);
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
            case CODE_STATUS_INSTITUTION:
                baseFragment = MeInstitutionFragment.newInstance();
                break;
            case CODE_STATUS_PERSONAL:
                baseFragment = MePersonalFragment.newInstance();
                break;
        }
        showFragment(baseFragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_avatar:
            case R.id.tv_name:
                if (TextUtils.isEmpty(LocalUserInfo.getInstance().getMobile()) ||
                        TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.iv_setting:
                if (LoginUtil.checkUserLogin(getContext(), true)) {
                    startActivity(new Intent(getContext(), SetActivity.class));
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (disableScroll) {
            disableScroll = false;
        } else {
            setView();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (nestedScrollView != null) {
                nestedScrollView.smoothScrollTo(0, scrollDistance);
            }
        } else {
            if (nestedScrollView != null) {
                scrollDistance = nestedScrollView.getScrollY();
            }
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeScrollStatus(ActionTypeEvent event) {
        if (event == null) {
            return;
        }
        disableScroll = event.isDisableScroll();
    }
}
