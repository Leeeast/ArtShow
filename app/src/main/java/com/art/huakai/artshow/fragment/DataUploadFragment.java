package com.art.huakai.artshow.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TakePhotoDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.UserInfo;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.IdNumberUtil;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.PhoneUtils;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SharePreUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

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
    private EditText edtAuthName, edtIdentityNumber, edtIdentityConnectName, edtIdentityConnectPhone;
    private TextView tvAuthName, tvIdentityNumber;
    private TextView tvDataUploadTip;
    private String mFrom;
    private List<LocalMedia> selectList = new ArrayList<>();
    private SimpleDraweeView sdvData;
    private TakePhotoDialog takePhotoDialog;
    private ShowProgressDialog showProgressDialog;
    private String mAvatarUrl = "";
    private RelativeLayout rLyLinkMan;
    private RelativeLayout rLyLinkTel;

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
        rLyLinkMan = (RelativeLayout) rootView.findViewById(R.id.rly_linkman);
        rLyLinkTel = (RelativeLayout) rootView.findViewById(R.id.rly_linktel);
        tvAuthName = (TextView) rootView.findViewById(R.id.tv_auth_name);
        tvIdentityNumber = (TextView) rootView.findViewById(R.id.tv_identity_number);
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
        String hintNameTip = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.tip_name_input_personal) :
                getString(R.string.tip_name_input_institution);
        String hintIdentifyID = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_identify_personal) :
                getString(R.string.data_auth_identify_institution);
        String hintIdentifyIDTip = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.tip_identifyid_input_personal) :
                getString(R.string.tip_identifyid_input_institution);

        String uploadTip = mUserType == LocalUserInfo.USER_TYPE_PERSONAL ? getString(R.string.data_auth_upload_tip_personal) :
                getString(R.string.data_auth_upload_tip_institution);

        tvAuthName.setText(hintName);
        edtAuthName.setHint(hintNameTip);
        tvIdentityNumber.setText(hintIdentifyID);
        edtIdentityNumber.setHint(hintIdentifyIDTip);
        tvDataUploadTip.setText(uploadTip);
        if (mUserType == LocalUserInfo.USER_TYPE_PERSONAL) {
            rLyLinkMan.setVisibility(View.GONE);
            rLyLinkTel.setVisibility(View.GONE);
        } else {
            rLyLinkMan.setVisibility(View.VISIBLE);
            rLyLinkTel.setVisibility(View.VISIBLE);
        }
        setMargionLeft();
    }

    /**
     * 设置左边距
     */
    private void setMargionLeft() {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvIdentityNumber.measure(spec, spec);
        int leftMargion = tvIdentityNumber.getMeasuredWidth() + getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX);

        RelativeLayout.LayoutParams layoutParamsName = (RelativeLayout.LayoutParams) edtAuthName.getLayoutParams();
        layoutParamsName.leftMargin = leftMargion;
        RelativeLayout.LayoutParams layoutParamsID = (RelativeLayout.LayoutParams) edtIdentityNumber.getLayoutParams();
        layoutParamsID.leftMargin = leftMargion;
        RelativeLayout.LayoutParams layoutParamsLinkman = (RelativeLayout.LayoutParams) edtIdentityConnectName.getLayoutParams();
        layoutParamsLinkman.leftMargin = leftMargion;
        RelativeLayout.LayoutParams layoutParamsLinktel = (RelativeLayout.LayoutParams) edtIdentityConnectPhone.getLayoutParams();
        layoutParamsLinktel.leftMargin = leftMargion;
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
                getActivity().setResult(JumpCode.FLAG_RES_DATA_AUTH);
                getActivity().finish();
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
        if (mUserType == LocalUserInfo.USER_TYPE_PERSONAL) {
            //身份证校验
            if (!IdNumberUtil.strongVerifyIdNumber(identifyID)) {
                showToast(getString(R.string.tip_error_id));
                return;
            }
        } else {

        }
        String connectName = edtIdentityConnectName.getText().toString().trim();
        String connectPhone = edtIdentityConnectPhone.getText().toString().trim();
        if (!(mUserType == LocalUserInfo.USER_TYPE_PERSONAL)) {
            if (TextUtils.isEmpty(connectName)) {
                showToast(getString(R.string.tip_connect_name_input));
                return;
            }
            if (TextUtils.isEmpty(connectPhone)) {
                showToast(getString(R.string.tip_connect_phone_input));
                return;
            }
            if (!PhoneUtils.isMobileNumber(connectPhone)) {
                showToast(getString(R.string.please_input_correct_phone));
            }
        }
        String urlJson = "";
        if (TextUtils.isEmpty(mAvatarUrl)) {
            showToast(getString(R.string.tip_auth_data_update));
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
                    updateUserInfo();
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
     * 更新用户信息
     */
    private void updateUserInfo() {
        if (LoginUtil.checkUserLogin(getContext(), false)) {
            Map<String, String> params = new TreeMap<>();
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
            String sign = SignUtil.getSign(params);
            params.put("sign", sign);
            RequestUtil.request(true, Constant.URL_USER_PREVIEW, params, 16, new RequestUtil.RequestListener() {
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

                            DataUploadSusFragment dataUploadSusFragment = DataUploadSusFragment.newInstance();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fly_content, dataUploadSusFragment, dataUploadSusFragment.getTAG()).commit();
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
                }
            });
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
