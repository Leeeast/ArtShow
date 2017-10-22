package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.CityBean;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 双项选择器，选择两项，数据不联动。
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/5/1 8:34
 * Builder:Android Studio
 */
public class ProvincePicker extends WheelPicker {
    private List<ProvinceBean> provinces = new ArrayList<>();
    private List<CityBean> cityBeanList;
    private int selectedFirstIndex = 0, selectedSecondIndex = 0;
    private OnItemProvincePickListener onItemPickListener;
    private String mProvince, mCity;
    private int mRegionId;

    public ProvincePicker(Activity activity, List<ProvinceBean> provinces) {
        super(activity);
        this.provinces = provinces;
    }

    public void setSelectedIndex(int firstIndex) {
        setSelectedIndex(firstIndex, 0);
    }

    public void setSelectedIndex(int firstIndex, int secondIndex) {
        selectedFirstIndex = firstIndex;
        selectedSecondIndex = secondIndex;
    }

    @NonNull
    public ProvinceBean getSelectedProvince() {
        return provinces.get(selectedFirstIndex);
    }

    @Nullable
    public CityBean getSelectedCity() {
        List<CityBean> cities = getSelectedProvince().getChildren();
        if (cities.size() == 0) {
            return null;//可能没有第二级数据
        }
        return cities.get(selectedSecondIndex);
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView firstView = createWheelView();
        firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(firstView);

        final WheelView secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(secondView);

        firstView.setItems(provinces, selectedFirstIndex);
        cityBeanList = provinces.get(0).getChildren();
        if (cityBeanList == null || cityBeanList.size() <= 0) {
            cityBeanList = new ArrayList<>();
        }
        secondView.setItems(cityBeanList, 0);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {

            @Override
            public void onSelected(int index) {
                setSelectedIndex(index);
                cityBeanList = getSelectedProvince().getChildren();
                if (cityBeanList == null || cityBeanList.size() <= 0) {
                    cityBeanList = new ArrayList<>();
                }
                secondView.setItems(cityBeanList, 0);
            }
        });
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onItemPickListener != null) {
            mProvince = getSelectedProvince().getName();
            mCity = getSelectedCity().getName();
            mRegionId = (int) (getSelectedCity() == null ? getSelectedProvince().getId() : getSelectedCity().getId());
            onItemPickListener.onPicked(mProvince, mCity, mRegionId);
        }
    }

    /**
     * 设置确认选择监听器
     */
    public void setOnItemPickListener(OnItemProvincePickListener listener) {
        this.onItemPickListener = listener;
    }

    public interface OnItemProvincePickListener {
        void onPicked(String province, String city, int regionId);
    }
}
