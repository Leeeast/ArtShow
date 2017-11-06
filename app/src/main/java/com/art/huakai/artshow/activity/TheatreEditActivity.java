package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.dialog.TakePhotoDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.TheatreTechParamsUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;
import com.art.huakai.artshow.widget.calendar.CalendarSelectorActivity;
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

public class TheatreEditActivity extends BaseActivity {
    public static final String PARAMS_NEW_CREATE = "PARAMS_NEW_CREATE";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dataitem_base)
    DataItem dataItemBase;
    @BindView(R.id.dataitem_detail_intro)
    DataItem dataItemDetailIntro;
    @BindView(R.id.dataitem_intro)
    DataItem dataItemIntro;
    @BindView(R.id.dataitem_seat_pic)
    DataItem dataitemSeatPic;
    @BindView(R.id.dataitem_technical_parameters)
    DataItem dataitemTechParams;
    @BindView(R.id.dataitem_pic)
    DataItem dataitemPic;
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.switch_release)
    Switch switchRelease;

    public boolean isNewCreate = false;
    private ShowProgressDialog showProgressDialog;
    private List<LocalMedia> selectList = new ArrayList<>();
    private TakePhotoDialog takePhotoDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_edit;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            isNewCreate = extras.getBoolean(PARAMS_NEW_CREATE, false);
        }
        if (isNewCreate) {
            initTheatreDetailInfo();
        } else {
            getTheatreDetail(TheatreDetailInfo.getInstance().getId());
        }
    }

    /**
     * 初始化TheatreDetailInfo单例
     */
    private void initTheatreDetailInfo() {
        TheatreDetailInfo instance = TheatreDetailInfo.getInstance();
        instance.setId(null);
        instance.setLogo(null);
        instance.setName(null);
        instance.setRoomName(null);
        instance.setSeating(null);
        instance.setRegionId(null);
        instance.setAddress(null);
        instance.setCoordinate(null);
        instance.setLinkman(null);
        instance.setLinkTel(null);
        instance.setExpense(null);
        instance.setDescription(null);
        instance.setStageHeight(null);
        instance.setStageWidth(null);
        instance.setStageDepth(null);
        instance.setCurtainHeight(null);
        instance.setCurtainWidth(null);
        instance.setDressingRoomNum(null);
        instance.setRehearsalRoomNum(null);
        instance.setPropRoomNum(null);
        instance.setCostumeRoomNum(null);
        instance.setStageLights(null);
        instance.setStereoEquipment(null);
        instance.setBroadcastSystem(null);
        instance.setSteeve(null);
        instance.setMusicStage(null);
        instance.setChorusPlatform(null);
        instance.setOrchestraPit(null);
        instance.setAcousticShroud(null);
        instance.setBandPlatform(null);
        instance.setCurtainSystem(null);
        instance.setSpecialEquipment(null);
        instance.setProjector(null);
        instance.setPriceDiagram(null);
        instance.setDetailedIntroduce(null);
        instance.setUserId(null);
        instance.setStatus(0);
        instance.setCreateTime(0);
        instance.setUpdateTime(null);
        instance.setRegionName(null);
        instance.setPictures(null);
        instance.setDisabledDates(null);
        instance.setDisabledMonths(null);
        instance.setViewTimes(0);
    }

    /**
     * 剧场详情获取
     */
    private void getTheatreDetail(final String theatreId) {
        Map<String, String> params = new TreeMap<>();
        params.put("id", theatreId);
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        RequestUtil.request(true, Constant.URL_USER_THEATER_DETAIL, params, 62, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        //初始化剧场单例类
                        TheatreDetailInfo t = GsonTools.parseData(obj, TheatreDetailInfo.class);
                        TheatreDetailInfo instance = TheatreDetailInfo.getInstance();
                        instance.setId(t.getId());
                        instance.setLogo(t.getLogo());
                        instance.setName(t.getName());
                        instance.setRoomName(t.getRoomName());
                        instance.setSeating(t.getSeating());
                        instance.setRegionId(t.getRegionId());
                        instance.setAddress(t.getAddress());
                        instance.setCoordinate(t.getCoordinate());
                        instance.setLinkman(t.getLinkman());
                        instance.setLinkTel(t.getLinkTel());
                        instance.setExpense(t.getExpense());
                        instance.setDescription(t.getDescription());
                        instance.setStageHeight(t.getStageHeight());
                        instance.setStageWidth(t.getStageWidth());
                        instance.setStageDepth(t.getStageDepth());
                        instance.setCurtainHeight(t.getCurtainHeight());
                        instance.setCurtainWidth(t.getCurtainWidth());
                        instance.setDressingRoomNum(t.getDressingRoomNum());
                        instance.setRehearsalRoomNum(t.getRehearsalRoomNum());
                        instance.setPropRoomNum(t.getPropRoomNum());
                        instance.setCostumeRoomNum(t.getCostumeRoomNum());
                        instance.setStageLights(t.getStageLights());
                        instance.setStereoEquipment(t.getStereoEquipment());
                        instance.setBroadcastSystem(t.getBroadcastSystem());
                        instance.setSteeve(t.getSteeve());
                        instance.setMusicStage(t.getMusicStage());
                        instance.setChorusPlatform(t.getChorusPlatform());
                        instance.setOrchestraPit(t.getOrchestraPit());
                        instance.setAcousticShroud(t.getAcousticShroud());
                        instance.setBandPlatform(t.getBandPlatform());
                        instance.setCurtainSystem(t.getCurtainSystem());
                        instance.setSpecialEquipment(t.getSpecialEquipment());
                        instance.setProjector(t.getProjector());
                        instance.setPriceDiagram(t.getPriceDiagram());
                        instance.setDetailedIntroduce(t.getDetailedIntroduce());
                        instance.setUserId(t.getUserId());
                        instance.setStatus(t.getStatus());
                        instance.setCreateTime(t.getCreateTime());
                        instance.setUpdateTime(t.getUpdateTime());
                        instance.setRegionName(t.getRegionName());
                        instance.setPictures(t.getPictures());
                        instance.setDisabledDates(t.getDisabledDates());
                        instance.setDisabledMonths(t.getDisabledMonths());
                        instance.setViewTimes(t.getViewTimes());
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

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_title_my);
    }

    @Override
    public void setView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    public void jumpTheatreDataBase() {
        invokActivity(this, TheatreBaseActivity.class, null, JumpCode.FLAG_REQ_THEATRE_BASE);
    }

    /**
     * 简介
     */
    @OnClick(R.id.dataitem_intro)
    public void jumpTheatreIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_INTRO);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 详细介绍
     */
    @OnClick(R.id.dataitem_detail_intro)
    public void jumpTheatreDetailIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_DETAIL_INTRO);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 票区图
     */
    @OnClick(R.id.dataitem_seat_pic)
    public void jumpTheatreTicket() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_TICKET);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 剧场相片
     */
    @OnClick(R.id.dataitem_pic)
    public void jumpTheatrePic() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_PIC);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 剧场相片
     */
    @OnClick(R.id.dataitem_technical_parameters)
    public void jumpTheatreTechParam() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_TECH_PARAM);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 更换封面
     */
    @OnClick(R.id.sdv_avatar)
    public void changeCover() {
        if (takePhotoDialog == null) {
            takePhotoDialog = TakePhotoDialog.newInstence();
            takePhotoDialog.setOnCallBack(new TakePhotoDialog.CallBack() {
                @Override
                public void onTakePhoto(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.takePhoto(TheatreEditActivity.this, selectList);
                }

                @Override
                public void onAlbuml(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    TakePhotoDialog.photoAlbum(TheatreEditActivity.this, selectList);
                }
            });
        }
        takePhotoDialog.show(getSupportFragmentManager(), "TAKEPHOTO.DIALOG");
    }

    public ArrayList<String> scheduleAdd = new ArrayList<>();

    @OnClick(R.id.dataitem_schedule)
    public void dataitemSchedule() {
        Intent i = new Intent(this, CalendarSelectorActivity.class);
        i.putExtra(CalendarSelectorActivity.DAYS_OF_SELECT, 1000);
        i.putExtra(CalendarSelectorActivity.ORDER_DAY, "");
        i.putExtra(CalendarSelectorActivity.SELECT_ENALBE, true);
        i.putStringArrayListExtra(CalendarSelectorActivity.SELECT_LIST, scheduleAdd);
        startActivityForResult(i, 10);
        //startActivity(i);
    }

    /**
     * 剧场信息更新，通知页面变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(TheatreInfoChangeEvent event) {
        if (event == null) {
            return;
        }
        updateEditUI();
    }


    /**
     * 根据剧场信息切换页面变化
     */
    private void updateEditUI() {
        //详细描述
        TheatreDetailInfo theaterInstance = TheatreDetailInfo.getInstance();
        if (!TextUtils.isEmpty(theaterInstance.getLogo())) {
            sdvAvatar.setImageURI(theaterInstance.getLogo());
        }
        String detailInfoStatus = TextUtils.isEmpty(theaterInstance.getDetailedIntroduce()) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataItemDetailIntro.setDesText(detailInfoStatus);

        String theatreIntro = (TextUtils.isEmpty(theaterInstance.getDescription()) ||
                theaterInstance.getDescription().equals(Constant.DESCRIPTION_DEFAULT)) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataItemIntro.setDesText(theatreIntro);

        String theatrePriceDiagram = TextUtils.isEmpty(theaterInstance.getPriceDiagram()) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataitemSeatPic.setDesText(theatrePriceDiagram);

        String theatrePictures = theaterInstance.getPictures() == null || theaterInstance.getPictures().size() <= 0 ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataitemPic.setDesText(theatrePictures);

        String theatreBase = TextUtils.isEmpty(theaterInstance.getName()) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataItemBase.setDesText(theatreBase);

        String theatreTech = TheatreTechParamsUtil.isTechParamsFill() ?
                getString(R.string.app_has_filled) : getString(R.string.app_un_fill);
        dataitemTechParams.setDesText(theatreTech);
        switchRelease.setChecked(TheatreDetailInfo.getInstance().getStatus() == 1);
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
                        changeTheatreCover(avatarUrl);
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
    public void changeTheatreCover(final String picUrl) {
        Map<String, String> params = new TreeMap<>();
        if (TheatreDetailInfo.getInstance().getId() != null) {
            params.put("id", TheatreDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("logo", picUrl);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_THEATER_EDIT_LOGO, params, 67, new RequestUtil.RequestListener() {
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
                        TheatreDetailInfo.getInstance().setId(theatreId);
                        TheatreDetailInfo.getInstance().setLogo(picUrl);
                        showToast(getString(R.string.tip_theatre_cover_upload));
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
                showToast(getString(R.string.tip_theatre_cover_fail));
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.view_theatre_release)
    public void theatreReleaseOffline() {
        if (switchRelease.isChecked()) {
            theatreOffline();
        } else {
            theatreRelease();
        }
    }

    /**
     * 发布剧场
     */
    public void theatreRelease() {
        if (TextUtils.isEmpty(TheatreDetailInfo.getInstance().getId())) {
            showToast(getString(R.string.tip_theatre_release));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("id", TheatreDetailInfo.getInstance().getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_THEATER_RELEASE, params, 69, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_theatre_release_suc));
                    switchRelease.setChecked(true);
                    TheatreDetailInfo.getInstance().setStatus(1);
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
                showToast(getString(R.string.tip_theatre_release_fail));
            }
        });
    }

    /**
     * 下线剧场
     */
    public void theatreOffline() {
        Map<String, String> params = new TreeMap<>();
        params.put("id", TheatreDetailInfo.getInstance().getId());
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_THEATER_OFFLINE, params, 69, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_theatre_offline_suc));
                    switchRelease.setChecked(false);
                    TheatreDetailInfo.getInstance().setStatus(0);
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
                showToast(getString(R.string.tip_theatre_offline_fail));
            }
        });
    }

}
