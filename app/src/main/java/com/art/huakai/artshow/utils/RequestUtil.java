package com.art.huakai.artshow.utils;

import android.text.TextUtils;

import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.okhttp.OkHttpUtils;
import com.art.huakai.artshow.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * 请求封装
 * Created by lidongliang on 2017/10/3.
 */

public class RequestUtil {
    public static final String TAG = "RequestUtil";

    public interface RequestListener {
        void onSuccess(boolean isSuccess, String obj, int code, int id);

        void onFailed(Call call, Exception e, int id);
    }

    /**
     * 请求统一封装
     *
     * @param isPost    是否是post请求
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static void request(boolean isPost, String url, Map<String, String> params, int requestId, RequestListener listener) {
        if (isPost) {
            postRequest(url, params, requestId, listener);
        } else {
            getRequest(url, params, requestId, listener);
        }
    }

    /**
     * post请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static void postRequest(String url, Map<String, String> params, int requestId, final RequestListener listener) {
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .id(requestId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onFailed(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                String data = jsonObject.getString("data");
                                if (ResponseCodeCheck.checkResponseCode(code)) {
                                    listener.onSuccess(true, data, code, id);
                                } else {
                                    listener.onSuccess(false, msg, code, id);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });
    }

    /**
     * get请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static void getRequest(String url, Map<String, String> params, final int requestId, final RequestListener listener) {
        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .id(requestId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e(TAG, e.getMessage() + "-" + id);
                        listener.onFailed(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i(TAG, response + ":id = " + id);
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                String data = jsonObject.getString("data");
                                if (ResponseCodeCheck.checkResponseCode(code)) {
                                    listener.onSuccess(true, data, code, id);
                                } else {
                                    listener.onSuccess(false, msg, code, id);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
