package com.art.huakai.artshow.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Device Utils
 * Author: fengwx
 * Date: 13-10-31
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";


    /**
     * Get version code
     *
     * @param ctx
     * @return
     */
    public static int getVersionCode(Context ctx) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Throwable t) {
            return 0;
        }
    }

    /**
     * Get version name
     *
     * @param ctx
     * @return
     */
    public static String getVersionName(Context ctx) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Throwable t) {
            return "";
        }
    }

    /**
     * Ger os version info
     *
     * @return
     */
    public static String getOSVersionInfo() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * Get dns
     *
     * @return
     */
    public static String getDNS() {
        String dns = "";
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            Properties pr = new Properties();
            pr.load(process.getInputStream());
            dns = pr.getProperty("[net.dns1]", "");
            if (!TextUtils.isEmpty(dns) && dns.length() > 6) {
                dns = dns.substring(1, dns.length() - 1);
                return dns;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dns;
    }

    public static String getRunningPkgName(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
            if (runningTaskInfos != null && !runningTaskInfos.isEmpty()) {
                ComponentName componentName = runningTaskInfos.get(0).topActivity;
                Log.d(TAG, componentName.toString());
                return componentName.getPackageName();
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static String getRunningClassName(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
            if (runningTaskInfos != null && !runningTaskInfos.isEmpty()) {
                ComponentName componentName = runningTaskInfos.get(0).topActivity;
                Log.d(TAG, componentName.toString());
                return componentName.getClassName();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    public static ResolveInfo getIntentMessage(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_ALL);
        if (resolveInfo.size() > 0) {
            return resolveInfo.get(0);
        }
        return null;
    }

    public static boolean checkIsInApplication(Context context) {
        if (context == null) {
            return false;
        }
        String runPackageName = getRunningPkgName(context);
        return !TextUtils.isEmpty(runPackageName) && runPackageName.equals(context.getPackageName());
    }



    public static int getScreenHeight(Context context){

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;

    }

    public static int getScreenWeight(Context context){

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;

    }






}

