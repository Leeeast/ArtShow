package com.art.huakai.artshow.utils;

import android.content.Context;

/**
 * Created by lining on 16-9-28.
 */
public class PxToDpUtils {
    public static final String width="width";
    public static final String height="height";

    public static int getNumber(int px, Context context,String str){
        int number=0;
        int height =DeviceUtils.getScreenHeight(context);
        int width =DeviceUtils.getScreenWeight(context);
        if("width".equals(str)){
            number=px*width/1920;
        }else{
            number=px*height/1080;
        }
        return number;
    }
}
