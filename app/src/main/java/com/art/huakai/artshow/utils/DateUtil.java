package com.art.huakai.artshow.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lidongliang on 2017/10/29.
 */

public class DateUtil {
    //转成yyyy-MM-dd这种日期格式
    public static String transTime(String time) {
        try {
            if (TextUtils.isEmpty(time) || time.equals("-28800000"))
                return "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(Long.parseLong(time));
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //转成yyyy-MM-dd这种日期格式
    public static String transTime(String time, String yyMMdd) {
        try {
            if (TextUtils.isEmpty(time))
                return "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyMMdd);
            Date date = new Date();
            date.setTime(Long.parseLong(time));
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转成时间戳
     */
    public static String transTimestamp(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long birthdayTime = 0;
        try {
            Date date = simpleDateFormat.parse(time);
            birthdayTime = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return String.valueOf(birthdayTime);
    }

    /**
     * 格式化一下日期格式
     *
     * @param time
     * @return
     */
    public static String formateDate(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long timeTemp = 0;
        try {
            Date date = simpleDateFormat.parse(time);
            timeTemp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transTime(String.valueOf(timeTemp), "yyyy-M-d");
    }
}
