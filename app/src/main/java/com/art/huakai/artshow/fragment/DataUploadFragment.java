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
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.AccountInfoActivity;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
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
import org.json.JSONArray;
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
public class DataUploadFragment extends BaseFragment implements View.OnClickListener {
    private static final String PARAMS_FROM = "PARAMS_FROM";
    public static final String FROM_LOGIN = "FROM_LOGIN";
    public static final String FROM_ME = "FROM_ME";
    private int mUserType;
    private TextInputLayout tiLyAuthName, tiLyIdentityNumber, tiLyIdentityConnectName, tiLyIdentityConnectPhone;
    private EditText edtAuthName, edtIdentityNumber, edtIdentityConnectName, edtIdentityConnectPhone;
    private TextView tvDataUploadTip;
    private String mFrom;
    private List<LocalMedia> selectList = new ArrayList<>();
    private SimpleDraweeView sdvData;
    private TakePhotoDialog takePhotoDialog;
    private ShowProgressDialog showProgressDialog;
    private String mAvatarUrl = "";

    public DataUploadFragment() {
        // Required empty public constructor
    }

    public static DataUploadFragment newInstance(String from) {
        DataUploadFragment fragment = new DataUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mUserType = LocalUserInfo.getInstance().getUserType();
        if (bundle != null) {
            mFrom = bundle.getString(PARAMS_FROM, "");
        }
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_data_upload;
    }

    @Override
    public void initView(View rootView) {
        tiLyAuthName = (TextInputLayout) rootView.findViewById(R.id.tily_auth_name);
        tiLyIdentityNumber = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_number);
        tiLyIdentityConnectName = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_connect_name);
        tiLyIdentityConnectPhone = (TextInputLayout) rootView.findViewById(R.id.tiLy_identity_connect_phone);
        edtAuthName = (EditText) rootView.findViewById(R.id.edt_auth_name);
        edtIdentityNumber = (EditText) rootView.findViewById(R.id.edt_identity_number);
        edtIdentityConnectName = (EditText) rootView.findViewById(R.id.edt_identity_connect_name);
        edtIdentityConnectPhone = (EditText) rootView.findViewById(R.id.edt_identity_connect_phone);
        tvDataUploadTip = (TextView) rootView.findViewById(R.id.tv_data_upload_tip);
        sdvData = (SimpleDraweeView) rootView.findViewById(R.id.sdv_data);

        TextView tvSubtitle = (TextView) rootView.findViewById(R.id.tv_subtitle);
        if (mFrom.equals(FROM_LOGIN)) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(R.string.jump_auth);
            tvSubtitle.setOnClickListener(this);
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.btn_commit_data).setOnClickListener(this);
        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        sdvData.setOnClickListener(this);
    }

    @Override
    public void setView() {
        String hintName = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_name_personal) :
                getString(R.string.data_auth_name_institution);
        String hintIdentifyID = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_identify_personal) :
                getString(R.string.data_auth_identify_institution);
        String uploadTip = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_upload_tip_personal) :
                getString(R.string.data_auth_upload_tip_institution);

        tiLyAuthName.setHint(hintName);
        tiLyIdentityNumber.setHint(hintIdentifyID);
        tiLyIdentityConnectName.setHint(getString(R.string.data_auth_connect_name));
        tiLyIdentityConnectPhone.setHint(getString(R.string.data_auth_connect_phone));
        tvDataUploadTip.setText(uploadTip);
        if (mUserType == LocalUserInfo.USER_TYPE_PERSONAL) {
            tiLyIdentityConnectName.setVisibility(View.GONE);
            tiLyIdentityConnectPhone.setVisibility(View.GONE);
        } else {
            tiLyIdentityConnectName.setVisibility(View.VISIBLE);
            tiLyIdentityConnectPhone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().setResult(JumpCode.FLAG_RES_DATA_AUTH);
                getActivity().finish();
                break;
            case R.id.btn_commit_data:
                commitData();
                break;
            case R.id.tv_subtitle:
                Toast.makeText(getContext(), "跳过", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sdv_data:
                if (takePhotoDialog == null) {
                    takePhotoDialog = TakePhotoDialog.newInstence();
                    takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                        @Override
                        public void onTakePhoto(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            TakePhotoDialog.takePhoto(DataUploadFragment.this, selectList);
                        }

                        @Override
                        public void onAlbuml(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            TakePhotoDialog.photoAlbum(DataUploadFragment.this, selectList);
                        }
                    });
                }
                takePhotoDialog.show(getChildFragmentManager(), "TAKEPHOTO.DIALOG");
                break;
        }
    }

    /**
     * 上传资质
     */
    private void commitData() {
        String name = edtAuthName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            String tipNameInput = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.tip_name_input_personal) :
                    getString(R.string.tip_name_input_institution);
            Toast.makeText(getContext(), tipNameInput, Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 有时间做个身份证校验正则
        String identifyID = edtIdentityNumber.getText().toString().trim();
        if (TextUtils.isEmpty(identifyID)) {
            String tipIdentifyIDInput = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.tip_identifyid_input_personal) :
                    getString(R.string.tip_identifyid_input_institution);
            Toast.makeText(getContext(), tipIdentifyIDInput, Toast.LENGTH_SHORT).show();
            return;
        }
        String connectName = edtIdentityConnectName.getText().toString().trim();
        String connectPhone = edtIdentityConnectPhone.getText().toString().trim();
        if (!(mUserType == LocalUserInfo.USER_TYPE_PERSONAL)) {
            if (TextUtils.isEmpty(connectName)) {
                Toast.makeText(getContext(), getString(R.string.tip_connect_name_input), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(connectPhone)) {
                Toast.makeText(getContext(), getString(R.string.tip_connect_phone_input), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String urlJson = "";
        if (TextUtils.isEmpty(mAvatarUrl)) {
            Toast.makeText(getContext(), getString(R.string.tip_auth_data_update), Toast.LENGTH_SHORT).show();
            return;
        } else {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(mAvatarUrl);
            urlJson = jsonArray.toString();
        }
        String applyType = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? "0" : "1";
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("pictures", urlJson);
        params.put("name", name);
        params.put("cardCode", identifyID);
        params.put("applyType", applyType);
        if (mUserType == LocalUserInfo.USER_TYPE_INSTITUTION) {
            params.put("linkman", connectName);
            params.put("linkTel", connectPhone);
        }
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_AUTHENTICATION_APPLY, params, 58, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    DataUploadSusFragment dataUploadSusFragment = DataUploadSusFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, dataUploadSusFragment, dataUploadSusFragment.getTAG()).commit();
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
        sdvData.setBackground(null);
        sdvData.setImageURI("file:///" + path);
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
}
