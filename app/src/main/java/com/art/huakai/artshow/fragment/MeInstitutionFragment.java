package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.DataUploadActivity;
import com.art.huakai.artshow.activity.ProjectActivity;
import com.art.huakai.artshow.activity.ResumeActivity;
import com.art.huakai.artshow.activity.TheatreActivity;
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
import com.art.huakai.artshow.widget.SettingItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class MeInstitutionFragment extends BaseFragment implements View.OnClickListener {

    private SettingItem itemAuth;
    private SettingItem itemMyProject;
    private SettingItem itemMyTheatre;
    private SettingItem itemMyTalent;
    private LocalUserInfo localUserInfo;
    private RequestCall requestCall;

    public MeInstitutionFragment() {
        // Required empty public constructor
    }

    public static MeInstitutionFragment newInstance() {
        MeInstitutionFragment fragment = new MeInstitutionFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        EventBus.getDefault().register(this);
        localUserInfo = LocalUserInfo.getInstance();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_me_institution;
    }

    @Override
    public void initView(View rootView) {
        itemAuth = (SettingItem) rootView.findViewById(R.id.item_institution_auth);
        itemMyProject = (SettingItem) rootView.findViewById(R.id.item_my_project);
        itemMyTheatre = (SettingItem) rootView.findViewById(R.id.item_my_theatre);
        itemMyTalent = (SettingItem) rootView.findViewById(R.id.item_my_talent);

        itemAuth.setOnClickListener(this);
        itemMyProject.setOnClickListener(this);
        itemMyTheatre.setOnClickListener(this);
        itemMyTalent.setOnClickListener(this);
    }

    @Override
    public void setView() {
        String talentDes = localUserInfo.getTalentCount() == 0 ?
                getString(R.string.institution_upload_talent) :
                String.valueOf(localUserInfo.getTalentCount());
        itemMyTalent.setDesText(talentDes);

        String theatreDes = localUserInfo.getTheterCount() == 0 ?
                getString(R.string.institution_upload_theatre) :
                String.valueOf(localUserInfo.getTheterCount());
        itemMyTheatre.setDesText(theatreDes);

        String projectDes = localUserInfo.getRepertoryCount() == 0 ?
                getString(R.string.institution_upload_project) :
                String.valueOf(localUserInfo.getRepertoryCount());
        itemMyProject.setDesText(projectDes);

        String authDes = AuthStatusUtil.getAuthDes();
        itemAuth.setDesText(authDes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_institution_auth:
                Bundle bundle = new Bundle();
                bundle.putString(DataUploadActivity.PARAMS_FROM, DataUploadFragment.FROM_ME);
                invokActivity(getContext(), DataUploadActivity.class, bundle, JumpCode.FLAG_REQ_DATA_UPLOAD);
                break;
            case R.id.item_my_project:
                invokActivity(getContext(), ProjectActivity.class, null, JumpCode.FLAG_REQ_PROJECT_MY);
                break;
            case R.id.item_my_theatre:
                invokActivity(getContext(), TheatreActivity.class, null, JumpCode.FLAG_REQ_THEATRE_MY);
                break;
            case R.id.item_my_talent:
                invokActivity(getContext(), ResumeActivity.class, null, JumpCode.FLAG_REQ_TALENT_MY);
                break;
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
