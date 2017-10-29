package com.art.huakai.artshow.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.ClassifyTypeBean;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.qqtheme.framework.entity.CityBean;
import cn.qqtheme.framework.entity.ProvinceBean;
import okhttp3.Call;

/**
 * Created by lidongliang on 2017/10/28.
 */

public class ClassifySelectUtil {
    private static final String TAG = ClassifySelectUtil.class.getSimpleName();

    public interface ClassifyRequestListener {
        void onSuccess(String s);

        void onFail();
    }

    /**
     * 获取类型字符串
     *
     * @param type
     * @param listener
     */
    public static void getClassifyJson(final String type, final ClassifyRequestListener listener) {
        final ACache mACache = ACache.get(ShowApplication.getAppContext());
        //读取缓存
        String timeAddressCache = mACache.getAsString(Constant.TIME_CLASSIFY_CACHE + type);
        if (TextUtils.isEmpty(timeAddressCache)) {
            timeAddressCache = "0";
        }
        long lastTime = Long.parseLong(timeAddressCache);//得到上次保存最新礼物的时间
        final long currentTime = System.currentTimeMillis();
        String addressJson = mACache.getAsString(Constant.CLASSIFY_CACHE + type);
        if (!TextUtils.isEmpty(addressJson) && currentTime - lastTime <= Constant.TIME_CACHE) {//如果缓存是新鲜的
            listener.onSuccess(addressJson);
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("type", type);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        RequestUtil.request(false, Constant.URL_GET_CLASSFY_LIST, params, 56, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    if (TextUtils.isEmpty(obj)) {
                        Toast.makeText(
                                ShowApplication.getAppContext(),
                                ShowApplication.getAppContext().getString(R.string.tip_data_error),
                                Toast.LENGTH_SHORT).show();
                        listener.onFail();
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    mACache.put(Constant.TIME_CLASSIFY_CACHE + type, String.valueOf(currentTime));
                    mACache.put(Constant.CLASSIFY_CACHE + type, obj);
                    listener.onSuccess(obj);
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                    listener.onFail();
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                listener.onFail();
            }
        });
    }

    /**
     * 根据classifyId获取对应的类型
     *
     * @param classifyId
     * @param classifyRequestListener
     */
    public static void getClassify(String type, final String classifyId, final ClassifyRequestListener classifyRequestListener) {
        getClassifyJson(type, new ClassifyRequestListener() {
            @Override
            public void onSuccess(String s) {
                parseClassifyData(classifyId, classifyRequestListener, s);
            }

            @Override
            public void onFail() {
                classifyRequestListener.onFail();
            }
        });
    }

    private static void parseClassifyData(String classifyId, final ClassifyRequestListener listener, String clssifyJson) {
        try {
            String classify = "";
            List<ClassifyTypeBean> classifyTypeBeans = GsonTools.parseDatas(clssifyJson, ClassifyTypeBean.class);
            for (int i = 0; i < classifyTypeBeans.size(); i++) {
                ClassifyTypeBean provinceB = classifyTypeBeans.get(i);
                classify = provinceB.getName();
                String provinceId = String.valueOf(provinceB.getId());
                if (provinceId.equals(classifyId)) {
                    listener.onSuccess(classify);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
