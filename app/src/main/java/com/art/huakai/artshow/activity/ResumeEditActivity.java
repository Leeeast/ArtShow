package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TakePhotoDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TalentBean;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.eventbus.TalentInfoChangeEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 简历Activity
 * Created by lidongliang on 2017/10/14.
 */

public class ResumeEditActivity extends BaseActivity {
    public static final String PARAMS_NEW = "PARAMS_NEW";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_data_base)
    TextView tvDataBase;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_picture)
    TextView tvPicture;
    @BindView(R.id.tv_awards)
    TextView tvAwards;
    @BindView(R.id.switch_talent_release)
    Switch switchTalentRelease;

    private TakePhotoDialog takePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ShowProgressDialog showProgressDialog;
    private TalentDetailInfo talentInfo;
    private boolean mIsNewCreate;
    private RequestCall requestCallList;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_resume_edit;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        talentInfo = TalentDetailInfo.getInstance();
        Bundle extras = getIntent().getExtras();
        if (LocalUserInfo.getInstance().getUserType() == LocalUserInfo.USER_TYPE_PERSONAL) {
            loadTalentData();
        } else {
            if (extras != null) {
                mIsNewCreate = extras.getBoolean(PARAMS_NEW, true);
            }
            if (mIsNewCreate) {
                initTalentDetailInfo();
            } else {
                getTalentDetailInfo(TalentDetailInfo.getInstance().getId());
            }
        }
    }

    /**
     * 加人才简历列表数据
     */
    private void loadTalentData() {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("page", "1");
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        requestCallList = RequestUtil.request(true, Constant.URL_USER_TALENT, params, 60, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        List<TalentBean> talents = GsonTools.parseDatas(obj, TalentBean.class);
                        LogUtil.i(TAG, "talents.size = " + talents.size());
                        if (talents.size() > 0) {
                            String talentId = talents.get(0).getId();
                            getTalentDetailInfo(talentId);
                        } else {
                            initTalentDetailInfo();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.resume_my);
    }

    @Override
    public void setView() {

    }

    /**
     * 通知页面信息更新
     */
    public void updateEditUI() {
        if (!TextUtils.isEmpty(talentInfo.getName())) {
            tvDataBase.setText(getString(R.string.app_has_filled));
        }
        if (!TextUtils.isEmpty(talentInfo.getDescription())) {
            tvIntro.setText(getString(R.string.app_has_filled));
        }
        if (!TextUtils.isEmpty(talentInfo.getWorksDescpt())) {
            tvProject.setText(getString(R.string.app_has_filled));
        }
        if (talentInfo.getPictures() != null && talentInfo.getPictures().size() > 0) {
            tvPicture.setText(getString(R.string.app_has_filled));
        }
        if (!TextUtils.isEmpty(talentInfo.getAwardsDescpt())) {
            tvAwards.setText(getString(R.string.app_has_filled));
        }
        switchTalentRelease.setChecked(talentInfo.getStatus() == 1);
        sdvAvatar.setImageURI(talentInfo.getLogo());
    }

    /**
     * 回复简历详情初始值
     */
    public void initTalentDetailInfo() {
        talentInfo.setId(null);
        talentInfo.setLogo(null);
        talentInfo.setName(null);
        talentInfo.setBirthday(null);
        talentInfo.setLinkTel(null);
        talentInfo.setRegionId(null);
        talentInfo.setDescription(null);
        talentInfo.setHeight(null);
        talentInfo.setWeight(null);
        talentInfo.setSchool(null);
        talentInfo.setAgency(null);
        talentInfo.setWorksDescpt(null);
        talentInfo.setAwardsDescpt(null);
        talentInfo.setStatus(0);
        talentInfo.setUserId(null);
        talentInfo.setCreateTime(0);
        talentInfo.setUpdateTime(0);
        talentInfo.setPictures(null);
        talentInfo.setClassifyIds(null);
        talentInfo.setClassifyNames(null);
        talentInfo.setRegionName(null);
        talentInfo.setAge(0);
        talentInfo.setViewTimes(0);
        talentInfo.setAuthentication(null);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 个人介绍
     */
    @OnClick(R.id.rly_self_introduce)
    public void jumpIntroduction() {
        Bundle bundle = new Bundle();
        bundle.putInt(ResumeFillActivity.PARAMS_ACTION, ResumeFillActivity.CODE_ACTION_FILL_INTRODUCE);
        invokActivity(this, ResumeFillActivity.class, bundle, JumpCode.FLAG_REQ_RESUME_FILL);
    }

    /**
     * 获奖经历
     */
    @OnClick(R.id.rly_award_exper)
    public void jumpAwardExper() {
        Bundle bundle = new Bundle();
        bundle.putInt(ResumeFillActivity.PARAMS_ACTION, ResumeFillActivity.CODE_ACTION_FILL_AWARD);
        invokActivity(this, ResumeFillActivity.class, bundle, JumpCode.FLAG_REQ_RESUME_FILL);
    }

    /**
     * 或将经历
     */
    @OnClick(R.id.rly_self_works)
    public void jumpSelfWorks() {
        Bundle bundle = new Bundle();
        bundle.putInt(ResumeFillActivity.PARAMS_ACTION, ResumeFillActivity.CODE_ACTION_FILL_WORKS);
        invokActivity(this, ResumeFillActivity.class, bundle, JumpCode.FLAG_REQ_RESUME_FILL);
    }

    /**
     * 相片
     */
    @OnClick(R.id.rly_photo)
    public void jumpBasePhotoUpload() {
        Bundle bundle = new Bundle();
        bundle.putInt(ResumeFillActivity.PARAMS_ACTION, ResumeFillActivity.CODE_ACTION_UPLOAD_PHOTO);
        invokActivity(this, ResumeFillActivity.class, bundle, JumpCode.FLAG_REQ_BASE_DATA);
    }

    /**
     * 修改封面
     */
    @OnClick(R.id.sdv_avatar)
    public void selectPhoto() {
        if (takePhotoDialog == null) {
            takePhotoDialog = TakePhotoDialog.newInstence();
            takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                @Override
                public void onTakePhoto(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.takePhoto(ResumeEditActivity.this, selectList);
                }

                @Override
                public void onAlbuml(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.photoAlbum(ResumeEditActivity.this, selectList);
                }
            });
        }
        takePhotoDialog.show(getSupportFragmentManager(), "TAKEPHOTO.DIALOG");
    }

    /**
     * 基本信息
     */
    @OnClick(R.id.rly_base_data)
    public void jumpBaseDataActivity() {
        invokActivity(this, ResumeBaseActivity.class, null, JumpCode.FLAG_REQ_BASE_DATA);
    }

    @OnClick(R.id.view_talent_release)
    public void talentRelase() {
        if (switchTalentRelease.isChecked()) {
            talentOffline();
        } else {
            talentRelease();
        }
    }

    /**
     * 下线简历
     */
    public void talentOffline() {
        if (TextUtils.isEmpty(talentInfo.getId())) {
            showToast(getString(R.string.tip_talent_release));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", talentInfo.getInstance().getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_OFFLINE, params, 90, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_resume_offline_suc));
                    talentInfo.setStatus(0);
                    switchTalentRelease.setChecked(false);
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showToast(getString(R.string.tip_resume_offline_fail));
            }
        });
    }

    /**
     * 发布简历
     */
    public void talentRelease() {
        if (TextUtils.isEmpty(talentInfo.getId())) {
            showToast(getString(R.string.tip_talent_release));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", talentInfo.getInstance().getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_RELEASE, params, 89, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_resume_release_suc));
                    talentInfo.setStatus(1);
                    switchTalentRelease.setChecked(true);
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showToast(getString(R.string.tip_resume_release_fail));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    if (selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        int mimeType = localMedia.getMimeType();
                        String path = "";
                        if (localMedia.isCut() && !localMedia.isCompressed()) {
                            // 裁剪过
                            path = localMedia.getCutPath();
                        } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            path = localMedia.getCompressPath();
                        } else {
                            // 原图
                            path = localMedia.getPath();
                        }
                        uploadPhoto(path);
                    }
                    break;
            }
        }
    }

    /**
     * 上传图片
     *
     * @param path
     */
    public void uploadPhoto(String path) {
        LogUtil.i(TAG, "path = " + path);
        sdvAvatar.setBackground(null);
        sdvAvatar.setImageURI("file:///" + path);
        showProgressDialog.show();
        RequestUtil.uploadLoadFile(Constant.URL_UPLOAD_FILE, path, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String avatarUrl = jsonObject.getString("url");
                        changeTalentLogo(avatarUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 上传简历头像
     *
     * @param talentLogoUrl
     */
    private void changeTalentLogo(final String talentLogoUrl) {

        Map<String, String> params = new TreeMap<>();
        if (TalentDetailInfo.getInstance().getId() != null) {
            params.put("id", TalentDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("logo", talentLogoUrl);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params :" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_EDIT_LOGO, params, 67, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.talent_logo_upload_suc));
                        //{"id":"8a999cce5f5da93b015f5f338d0a0020"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String theatreId = jsonObject.getString("id");
                        talentInfo.setId(theatreId);
                        talentInfo.setLogo(talentLogoUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                showToast(getString(R.string.talent_logo_upload_fail));
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });

    }

    /**
     * 获取个人简历详情
     */
    public void getTalentDetailInfo(String talentId) {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(this, true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(talentId)) {
            showToast(getString(R.string.tip_data_error));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("id", talentId);
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_USER_TALENT_DETAIL, params, 53, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        TalentDetailInfo resume = GsonTools.parseData(obj, TalentDetailInfo.class);
                        talentInfo.setId(resume.getId());
                        talentInfo.setLogo(resume.getLogo());
                        talentInfo.setName(resume.getName());
                        talentInfo.setBirthday(resume.getBirthday());
                        talentInfo.setLinkTel(resume.getLinkTel());
                        talentInfo.setRegionId(resume.getRegionId());
                        talentInfo.setDescription(resume.getDescription());
                        talentInfo.setHeight(resume.getHeight());
                        talentInfo.setWeight(resume.getWeight());
                        talentInfo.setSchool(resume.getSchool());
                        talentInfo.setAgency(resume.getAgency());
                        talentInfo.setWorksDescpt(resume.getWorksDescpt());
                        talentInfo.setAwardsDescpt(resume.getAwardsDescpt());
                        talentInfo.setStatus(resume.getStatus());
                        talentInfo.setUserId(resume.getUserId());
                        talentInfo.setCreateTime(resume.getCreateTime());
                        talentInfo.setUpdateTime(resume.getUpdateTime());
                        talentInfo.setPictures(resume.getPictures());
                        talentInfo.setClassifyIds(resume.getClassifyIds());
                        talentInfo.setClassifyNames(resume.getClassifyNames());
                        talentInfo.setRegionName(resume.getRegionName());
                        talentInfo.setAge(resume.getAge());
                        talentInfo.setViewTimes(resume.getViewTimes());
                        talentInfo.setAuthentication(resume.getAuthentication());
                        updateEditUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(TalentInfoChangeEvent event) {
        if (event == null) {
            return;
        }
        updateEditUI();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestCallList != null) {
            requestCallList.cancel();
        }
    }
}
