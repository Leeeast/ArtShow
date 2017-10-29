package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
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
import com.art.huakai.artshow.entity.TalentResumeInfo;
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

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;

    private TakePhotoDialog takePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ShowProgressDialog showProgressDialog;

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
        showProgressDialog = new ShowProgressDialog(this);
        getTalent();
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
                        String mAvatarUrl = jsonObject.getString("url");
                        TalentResumeInfo.getInstance().setLogo(mAvatarUrl);
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
     * 获取简历列表
     *
     * @return
     */
    public void getTalent() {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(this, true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("page", "1");
        params.put("size", "1");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_USER_TALENT, params, 50, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        List<TalentBean> resumeBeens = GsonTools.parseDatas(obj, TalentBean.class);
                        if (resumeBeens != null && resumeBeens.size() > 0) {
                            TalentBean resumeBeen = resumeBeens.get(0);
                            TalentResumeInfo.getInstance().setId(resumeBeen.getId());
                            getResumeDetail();
                        } else {
                            changeResumeDescription();
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

    /**
     * 修改简历个人介绍
     * TODO 此处调用这个方法是为了创建简历，获取简历ID
     */
    public void changeResumeDescription() {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(this, true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("description", "创建简历");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_EDIT_DESCRIPTION, params, 51, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String resumeId = jsonObject.getString("id");
                        TalentResumeInfo.getInstance().setId(resumeId);
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

//    /**
//     * 获取个人简历详情
//     */
//    public void getResumeDetail() {
//        //判断是否登录
//        if (!LoginUtil.checkUserLogin(this, true)) {
//            return;
//        }
//        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
//                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
//            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Map<String, String> params = new TreeMap<>();
//        params.put("id", TalentResumeInfo.getInstance().getTalentResumeId());
//        String sign = SignUtil.getSign(params);
//        params.put("sign", sign);
//        LogUtil.i(TAG, "params = " + params);
//        showProgressDialog.show();
//        RequestUtil.request(true, Constant.URL_TALENT_DETAIL, params, 53, new RequestUtil.RequestListener() {
//            @Override
//            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
//                LogUtil.i(TAG, obj);
//                if (showProgressDialog.isShowing()) {
//                    showProgressDialog.dismiss();
//                }
//                if (isSuccess) {
//                    try {
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ResponseCodeCheck.showErrorMsg(code);
//                }
//            }
//
//            @Override
//            public void onFailed(Call call, Exception e, int id) {
//                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
//                if (showProgressDialog.isShowing()) {
//                    showProgressDialog.dismiss();
//                }
//            }
//        });
//    }

    /**
     * 获取个人简历详情
     */
    public void getResumeDetail() {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(this, true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("id", TalentResumeInfo.getInstance().getId());
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
                        TalentResumeInfo resume = GsonTools.parseData(obj, TalentResumeInfo.class);
                        TalentResumeInfo instanceResume = TalentResumeInfo.getInstance();
                        instanceResume.setId(resume.getId());
                        instanceResume.setLogo(resume.getLogo());
                        instanceResume.setName(resume.getName());
                        instanceResume.setBirthday(resume.getBirthday());
                        instanceResume.setLinkTel(resume.getLinkTel());
                        instanceResume.setRegionId(resume.getRegionId());
                        instanceResume.setDescription(resume.getDescription());
                        instanceResume.setHeight(resume.getHeight());
                        instanceResume.setWeight(resume.getWeight());
                        instanceResume.setSchool(resume.getSchool());
                        instanceResume.setAgency(resume.getAgency());
                        instanceResume.setWorksDescpt(resume.getWorksDescpt());
                        instanceResume.setAwardsDescpt(resume.getAwardsDescpt());
                        instanceResume.setStatus(resume.getStatus());
                        instanceResume.setUserId(resume.getUserId());
                        instanceResume.setCreateTime(resume.getCreateTime());
                        instanceResume.setUpdateTime(resume.getUpdateTime());
                        instanceResume.setPictures(resume.getPictures());
                        instanceResume.setClassifyIds(resume.getClassifyIds());
                        instanceResume.setClassifyNames(resume.getClassifyNames());
                        instanceResume.setRegionName(resume.getRegionName());
                        instanceResume.setAge(resume.getAge());
                        instanceResume.setViewTimes(resume.getViewTimes());
                        instanceResume.setAuthentication(resume.getAuthentication());
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


}
