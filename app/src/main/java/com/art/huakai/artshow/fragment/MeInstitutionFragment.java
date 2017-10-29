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
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.widget.SettingItem;


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

    public MeInstitutionFragment() {
        // Required empty public constructor
    }

    public static MeInstitutionFragment newInstance() {
        MeInstitutionFragment fragment = new MeInstitutionFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_institution_auth:
                //if(LocalUserInfo.getInstance().getAuthenStatus()==)
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
}
