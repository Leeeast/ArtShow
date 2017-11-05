package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.CitySelectUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

/**
 * 剧场基本资料
 * Created by lidongliang on 2017/10/14.
 */

public class TheatreBaseActivity extends BaseActivity {

    public static final String RESULT_ADDRESS_NAME = "RESULT_ADDRESS_NAME";
    public static final String RESULT_ADDRESS_LATITUDE = "RESULT_ADDRESS_LATITUDE";
    public static final String RESULT_ADDRESS_LONGITUDE = "RESULT_ADDRESS_LONGITUDE";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.edt_theatre_name)
    EditText edtTheatreName;
    @BindView(R.id.edt_theatre_hall_name)
    EditText edtTheatreHallName;
    @BindView(R.id.edt_theatre_seat_count)
    EditText edtTheatreSeatCount;
    @BindView(R.id.edt_theatre_colocation_price)
    EditText edtTheatreColocationPrice;
    @BindView(R.id.edt_theatre_detail_address)
    EditText edtTheatreDetailAddress;
    @BindView(R.id.edt_connect_name)
    EditText edtConnectName;
    @BindView(R.id.edt_connect_phone)
    EditText edtConnectPhone;
    @BindView(R.id.tv_live_city)
    TextView tvLiveCity;

    private ShowProgressDialog showProgressDialog;
    private TheatreDetailInfo theatreInstance;
    private int mRegionId = -1;
    private String mAddressName;
    private String mAddressLatitude;
    private String mAddressLongitude;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_base;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        theatreInstance = TheatreDetailInfo.getInstance();
    }


    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_data_base);

        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText(R.string.affirm_info);
    }

    @Override
    public void setView() {
        fillView();
    }

    /**
     * 填充数据
     */
    private void fillView() {
        edtTheatreName.setText(theatreInstance.getName());
        edtTheatreHallName.setText(theatreInstance.getRoomName());
        edtTheatreSeatCount.setText(theatreInstance.getSeating());
        edtTheatreColocationPrice.setText(theatreInstance.getExpense());
        edtConnectName.setText(theatreInstance.getLinkman());
        edtConnectPhone.setText(theatreInstance.getLinkTel());
        edtTheatreDetailAddress.setText(theatreInstance.getAddress());
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    @OnClick(R.id.rly_live_city)
    public void selectCity() {
        invokActivity(this, PioSearchActivity.class, null, JumpCode.FLAG_REQ_ADDRESS_SELECT);
//        showProgressDialog.show();
//        CitySelectUtil.getCityJson(new CitySelectUtil.CityDataRequestListener() {
//            @Override
//            public void onSuccess(String s) {
//                showProgressDialog.dismiss();
//                showAddressSelect(s);
//            }
//
//            @Override
//            public void onFail() {
//                showProgressDialog.dismiss();
//            }
//        });
    }

    /**
     * 现实地区选择Dialog
     */
    public void showAddressSelect(String address) {
        try {
            List<ProvinceBean> provinceBeen = GsonTools.parseDatas(address, ProvinceBean.class);
            final ProvincePicker picker = new ProvincePicker(TheatreBaseActivity.this, provinceBeen);
            picker.setDividerRatio(WheelView.DividerConfig.FILL);
            picker.setCanceledOnTouchOutside(false);
            picker.setCycleDisable(true);
            picker.setSelectedIndex(0);
            picker.setAnimationStyle(R.style.Animation_CustomPopup);
            picker.setTextSize(23);

            WheelView.DividerConfig dividerConfig = new WheelView.DividerConfig();
            dividerConfig.setRatio(WheelView.DividerConfig.FILL);
            dividerConfig.setThick(1);
            picker.setDividerConfig(dividerConfig);
            picker.setOnItemPickListener(new ProvincePicker.OnItemProvincePickListener() {
                @Override
                public void onPicked(String province, String city, int regionId) {
                    tvLiveCity.setText(province + "  " + city);
                    mRegionId = regionId;
                }
            });
            picker.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void confirmInfo() {
        final String theatreName = edtTheatreName.getText().toString().trim();
        if (TextUtils.isEmpty(theatreName)) {
            showToast(getString(R.string.tip_theatre_input_name));
            return;
        }
        final String theatreHallName = edtTheatreHallName.getText().toString().trim();
        if (TextUtils.isEmpty(theatreHallName)) {
            showToast(getString(R.string.tip_theatre_input_hall));
            return;
        }
        final String theatreSeatCount = edtTheatreSeatCount.getText().toString().trim();
        if (TextUtils.isEmpty(theatreSeatCount)) {
            showToast(getString(R.string.tip_theatre_input_seat));
            return;
        }
        final String theatreColoPrice = edtTheatreColocationPrice.getText().toString().trim();
        if (TextUtils.isEmpty(theatreColoPrice)) {
            showToast(getString(R.string.tip_theatre_input_price));
            return;
        }
        if (TextUtils.isEmpty(mAddressLatitude) || TextUtils.isEmpty(mAddressLongitude)) {
            showToast(getString(R.string.tip_resume_region_city));
            return;
        }
        final String theatreDetailAddress = edtTheatreDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(theatreDetailAddress)) {
            showToast(getString(R.string.tip_theatre_input_detail_address));
            return;
        }
        final String theatreConnectName = edtConnectName.getText().toString().trim();
        if (TextUtils.isEmpty(theatreConnectName)) {
            showToast(getString(R.string.tip_theatre_input_linkman));
            return;
        }
        final String theatreConnectPhone = edtConnectPhone.getText().toString().trim();
        if (TextUtils.isEmpty(theatreConnectPhone)) {
            showToast(getString(R.string.tip_theatre_input_linktel));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (theatreInstance.getId() != null) {
            params.put("id", theatreInstance.getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("name", theatreName);
        params.put("roomName", theatreHallName);
        params.put("seating", theatreSeatCount);
        //params.put("regionId", String.valueOf(mRegionId));
        params.put("coordinate", mAddressLongitude + "," + mAddressLatitude);
        params.put("address", theatreDetailAddress);
        params.put("expense", theatreColoPrice);
        params.put("linkman", theatreConnectName);
        params.put("linkTel", theatreConnectPhone);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_THEATER_EDIT_BASE, params, 66, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.tip_theatre_info_commit_success));
                        //{"id":"8a999cce5f5da93b015f5f338d0a0020"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String theatreId = jsonObject.getString("id");
                        theatreInstance.setId(theatreId);
                        theatreInstance.setName(theatreName);
                        theatreInstance.setRoomName(theatreHallName);
                        theatreInstance.setSeating(theatreSeatCount);
                        theatreInstance.setRegionId(String.valueOf(mRegionId));
                        theatreInstance.setAddress(theatreDetailAddress);
                        theatreInstance.setExpense(theatreColoPrice);
                        theatreInstance.setLinkman(theatreConnectName);
                        theatreInstance.setLinkTel(theatreConnectPhone);

                        EventBus.getDefault().post(new TheatreInfoChangeEvent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == JumpCode.FLAG_RES_ADDRESS_RESULT) {
            if (data == null) {
                return;
            }
            mAddressName = data.getStringExtra(RESULT_ADDRESS_NAME);
            mAddressLatitude = data.getStringExtra(RESULT_ADDRESS_LATITUDE);
            mAddressLongitude = data.getStringExtra(RESULT_ADDRESS_LONGITUDE);

            edtTheatreDetailAddress.setText(mAddressName);
        }
    }
}
