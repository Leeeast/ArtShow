package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TakePhotoDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
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

public class AccountInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;

    private TakePhotoDialog takePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ShowProgressDialog showProgressDialog;
    private String mAvatarUrl;
    private LocalUserInfo userInfo;
    private RequestCall requestCallUploadPic;
    private RequestCall requestCallCommit;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_account_info;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        userInfo = LocalUserInfo.getInstance();
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.set_account_info);
    }

    @Override
    public void setView() {
        BaseControllerListener baseControllerListener = new BaseControllerListener() {
            @Override
            public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                sdvAvatar.setBackground(null);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(baseControllerListener)
                .setUri(userInfo.getDp()).build();
        sdvAvatar.setController(controller);

        tvAccountName.setText(userInfo.getName());
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 名称
     */
    @OnClick(R.id.rly_name)
    public void showName() {
        invokActivity(this, AccountNameActivity.class, null, JumpCode.FLAG_REQ_SET_ACCOUNT_NAME);
    }

    /**
     * 帐号认证
     */
    @OnClick(R.id.rly_account_auth)
    public void accountAuth() {
        invokActivity(this, DataUploadActivity.class, null, JumpCode.FLAG_REQ_DATA_UPLOAD);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.rly_change_pwd)
    public void changePwd() {
        invokActivity(this, ChangePwdActivity.class, null, JumpCode.FLAG_REQ_CHANGE_PWD);
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.sdv_avatar)
    public void selectPhoto() {
        if (takePhotoDialog == null) {
            takePhotoDialog = TakePhotoDialog.newInstence();
            takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                @Override
                public void onTakePhoto(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.takePhoto(AccountInfoActivity.this, selectList);
                }

                @Override
                public void onAlbuml(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.photoAlbum(AccountInfoActivity.this, selectList);
                }
            });
        }
        takePhotoDialog.show(getSupportFragmentManager(), "TAKEPHOTO.DIALOG");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestCallUploadPic != null) {
            requestCallUploadPic.cancel();
        }
        if (requestCallCommit != null) {
            requestCallCommit.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(NameChangeEvent nameChangeEvent) {
        if (nameChangeEvent == null) {
            return;
        }
        if (tvAccountName != null) {
            tvAccountName.setText(nameChangeEvent.getAccountName());
        }
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
        requestCallUploadPic = RequestUtil.uploadLoadFile(Constant.URL_UPLOAD_FILE, path, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        mAvatarUrl = jsonObject.getString("url");
                        changeUserInfo(mAvatarUrl);
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
     * 修改用户信息
     *
     * @param url
     */
    public void changeUserInfo(String url) {
        if (TextUtils.isEmpty(userInfo.getId())) {
            showToast(getString(R.string.tip_data_error));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", userInfo.getId());
        params.put("accessToken", userInfo.getAccessToken());
        params.put("name", userInfo.getName());
        params.put("dp", url);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        LogUtil.i(TAG, "params=" + params);
        requestCallCommit = RequestUtil.request(true, Constant.URL_USER_EDITINFO, params, 15, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String dp = jsonObject.getString("dp");
                        userInfo.setDp(dp);
                        SharePreUtil.getInstance().storeUserInfo(userInfo);
                        showToast(getString(R.string.change_avatar_suc));
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(getString(R.string.tip_data_parsing_failure));
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
