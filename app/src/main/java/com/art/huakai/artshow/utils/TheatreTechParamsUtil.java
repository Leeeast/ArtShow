package com.art.huakai.artshow.utils;

import android.text.TextUtils;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
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
                TheatreDetailInfo.getInstance().getStageLights().equals("1") ||
                TheatreDetailInfo.getInstance().getStereoEquipment().equals("1") ||
                TheatreDetailInfo.getInstance().getBroadcastSystem().equals("1") ||
                TheatreDetailInfo.getInstance().getSteeve().equals("1") ||
                TheatreDetailInfo.getInstance().getMusicStage().equals("1") ||
                TheatreDetailInfo.getInstance().getChorusPlatform().equals("1") ||
                TheatreDetailInfo.getInstance().getAcousticShroud().equals("1") ||
                TheatreDetailInfo.getInstance().getCurtainSystem().equals("1") ||
                TheatreDetailInfo.getInstance().getSpecialEquipment().equals("1") ||
                TheatreDetailInfo.getInstance().getProjector().equals("1");
    }

    public static void getTheatreTechParamsAddedList(List<String> added) {
        String[] stringArray = ShowApplication.getAppContext().getResources().getStringArray(R.array.theatre_tech_param);
        if (TheatreDetailInfo.getInstance().getStageLights().equals("1")) {
            added.add(stringArray[0]);//灯光
        }
        if (TheatreDetailInfo.getInstance().getStereoEquipment().equals("1")) {
            added.add(stringArray[1]);//音响
        }
        if (TheatreDetailInfo.getInstance().getBroadcastSystem().equals("1")) {
            added.add(stringArray[2]);//播音系统
        }
        if (TheatreDetailInfo.getInstance().getSteeve().equals("1")) {
            added.add(stringArray[3]);//吊杆
        }
        if (TheatreDetailInfo.getInstance().getMusicStage().equals("1")) {
            added.add(stringArray[4]);//演奏台
        }
        if (TheatreDetailInfo.getInstance().getChorusPlatform().equals("1")) {
            added.add(stringArray[5]);//合唱台
        }
        if (TheatreDetailInfo.getInstance().getOrchestraPit().equals("1")) {
            added.add(stringArray[6]);//乐池
        }
        if (TheatreDetailInfo.getInstance().getAcousticShroud().equals("1")) {
            added.add(stringArray[7]);//音罩
        }
        if (TheatreDetailInfo.getInstance().getBandPlatform().equals("1")) {
            added.add(stringArray[8]);//乐队平台
        }
        if (TheatreDetailInfo.getInstance().getCurtainSystem().equals("1")) {
            added.add(stringArray[9]);//幕布系统
        }
        if (TheatreDetailInfo.getInstance().getSpecialEquipment().equals("1")) {
            added.add(stringArray[10]);//特殊机具
        }
        if (TheatreDetailInfo.getInstance().getProjector().equals("1")) {
            added.add(stringArray[11]);//投影仪
        }
    }
}
