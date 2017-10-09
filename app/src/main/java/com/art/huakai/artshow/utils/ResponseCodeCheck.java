package com.art.huakai.artshow.utils;

import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;

/**
 * Created by lidongliang on 2017/10/3.
 */

public class ResponseCodeCheck {

    //网络请求状态码
    public static final int CODE_200 = 200;//sucess
    public static final int CODE_4000 = 4000;//参数签名错误
    public static final int CODE_4001 = 4001;//令牌过期或无效
    public static final int CODE_4002 = 4002;//系统内部错误
    public static final int CODE_4003 = 4003;//请求参数错误
    public static final int CODE_4004 = 4004;//验证码错误
    public static final int CODE_4005 = 4005;//账号被限制使用
    public static final int CODE_4006 = 4006;//账号或密码错误
    public static final int CODE_4007 = 4007;//手机号已存在
    public static final int CODE_4008 = 4008;//操作频繁，稍候再试
    public static final int CODE_4009 = 4009;//文件类型错误

    /**
     * 检测状态码
     *
     * @param code
     * @return
     */
    public static boolean checkResponseCode(int code) {
        switch (code) {
            case CODE_200:
                return true;
            case CODE_4000:
            case CODE_4001:
            case CODE_4002:
            case CODE_4003:
            case CODE_4004:
            case CODE_4005:
            case CODE_4006:
            case CODE_4007:
            case CODE_4008:
            case CODE_4009:
                return false;
            default:
                return false;
        }
    }

    public static void showErrorMsg(int code) {
        switch (code) {
            case CODE_200:
                //showToast(ShowApplication.getAppContext().getString(R.string.response_code_200));
                break;
            case CODE_4000:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4000));
                break;
            case CODE_4001:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4001));
                break;
            case CODE_4002:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4002));
                break;
            case CODE_4003:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4003));
                break;
            case CODE_4004:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4004));
                break;
            case CODE_4005:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4005));
                break;
            case CODE_4006:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4006));
                break;
            case CODE_4007:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4007));
                break;
            case CODE_4008:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4008));
                break;
            case CODE_4009:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4009));
                break;
            default:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_other));
                break;
        }
    }

    private static void showToast(String toastStr) {
        Toast.makeText(ShowApplication.getAppContext(), toastStr, Toast.LENGTH_SHORT).show();
    }
}
