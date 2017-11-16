package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.DataUploadActivity;
import com.art.huakai.artshow.activity.ResumeEditActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.UserInfo;
import com.art.huakai.artshow.eventbus.PullToRefreshEvent;
import com.art.huakai.artshow.eventbus.RefreshResultEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.AuthStatusUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import okhttp3.Call;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class MePersonalFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_auth)
    TextView tvAuth;
    private RequestCall requestCall;

    public MePersonalFragment() {
        // Required empty public constructor
    }

    public static MePersonalFragment newInstance() {
        MePersonalFragment fragment = new MePersonalFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me_personal;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.rly_my_resume).setOnClickListener(this);
        rootView.findViewById(R.id.rly_pro_auth).setOnClickListener(this);
    }

    @Override
    public void setView() {
        String authDes = AuthStatusUtil.getAuthDes();
        tvAuth.setText(authDes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_my_resume:
                Bundle bundleParams = new Bundle();
                invokActivity(getContext(), ResumeEditActivity.class, null, JumpCode.FLAG_REQ_RESUME_MY);
                break;
            case R.id.rly_pro_auth:
                Bundle bundle = new Bundle();
                bundle.putString(DataUploadActivity.PARAMS_FROM, DataUploadFragment.FROM_ME);
                invokActivity(getContext(), DataUploadActivity.class, bundle, JumpCode.FLAG_REQ_DATA_UPLOAD);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestCall != null) {
            requestCall.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(PullToRefreshEvent event) {
        if (event == null) {
            return;
        }
        switch (event.getType()) {
            case RefreshResultEvent.RESULT_PULL_REFRESH:
                updateUserInfo();
                break;
            case RefreshResultEvent.RESULT_TYPE_LOADING_MORE:
                break;
        }
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        if (LoginUtil.checkUserLogin(getContext(), false)) {
            Map<String, String> params = new TreeMap<>();
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
            String sign = SignUtil.getSign(params);
            params.put("sign", sign);
            requestCall = RequestUtil.request(true, Constant.URL_USER_PREVIEW, params, 16, new RequestUtil.RequestListener() {
                @Override
                public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                    LogUtil.i(TAG, obj);
                    if (isSuccess) {
                        try {
                            UserInfo userInfo = GsonTools.parseData(obj, UserInfo.class);
                            LocalUserInfo localUserInfo = LocalUserInfo.getInstance();
                            localUserInfo.setId(userInfo.user.id);
                            localUserInfo.setName(userInfo.user.name);
                            localUserInfo.setMobile(userInfo.user.mobile);
                            localUserInfo.setEmail(userInfo.user.email);
                            localUserInfo.setWechatOpenid(userInfo.user.wechatOpenid);
                            localUserInfo.setDp(userInfo.user.dp);
                            localUserInfo.setPassword(userInfo.user.password);
                            localUserInfo.setUserType(userInfo.user.userType);
                            localUserInfo.setStatus(userInfo.user.status);
                            localUserInfo.setCreateTime(userInfo.user.createTime);
                            localUserInfo.setAuthenStatus(userInfo.authenStatus);
                            localUserInfo.setTalentCount(userInfo.talentCount);
                            localUserInfo.setTheterCount(userInfo.theterCount);
                            localUserInfo.setRepertoryCount(userInfo.repertoryCount);
                            SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                            setView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ResponseCodeCheck.showErrorMsg(code);
                    }
                    EventBus.getDefault().post(new RefreshResultEvent(RefreshResultEvent.RESULT_PULL_REFRESH));
                }

                @Override
                public void onFailed(Call call, Exception e, int id) {
                    LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                    EventBus.getDefault().post(new RefreshResultEvent(RefreshResultEvent.RESULT_PULL_REFRESH));
                }
            });
        }
    }
}
