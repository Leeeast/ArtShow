package com.art.huakai.artshow.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lidongliang on 2017/11/9.
 */

public class AgeUtils {

    public static int getAgeByBirth(String timeTemp) {
        int age = 0;
        try {
            String time = DateUtil.transTime(timeTemp);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(time);
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }
}
