package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

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

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dataitem_base)
    DataItem dataItemBase;
    @BindView(R.id.dataitem_detail_intro)
    DataItem dataItemDetailIntro;
    @BindView(R.id.dataitem_intro)
    DataItem dataItemIntro;

    public boolean isNewCreate = true;
    private ShowProgressDialog showProgressDialog;

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
        showProgressDialog = new ShowProgressDialog(this);
        if (isNewCreate) {
            editTheatreDescription();
        }
        EventBus.getDefault().register(this);
    }

    /**
     * 剧场描述修改
     * 新创建剧场，为了获取剧场ID
     */
    private void editTheatreDescription() {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("description", Constant.DESCRIPTION_DEFAULT);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_THEATER_EDIT_DESCRIPTION, params, 61, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        //{"id":"8a999cce5f51904d015f528e8bf20005"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String theatreID = jsonObject.getString("id");
                        getTheatreDetail(theatreID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(TheatreEditActivity.this, getString(R.string.tip_mobile_registered), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
            }
        });
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
                    Toast.makeText(TheatreEditActivity.this, getString(R.string.tip_mobile_registered), Toast.LENGTH_SHORT).show();
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
        String detailInfoStatus = TextUtils.isEmpty(theaterInstance.getDetailedIntroduce()) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataItemDetailIntro.setDesText(detailInfoStatus);

        String theatreIntro = (TextUtils.isEmpty(theaterInstance.getDescription()) ||
                theaterInstance.getDescription().equals(Constant.DESCRIPTION_DEFAULT)) ?
                getString(R.string.app_un_fill) : getString(R.string.app_has_filled);
        dataItemIntro.setDesText(theatreIntro);
    }
}
