package com.art.huakai.artshow.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.TheatreBaseActivity;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.constant.Constant;

import java.util.List;

import cn.qqtheme.framework.entity.CityBean;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

/**
 * Created by lidongliang on 2017/10/28.
 */

public class CitySelectUtil {
    private static final String TAG = CitySelectUtil.class.getSimpleName();

    public interface CityDataRequestListener {
        void onSuccess(String s);

        void onFail();
    }

    public static void getCityJson(final CityDataRequestListener listener) {
        final ACache mACache = ACache.get(ShowApplication.getAppContext());
        //读取缓存
        String timeAddressCache = mACache.getAsString(Constant.TIME_ADDRESS_CACHE);
        if (TextUtils.isEmpty(timeAddressCache)) {
            timeAddressCache = "0";
        }
        long lastTime = Long.parseLong(timeAddressCache);//得到上次保存最新礼物的时间
        long currentTime = System.currentTimeMillis();
        String addressJson = mACache.getAsString(Constant.ADDRESS_CACHE);
        if (!TextUtils.isEmpty(addressJson) && currentTime - lastTime <= Constant.TIME_CACHE) {//如果缓存是新鲜的
            listener.onSuccess(addressJson);
            return;
        }
        RequestUtil.request(false, Constant.URL_REGION_LIST, null, 55, new RequestUtil.RequestListener() {
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
                    mACache.put(Constant.TIME_ADDRESS_CACHE, String.valueOf(currentTime));
                    mACache.put(Constant.ADDRESS_CACHE, obj);
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

    public static void getCity(final String regionId, final CityDataRequestListener listener) {
        final ACache mACache = ACache.get(ShowApplication.getAppContext());
        //读取缓存
        String timeAddressCache = mACache.getAsString(Constant.TIME_ADDRESS_CACHE);
        if (TextUtils.isEmpty(timeAddressCache)) {
            timeAddressCache = "0";
        }
        long lastTime = Long.parseLong(timeAddressCache);//得到上次保存最新礼物的时间
        long currentTime = System.currentTimeMillis();
        String addressJson = mACache.getAsString(Constant.ADDRESS_CACHE);
        if (!TextUtils.isEmpty(addressJson) && currentTime - lastTime <= Constant.TIME_CACHE) {//如果缓存是新鲜的
            parseCityData(regionId, listener, addressJson);
            return;
        }
        RequestUtil.request(false, Constant.URL_REGION_LIST, null, 55, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(
                            ShowApplication.getAppContext(),
                            ShowApplication.getAppContext().getString(R.string.tip_data_error),
                            Toast.LENGTH_SHORT).show();
                    listener.onFail();
                    return;
                }
                long currentTime = System.currentTimeMillis();
                mACache.put(Constant.TIME_ADDRESS_CACHE, String.valueOf(currentTime));
                mACache.put(Constant.ADDRESS_CACHE, obj);
                listener.onSuccess(obj);
                parseCityData(regionId, listener, obj);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                listener.onFail();
            }
        });
    }

    private static void parseCityData(String regionId, final CityDataRequestListener listener, String addressJson) {
        try {
            String province = "";
            String city = "";
            List<ProvinceBean> provinceBeen = GsonTools.parseDatas(addressJson, ProvinceBean.class);
            for (int i = 0; i < provinceBeen.size(); i++) {
                ProvinceBean provinceB = provinceBeen.get(i);
                province = provinceB.getName();
                String provinceId = String.valueOf(provinceB.getId());
                if (provinceId.equals(regionId)) {
                    listener.onSuccess(province + "  " + city);
                    return;
                }
                List<CityBean> children = provinceB.getChildren();
                if (children != null) {
                    for (int j = 0; j < children.size(); j++) {
                        CityBean cityBean = children.get(j);
                        city = cityBean.getName();
                        String cityId = String.valueOf(cityBean.getId());
                        if (cityId.equals(regionId)) {
                            listener.onSuccess(province + "  " + city);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
