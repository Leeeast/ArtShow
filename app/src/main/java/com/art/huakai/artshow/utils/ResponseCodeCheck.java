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
    public static final int CODE_240 = 240;//账户不存在
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
    public static final int CODE_4010 = 4010;//重复报名
    public static final int CODE_4011 = 4011;//账号未认证
    public static final int CODE_4012 = 4012;//账号不存在
    public static final int CODE_4013 = 4013;//资源不存在
    public static final int CODE_4014 = 4014;//越权操作
    public static final int CODE_4015 = 4015;//账号信息不完整
    public static final int CODE_4016 = 4016;//资源信息不完整
    public static final int CODE_4017 = 4017;//帐号审核中
    public static final int CODE_4018 = 4018;//帐号审核未通过
    public static final int CODE_4019 = 4019;//手机号已绑定其他微信帐号
    public static final int CODE_4020 = 4020;//微信已经绑定其手机号
    private static Toast toast;

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
            case CODE_240:
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
            case CODE_4010:
            case CODE_4011:
            case CODE_4012:
            case CODE_4013:
            case CODE_4014:
            case CODE_4015:
            case CODE_4016:
            case CODE_4017:
            case CODE_4018:
            case CODE_4019:
            case CODE_4020:
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
            case CODE_240:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_240));
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
            case CODE_4010:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4010));
                break;
            case CODE_4011:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4011));
                break;
            case CODE_4012:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4012));
                break;
            case CODE_4013:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4013));
                break;
            case CODE_4014:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4014));
                break;
            case CODE_4015:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4015));
                break;
            case CODE_4016:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4016));
                break;
            case CODE_4017:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4017));
                break;
            case CODE_4018:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4018));
                break;
            case CODE_4019:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4019));
                break;
            case CODE_4020:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_4020));
                break;
            default:
                showToast(ShowApplication.getAppContext().getString(R.string.response_code_other));
                break;
        }
    }

    private static void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(ShowApplication.getAppContext(), toastStr, Toast.LENGTH_SHORT);
        } else {
            toast.setText(toastStr);
        }
        toast.show();
    }
}
