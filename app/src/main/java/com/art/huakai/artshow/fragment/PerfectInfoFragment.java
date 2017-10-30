package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TakePhotoDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 账户类型选择Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class PerfectInfoFragment extends BaseFragment implements View.OnClickListener {
    private LocalUserInfo localUserInfo;
    private EditText edtName;
    private TextView tvPerfectDes;
    private ShowProgressDialog showProgressDialog;
    private TextInputLayout tilyName;
    private SimpleDraweeView sdvAvatar;
    private TakePhotoDialog mTakePhotoDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private String mAvatarUrl;

    public PerfectInfoFragment() {
        // Required empty public constructor
    }

    public static PerfectInfoFragment newInstance() {
        PerfectInfoFragment fragment = new PerfectInfoFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        localUserInfo = LocalUserInfo.getInstance();
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_perfect_info;
    }

    @Override
    public void initView(View rootView) {

        edtName = (EditText) rootView.findViewById(R.id.edt_name);
        tvPerfectDes = (TextView) rootView.findViewById(R.id.tv_perfect_des);
        tilyName = (TextInputLayout) rootView.findViewById(R.id.tily_name);
        sdvAvatar = (SimpleDraweeView) rootView.findViewById(R.id.sdv_avatar);

        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        rootView.findViewById(R.id.btn_next_step).setOnClickListener(this);
        sdvAvatar.setOnClickListener(this);
    }

    @Override
    public void setView() {
        switch (localUserInfo.getUserType()) {
            case LocalUserInfo.USER_TYPE_PERSONAL:
                tilyName.setHint(getString(R.string.perfect_type_personal));
                tvPerfectDes.setText(R.string.perfect_des_other);
                break;
            case LocalUserInfo.USER_TYPE_INSTITUTION:
                tilyName.setHint(getString(R.string.perfect_name_institution));
                tvPerfectDes.setText(R.string.perfect_des_institution);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_next_step:
                doPerfectInfo();
                break;
            case R.id.sdv_avatar:
                if (mTakePhotoDialog == null) {
                    mTakePhotoDialog = TakePhotoDialog.newInstence();
                    mTakePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                        @Override
                        public void onTakePhoto(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            TakePhotoDialog.takePhoto(PerfectInfoFragment.this, selectList);
                        }

                        @Override
                        public void onAlbuml(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            TakePhotoDialog.photoAlbum(PerfectInfoFragment.this, selectList);
                        }
                    });
                }
                mTakePhotoDialog.show(getFragmentManager(), "TAKEPHOTO.DIALOG");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
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
                        mAvatarUrl = jsonObject.getString("url");
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
     * 提交信息
     */
    private void doPerfectInfo() {
        String userName = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(mAvatarUrl)) {
            showToast(getString(R.string.tip_set_avatar));
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            showToast(getString(R.string.tip_set_name));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", localUserInfo.getId());
        params.put("accessToken", localUserInfo.getAccessToken());
        params.put("name", userName);
        params.put("dp", mAvatarUrl);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        LogUtil.i(TAG, "params=" + params);
        RequestUtil.request(true, Constant.URL_USER_EDITINFO, params, 15, new RequestUtil.RequestListener() {
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
                        String name = jsonObject.getString("name");
                        int status = jsonObject.getInt("status");
                        localUserInfo.setDp(dp);
                        localUserInfo.setName(name);
                        localUserInfo.setStatus(status);
                        SharePreUtil.getInstance().storeUserInfo(localUserInfo);
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_ACTION_PERFECT_INFO_SUC));
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
