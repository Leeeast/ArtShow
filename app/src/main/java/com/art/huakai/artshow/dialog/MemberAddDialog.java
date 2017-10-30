package com.art.huakai.artshow.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.ProjectEditActivity;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.Staff;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class MemberAddDialog extends BaseDialogFragment {

    public static final String PARAMS_STAFF = "PARAMS_STAFF";
    public static final String PARAMS_POSITION = "PARAMS_POSITION";

    @BindView(R.id.sdv_member)
    SimpleDraweeView sdvMember;
    @BindView(R.id.edt_project_member_name)
    EditText edtProjectMemberName;
    @BindView(R.id.edt_project_member_title)
    EditText edtProjectMemberTitle;
    @BindView(R.id.edt_introduce)
    EditText edtIntroduce;


    private TakePhotoDialog takePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private ShowProgressDialog showProgressDialog;
    private Staff mStaff;
    private int mPosition;


    private CommitCallBack mCommitCallBack;

    public void setCommitCallBack(CommitCallBack commitCallBack) {
        this.mCommitCallBack = commitCallBack;
    }

    public static MemberAddDialog newInstence() {
        MemberAddDialog typeConfirmDialog = new MemberAddDialog();
        return typeConfirmDialog;
    }

    public static MemberAddDialog newInstence(int position, Staff staff) {
        MemberAddDialog typeConfirmDialog = new MemberAddDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAMS_POSITION, position);
        bundle.putSerializable(PARAMS_STAFF, staff);
        typeConfirmDialog.setArguments(bundle);
        return typeConfirmDialog;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        if (bundle != null) {
            mStaff = (Staff) bundle.getSerializable(PARAMS_STAFF);
            mPosition = bundle.getInt(PARAMS_POSITION);
        } else {
            mStaff = new Staff();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_member_add;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {
        sdvMember.setImageURI(mStaff.getPhoto());
        edtProjectMemberName.setText(mStaff.getName() == null ? "" : mStaff.getName());
        edtProjectMemberTitle.setText(mStaff.getRoleName() == null ? "" : mStaff.getRoleName());
        edtIntroduce.setText(mStaff.getDescpt() == null ? "" : mStaff.getDescpt());
    }

    @OnClick(R.id.lly_close)
    public void closeDialog() {
        this.dismiss();
    }

    @OnClick(R.id.tv_subtitle)
    public void commit() {
        if (TextUtils.isEmpty(mStaff.getPhoto())) {
            showToast(getString(R.string.tip_project_staff_photo));
            return;
        }
        String staffName = edtProjectMemberName.getText().toString().trim();
        if (TextUtils.isEmpty(staffName)) {
            showToast(getString(R.string.tip_project_staff_name));
            return;
        }
        String staffTitle = edtProjectMemberTitle.getText().toString().trim();
        if (TextUtils.isEmpty(staffTitle)) {
            showToast(getString(R.string.tip_project_staff_title));
            return;
        }
        String staffDesc = edtIntroduce.getText().toString().trim();
        if (TextUtils.isEmpty(staffDesc)) {
            showToast(getString(R.string.tip_project_staff_desc));
            return;
        }
        if (TextUtils.isEmpty(mStaff.getId())) {
            long timeMillis = System.currentTimeMillis();
            mStaff.setId(String.valueOf(timeMillis));
            mStaff.setName(staffName);
            mStaff.setRoleName(staffTitle);
            mStaff.setDescpt(staffDesc);
            if (mCommitCallBack != null) {
                mCommitCallBack.createNewStaff(mStaff);
            }
        } else {
            mStaff.setName(staffName);
            mStaff.setRoleName(staffTitle);
            mStaff.setDescpt(staffDesc);
            if (mCommitCallBack != null) {
                mCommitCallBack.updateStaff(mPosition, mStaff);
            }
        }
        this.dismiss();
    }

    @OnClick(R.id.sdv_member)
    public void selectPicture() {
        if (takePhotoDialog == null) {
            takePhotoDialog = TakePhotoDialog.newInstence();
            takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                @Override
                public void onTakePhoto(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.takePhoto(MemberAddDialog.this, selectList);
                }

                @Override
                public void onAlbuml(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.photoAlbum(MemberAddDialog.this, selectList);
                }
            });
        }
        takePhotoDialog.show(getChildFragmentManager(), "TAKEPHOTO.DIALOG");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
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
        sdvMember.setImageURI("file:///" + path);
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
                        mStaff.setPhoto(avatarUrl);
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

    public interface CommitCallBack {
        void createNewStaff(Staff staff);

        void updateStaff(int position, Staff staff);
    }
}
