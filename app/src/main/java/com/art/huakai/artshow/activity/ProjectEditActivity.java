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
import com.art.huakai.artshow.entity.PicturesBean;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;
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
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectEditActivity extends BaseActivity {
    public static final String PARAMS_NEW = "PARAMS_NEW";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dataitem_base)
    DataItem dataItemBase;
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.dataitem_intro)
    DataItem dataItemIntro;
    @BindView(R.id.dataitem_show_intro)
    DataItem dataItemShowIntro;
    @BindView(R.id.dataitem_award_record)
    DataItem dataItemAwardRecord;
    @BindView(R.id.dataitem_tech_require)
    DataItem dataItemTechRequire;
    @BindView(R.id.dataitem_photo)
    DataItem dataItemPhoto;
    @BindView(R.id.dataitem_creator_intro)
    DataItem dataItemCreatorIntro;
    @BindView(R.id.switch_release)
    Switch switchRelease;

    private ShowProgressDialog showProgressDialog;
    private TakePhotoDialog takePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean mIsNewCreate;
    private ProjectDetailInfo projectDetailInfo;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_project_edit;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        projectDetailInfo = ProjectDetailInfo.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mIsNewCreate = extras.getBoolean(PARAMS_NEW, true);
        }
        if (mIsNewCreate) {
            initProjectDetailInfo();
        } else {
            getProjectDetailInfo(ProjectDetailInfo.getInstance().getId());
        }
    }

    /**
     * 获取项目详情
     *
     * @param projectId
     */
    private void getProjectDetailInfo(String projectId) {
        Map<String, String> params = new TreeMap<>();
        params.put("id", projectId);
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        RequestUtil.request(true, Constant.URL_USER_REPERTORY_DETAIL, params, 72, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        //初始化剧场单例类
                        ProjectDetailInfo p = GsonTools.parseData(obj, ProjectDetailInfo.class);
                        projectDetailInfo.setId(p.getId());
                        projectDetailInfo.setLogo(p.getLogo());
                        projectDetailInfo.setTitle(p.getTitle());
                        projectDetailInfo.setClassifyId(p.getClassifyId());
                        projectDetailInfo.setLinkman(p.getLinkman());
                        projectDetailInfo.setLinkTel(p.getLinkTel());
                        projectDetailInfo.setPeopleNum(p.getPeopleNum());
                        projectDetailInfo.setExpense(p.getExpense());
                        projectDetailInfo.setRegionId(p.getRegionId());
                        projectDetailInfo.setSeatingRequir(p.getSeatingRequir());
                        projectDetailInfo.setPremiereTime(p.getPremiereTime());
                        projectDetailInfo.setRounds(p.getRounds());
                        projectDetailInfo.setShowLast(p.getShowLast());
                        projectDetailInfo.setDescription(p.getDescription());
                        projectDetailInfo.setPlot(p.getPlot());
                        projectDetailInfo.setAwardsDescpt(p.getAwardsDescpt());
                        projectDetailInfo.setRequirements(p.getRequirements());
                        projectDetailInfo.setStatus(p.getStatus());
                        projectDetailInfo.setUserId(p.getUserId());
                        projectDetailInfo.setCreateTime(p.getCreateTime());
                        projectDetailInfo.setUpdateTime(p.getUpdateTime());
                        projectDetailInfo.setPerformanceBeginDate(p.getPerformanceBeginDate());
                        projectDetailInfo.setPerformanceEndDate(p.getPerformanceEndDate());
                        projectDetailInfo.setRegionName(p.getRegionName());
                        projectDetailInfo.setPictures(p.getPictures());
                        projectDetailInfo.setStaffs(p.getStaffs());
                        projectDetailInfo.setViewTimes(p.getViewTimes());
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
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
            }
        });
    }

    /**
     * 恢复ProjectDetailInfo为原始值
     */
    private void initProjectDetailInfo() {
        projectDetailInfo.setId(null);
        projectDetailInfo.setLogo(null);
        projectDetailInfo.setTitle(null);
        projectDetailInfo.setClassifyId(null);
        projectDetailInfo.setLinkman(null);
        projectDetailInfo.setLinkTel(null);
        projectDetailInfo.setPeopleNum(null);
        projectDetailInfo.setExpense(null);
        projectDetailInfo.setRegionId(null);
        projectDetailInfo.setSeatingRequir(null);
        projectDetailInfo.setPremiereTime(null);
        projectDetailInfo.setRounds(null);
        projectDetailInfo.setShowLast(null);
        projectDetailInfo.setDescription(null);
        projectDetailInfo.setPlot(null);
        projectDetailInfo.setAwardsDescpt(null);
        projectDetailInfo.setRequirements(null);
        projectDetailInfo.setStatus(0);
        projectDetailInfo.setUserId(null);
        projectDetailInfo.setCreateTime(0);
        projectDetailInfo.setUpdateTime(null);
        projectDetailInfo.setPerformanceBeginDate(null);
        projectDetailInfo.setPerformanceEndDate(null);
        projectDetailInfo.setRegionName(null);
        projectDetailInfo.setPictures(null);
        projectDetailInfo.setStaffs(null);
        projectDetailInfo.setViewTimes(0);
    }

    /**
     * 更新相应的页面
     */
    private void updateEditUI() {
        sdvAvatar.setImageURI(projectDetailInfo.getLogo());
        switchRelease.setChecked(projectDetailInfo.getStatus() == 1);

        String dataBase = TextUtils.isEmpty(projectDetailInfo.getTitle()) ?
                getResources().getString(R.string.app_un_fill) :
                getResources().getString(R.string.app_has_filled);
        dataItemBase.setDesText(dataBase);

        String dataProjectDes = TextUtils.isEmpty(projectDetailInfo.getDescription()) ?
                getResources().getString(R.string.app_un_fill) :
                getResources().getString(R.string.app_has_filled);
        dataItemIntro.setDesText(dataProjectDes);

        String dataProjectShowDes = TextUtils.isEmpty(projectDetailInfo.getPlot()) ?
                getResources().getString(R.string.app_un_fill) :
                getResources().getString(R.string.app_has_filled);
        dataItemShowIntro.setDesText(dataProjectShowDes);

        String dataProjectAwardsDes = TextUtils.isEmpty(projectDetailInfo.getAwardsDescpt()) ?
                getResources().getString(R.string.app_un_fill) :
                getResources().getString(R.string.app_has_filled);
        dataItemAwardRecord.setDesText(dataProjectAwardsDes);

        String dataProjectRequire = TextUtils.isEmpty(projectDetailInfo.getRequirements()) ?
                getResources().getString(R.string.app_un_fill) :
                getResources().getString(R.string.app_has_filled);
        dataItemTechRequire.setDesText(dataProjectRequire);

        String dataProjectPic =
                projectDetailInfo.getPictures() == null || projectDetailInfo.getPictures().size() <= 0 ?
                        getResources().getString(R.string.app_un_fill) :
                        getResources().getString(R.string.app_has_filled);
        dataItemPhoto.setDesText(dataProjectPic);

        String dataProjectStarf =
                projectDetailInfo.getStaffs() == null || projectDetailInfo.getStaffs().size() <= 0 ?
                        getResources().getString(R.string.app_un_fill) :
                        getResources().getString(R.string.app_has_filled);
        dataItemCreatorIntro.setDesText(dataProjectStarf);

    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_edit);
    }

    @Override
    public void setView() {
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 基本资料
     */
    @OnClick(R.id.dataitem_base)
    public void jumpProjectDataBase() {
        invokActivity(this, ProjectBaseActivity.class, null, JumpCode.FLAG_REQ_PROJECT_BASE);
    }

    /**
     * 项目介绍
     */
    @OnClick(R.id.dataitem_intro)
    public void jumpProjectIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 演出介绍
     */
    @OnClick(R.id.dataitem_show_intro)
    public void jumpProjectShowIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_SHOW_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 获奖记录
     */
    @OnClick(R.id.dataitem_award_record)
    public void jumpProjectAward() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_AWARD);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 技术要求
     */
    @OnClick(R.id.dataitem_tech_require)
    public void jumpProjectTech() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_TECH_REQUIRE);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 主创介绍
     */
    @OnClick(R.id.dataitem_creator_intro)
    public void jumpCreatorIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_CREATOR_INTRO);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 剧照
     */
    @OnClick(R.id.dataitem_photo)
    public void jumpPhotoUpload() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_PHOTO_UPLOAD);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    /**
     * 上传封面
     */
    @OnClick(R.id.sdv_avatar)
    public void changeCover() {
        if (takePhotoDialog == null) {
            takePhotoDialog = TakePhotoDialog.newInstence();
            takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                @Override
                public void onTakePhoto(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.takePhoto(ProjectEditActivity.this, selectList);
                }

                @Override
                public void onAlbuml(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.photoAlbum(ProjectEditActivity.this, selectList);
                }
            });
        }
        takePhotoDialog.show(getSupportFragmentManager(), "TAKEPHOTO.DIALOG");
    }

    /**
     * 上传图片
     *
     * @param path
     */
    public void uploadPhoto(String path) {
        LogUtil.i(TAG, "path = " + path);
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
                        changeProjectCover(avatarUrl);
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
     * 修改封面
     *
     * @param picUrl
     */
    public void changeProjectCover(final String picUrl) {
        Map<String, String> params = new TreeMap<>();
        if (ProjectDetailInfo.getInstance().getId() != null) {
            params.put("id", ProjectDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("logo", picUrl);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_EDIT_LOGO, params, 67, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        //{"id":"8a999cce5f5da93b015f5f338d0a0020"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String theatreId = jsonObject.getString("id");
                        ProjectDetailInfo.getInstance().setId(theatreId);
                        ProjectDetailInfo.getInstance().setLogo(picUrl);
                        showToast(getString(R.string.tip_project_cover_suc));
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
                showToast(getString(R.string.tip_project_cover_fail));
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
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
     * 发布项目
     */
    @OnClick(R.id.view_project_release)
    public void releaseProject() {
        if (switchRelease.isChecked()) {
            projectOffline();
        } else {
            projectRelease();
        }
    }

    /**
     * 发布项目
     */
    private void projectRelease() {
        if (TextUtils.isEmpty(projectDetailInfo.getId())) {
            showToast(getString(R.string.tip_theatre_release));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", projectDetailInfo.getInstance().getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_RELEASE, params, 79, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_project_release_suc));
                    projectDetailInfo.setStatus(1);
                    switchRelease.setChecked(true);
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
                showToast(getString(R.string.tip_project_release_fail));
            }
        });
    }

    /**
     * 下线项目
     */
    private void projectOffline() {
        Map<String, String> params = new TreeMap<>();
        params.put("id", projectDetailInfo.getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_OFFLINE, params, 77, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_project_offline_suc));
                    switchRelease.setChecked(false);
                    projectDetailInfo.setStatus(0);
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
                showToast(getString(R.string.tip_project_offline_fail));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(ProjectInfoChangeEvent event) {
        if (event == null) {
            return;
        }
        updateEditUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
