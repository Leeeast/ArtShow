package com.art.huakai.artshow.utils;

import java.io.UnsupportedEncodingException;
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
            try {
                sign = sign + "Yowj1Ow%";
                return MD5.getMD5(sign.getBytes());
                //return MD5.md5(sign);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
