package com.art.huakai.artshow.utils;

import android.text.TextUtils;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.entity.TechParamsBean;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.entity.TheatreDetailInfo;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/28.
 */

public class TheatreTechParamsUtil {
    public static boolean isTechParamsFill() {
        return !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getStageHeight()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getStageWidth()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getStageDepth()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getCurtainHeight()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getCurtainWidth()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getDressingRoomNum()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getRehearsalRoomNum()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getPropRoomNum()) ||
                !TextUtils.isEmpty(TheatreDetailInfo.getInstance().getCostumeRoomNum()) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getStageLights()) &&
                        TheatreDetailInfo.getInstance().getStageLights().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getStereoEquipment()) &&
                        TheatreDetailInfo.getInstance().getStereoEquipment().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getBroadcastSystem()) &&
                        TheatreDetailInfo.getInstance().getBroadcastSystem().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getSteeve()) &&
                        TheatreDetailInfo.getInstance().getSteeve().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getMusicStage()) &&
                        TheatreDetailInfo.getInstance().getMusicStage().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getChorusPlatform()) &&
                        TheatreDetailInfo.getInstance().getChorusPlatform().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getAcousticShroud()) &&
                        TheatreDetailInfo.getInstance().getAcousticShroud().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getCurtainSystem()) &&
                        TheatreDetailInfo.getInstance().getCurtainSystem().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getSpecialEquipment()) &&
                        TheatreDetailInfo.getInstance().getSpecialEquipment().equals("1")) ||
                (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getProjector()) &&
                        TheatreDetailInfo.getInstance().getProjector().equals("1"));
    }

    public static void getTheatreTechParamsAddedList(List<String> added) {
        String[] stringArray = ShowApplication.getAppContext().getResources().getStringArray(R.array.theatre_tech_param);
        TheatreDetailInfo theatre = TheatreDetailInfo.getInstance();
        if (!TextUtils.isEmpty(theatre.getStageLights()) &&
                theatre.getStageLights().equals("1")) {
            added.add(stringArray[0]);//灯光
        }
        if (!TextUtils.isEmpty(theatre.getStereoEquipment()) &&
                theatre.getStereoEquipment().equals("1")) {
            added.add(stringArray[1]);//音响
        }
        if (!TextUtils.isEmpty(theatre.getBroadcastSystem()) &&
                theatre.getBroadcastSystem().equals("1")) {
            added.add(stringArray[2]);//播音系统
        }
        if (!TextUtils.isEmpty(theatre.getSteeve()) &&
                theatre.getSteeve().equals("1")) {
            added.add(stringArray[3]);//吊杆
        }
        if (!TextUtils.isEmpty(theatre.getMusicStage()) &&
                theatre.getMusicStage().equals("1")) {
            added.add(stringArray[4]);//演奏台
        }
        if (!TextUtils.isEmpty(theatre.getChorusPlatform()) &&
                theatre.getChorusPlatform().equals("1")) {
            added.add(stringArray[5]);//合唱台
        }
        if (!TextUtils.isEmpty(theatre.getOrchestraPit()) &&
                theatre.getOrchestraPit().equals("1")) {
            added.add(stringArray[6]);//乐池
        }
        if (!TextUtils.isEmpty(theatre.getAcousticShroud()) &&
                theatre.getAcousticShroud().equals("1")) {
            added.add(stringArray[7]);//音罩
        }
        if (!TextUtils.isEmpty(theatre.getBandPlatform()) &&
                theatre.getBandPlatform().equals("1")) {
            added.add(stringArray[8]);//乐队平台
        }
        if (!TextUtils.isEmpty(theatre.getCurtainSystem()) &&
                theatre.getCurtainSystem().equals("1")) {
            added.add(stringArray[9]);//幕布系统
        }
        if (!TextUtils.isEmpty(theatre.getSpecialEquipment()) &&
                theatre.getSpecialEquipment().equals("1")) {
            added.add(stringArray[10]);//特殊机具
        }
        if (!TextUtils.isEmpty(theatre.getProjector()) &&
                theatre.getProjector().equals("1")) {
            added.add(stringArray[11]);//投影仪
        }
    }

    public static void getTheatreEquipAddedList(List<String> added, TheatreDetailBean theatre) {
        String[] stringArray = ShowApplication.getAppContext().getResources().getStringArray(R.array.theatre_tech_param);
        if (theatre.getStageLights() == 1) {
            added.add(stringArray[0]);//灯光
        }
        if (theatre.getStereoEquipment() == 1) {
            added.add(stringArray[1]);//音响
        }
        if (theatre.getBroadcastSystem() == 1) {
            added.add(stringArray[2]);//播音系统
        }
        if (theatre.getSteeve() == 1) {
            added.add(stringArray[3]);//吊杆
        }
        if (theatre.getMusicStage() == 1) {
            added.add(stringArray[4]);//演奏台
        }
        if (theatre.getChorusPlatform() == 1) {
            added.add(stringArray[5]);//合唱台
        }
        if (theatre.getOrchestraPit() == 1) {
            added.add(stringArray[6]);//乐池
        }
        if (theatre.getAcousticShroud() == 1) {
            added.add(stringArray[7]);//音罩
        }
        if (theatre.getBandPlatform() == 1) {
            added.add(stringArray[8]);//乐队平台
        }
        if (theatre.getCurtainSystem() == 1) {
            added.add(stringArray[9]);//幕布系统
        }
        if (theatre.getSpecialEquipment() == 1) {
            added.add(stringArray[10]);//特殊机具
        }
        if (theatre.getProjector() == 1) {
            added.add(stringArray[11]);//投影仪
        }
    }

    public static List<TechParamsBean> getTheatreTechParamsAddedList(List<TechParamsBean> added, TheatreDetailBean theatre) {
        if (!TextUtils.isEmpty(theatre.getStageHeight())) {
            added.add(new TechParamsBean("舞台高度", theatre.getStageHeight() + "米"));
        }
        if (!TextUtils.isEmpty(theatre.getStageWidth())) {
            added.add(new TechParamsBean("舞台宽度", theatre.getStageWidth() + "米"));
        }
        if (!TextUtils.isEmpty(theatre.getStageDepth())) {
            added.add(new TechParamsBean("舞台深度", theatre.getStageDepth() + "米"));
        }
        if (!TextUtils.isEmpty(theatre.getCurtainWidth())) {
            added.add(new TechParamsBean("幕布宽度", theatre.getCurtainWidth() + "米"));
        }
        if (!TextUtils.isEmpty(theatre.getCurtainHeight())) {
            added.add(new TechParamsBean("幕布高度", theatre.getCurtainHeight() + "米"));
        }
        added.add(new TechParamsBean("化妆间数量", theatre.getDressingRoomNum() + "个"));
        added.add(new TechParamsBean("排练室数量", theatre.getRehearsalRoomNum() + "个"));
        added.add(new TechParamsBean("道具间数量", theatre.getPropRoomNum() + "个"));
        added.add(new TechParamsBean("服装间数量", theatre.getCostumeRoomNum() + "个"));
        return added;
    }
}
