package com.art.huakai.artshow.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lidongliang on 2017/10/4.
 */

public class SignUtil {
    public static String getSign(Map<String, String> params) {
        if (params != null) {
            Set<String> keySet = params.keySet();
            Iterator<String> iterator = keySet.iterator();
            String sign = "";
            while (iterator.hasNext()) {
                String next = iterator.next();
                String value = params.get(next);
                sign += "&" + next + "=" + value;
            }
            sign = sign + "Yowj10w%";
            return MD5.getMD5(sign.getBytes());
        }
        return null;
    }
}
