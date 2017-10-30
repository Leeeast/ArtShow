package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.ClassifyTypeBean;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.eventbus.TalentInfoChangeEvent;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.ACache;
import com.art.huakai.artshow.utils.CitySelectUtil;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.DoublePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

/**
 * 基本资料Activity
 * Created by lidongliang on 2017/10/14.
 */

public class ResumeBaseActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.tv_graduate_institutions)
    TextView tvGraduateInstitu;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_live_city)
    TextView tvLiveCity;
    @BindView(R.id.tv_ability_type)
    TextView tvAbilityType;
    @BindView(R.id.edt_subsidiary_organ)
    TextView edtSubsidiaryOrgan;
    @BindView(R.id.edt_stature)
    EditText edtStature;
    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.edt_connect_method)
    EditText edtConnectMethod;

    private ShowProgressDialog showProgressDialog;
    private int mRegionId = -1;
    private ACache mACache;
    private ArrayList<ClassifyTypeBean> classifyTypeAdded;
    private TalentDetailInfo talentDetailInfo;
    private long birthdayTime = 0;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_resume_base;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        talentDetailInfo = TalentDetailInfo.getInstance();
        mACache = ACache.get(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.resume_base_data);
        tvSubtitle.setVisibility(View.VISIBLE);

    }

    @Override
    public void setView() {
        if (!TextUtils.isEmpty(TalentDetailInfo.getInstance().getName())) {
            edtUserName.setText(TalentDetailInfo.getInstance().getName());
        }

        String birthday = TextUtils.isEmpty(TalentDetailInfo.getInstance().getBirthday()) ?
                getString(R.string.app_un_fill) :
                DateUtil.transTime(talentDetailInfo.getBirthday(), "yyyy-MM");
        tvBirthday.setText(birthday);

        //tvAbilityType.setText(classifyType);
        edtSubsidiaryOrgan.setText(talentDetailInfo.getAgency());
        tvGraduateInstitu.setText(talentDetailInfo.getSchool());
        if (!TextUtils.isEmpty(talentDetailInfo.getRegionId())) {
            CitySelectUtil.getCity(talentDetailInfo.getRegionId(), new CitySelectUtil.CityDataRequestListener() {
                @Override
                public void onSuccess(String s) {
                    tvLiveCity.setText(s);
                }

                @Override
                public void onFail() {

                }
            });
        }
        edtStature.setText(talentDetailInfo.getHeight());
        edtWeight.setText(talentDetailInfo.getWeight());
        edtConnectMethod.setText(talentDetailInfo.getLinkTel());
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void commitInfo() {
        final String name = edtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast(getString(R.string.tip_resume_name_input));
            return;
        }
        String birthday = tvBirthday.getText().toString().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        try {
            Date date = simpleDateFormat.parse(birthday);
            birthdayTime = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(this, getString(R.string.tip_resume_birthday_input), Toast.LENGTH_SHORT).show();
            return;
        }
        if (classifyTypeAdded == null || classifyTypeAdded.size() <= 0) {
            Toast.makeText(this, getString(R.string.tip_resume_select_birthday), Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < classifyTypeAdded.size(); i++) {
            jsonArray.put(String.valueOf(classifyTypeAdded.get(i).getId()));
        }
        final String classifyIds = jsonArray.toString();
        final String schrool = tvGraduateInstitu.getText().toString().trim();
        final String agency = edtSubsidiaryOrgan.getText().toString().trim();
        if (TextUtils.isEmpty(agency)) {
            Toast.makeText(this, getString(R.string.tip_resume_agency_input), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mRegionId == -1) {
            Toast.makeText(this, getString(R.string.tip_resume_region_city), Toast.LENGTH_SHORT).show();
            return;
        }
        final String regionId = String.valueOf(mRegionId);
        final String weight = edtWeight.getText().toString().trim();
        final String heigth = edtStature.getText().toString().trim();
        final String linkTel = edtConnectMethod.getText().toString().trim();
        if (TextUtils.isEmpty(linkTel)) {
            Toast.makeText(this, getString(R.string.tip_resume_linktel_input), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(talentDetailInfo.getId())) {
            params.put("id", talentDetailInfo.getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("name", name);
        params.put("birthday", String.valueOf(birthdayTime));
        params.put("classifyIds", classifyIds);
        params.put("school", schrool);
        params.put("agency", agency);
        params.put("regionId", regionId);
        params.put("height", heigth);
        params.put("weight", weight);
        params.put("linkTel", linkTel);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_TALENT_DEIT_BASE, params, 58, new RequestUtil.RequestListener() {
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
                        talentDetailInfo.setId(theatreId);
                        talentDetailInfo.setName(name);
                        talentDetailInfo.setBirthday(String.valueOf(birthdayTime));

                        List<Integer> added = new ArrayList<Integer>();
                        for (int i = 0; i < classifyTypeAdded.size(); i++) {
                            added.add(classifyTypeAdded.get(i).getId());
                        }
                        talentDetailInfo.setClassifyIds(added);
                        talentDetailInfo.setSchool(schrool);
                        talentDetailInfo.setAgency(agency);
                        talentDetailInfo.setRegionId(regionId);
                        talentDetailInfo.setWeight(weight);
                        talentDetailInfo.setHeight(heigth);
                        talentDetailInfo.setLinkTel(linkTel);
                        EventBus.getDefault().post(new TalentInfoChangeEvent());
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

    @OnClick(R.id.rly_graduate_institutions)
    public void showSelectDialogGraduate() {
        String[] graduateArray = getResources().getStringArray(R.array.graduate_institutions);
        SinglePicker picker = new SinglePicker(this, graduateArray);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setTextSize(23);

        WheelView.DividerConfig dividerConfig = new WheelView.DividerConfig();
        dividerConfig.setRatio(WheelView.DividerConfig.FILL);
        dividerConfig.setThick(1);
        picker.setDividerConfig(dividerConfig);
        picker.setOnItemPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tvGraduateInstitu.setText(item);
            }
        });
        picker.show();
    }

    @OnClick(R.id.rly_birthday)
    public void showSelectDialogBirthday() {
        final ArrayList<String> firstDataYear = new ArrayList<>();
        final ArrayList<String> secondDataMonth = new ArrayList<>();
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear - 100; year <= currentYear; year++) {
            firstDataYear.add(year + "年");
        }
        for (int month = 1; month <= 12; month++) {
            secondDataMonth.add(month + "月");
        }
        final DoublePicker picker = new DoublePicker(this, firstDataYear, secondDataMonth);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setCanceledOnTouchOutside(false);
        picker.setCycleDisable(true);
        picker.setSelectedIndex(60, 0);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setTextSize(23);

        WheelView.DividerConfig dividerConfig = new WheelView.DividerConfig();
        dividerConfig.setRatio(WheelView.DividerConfig.FILL);
        dividerConfig.setThick(1);
        picker.setDividerConfig(dividerConfig);

        picker.setOnPickListener(new DoublePicker.OnPickListener() {
            @Override
            public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
                String year = firstDataYear.get(selectedFirstIndex);
                String month = secondDataMonth.get(selectedSecondIndex);
                String birthMonth = year.substring(0, year.length() - 1)
                        + "-" +
                        month.substring(0, month.length() - 1);
                tvBirthday.setText(birthMonth);
            }
        });
        picker.show();
    }

    @OnClick(R.id.rly_live_city)
    public void showSelectDialogAddress() {
        //读取缓存
        String timeAddressCache = mACache.getAsString(Constant.TIME_ADDRESS_CACHE);
        if (TextUtils.isEmpty(timeAddressCache)) {
            timeAddressCache = "0";
        }
        long lastTime = Long.parseLong(timeAddressCache);//得到上次保存最新礼物的时间
        long currentTime = System.currentTimeMillis();
        String addressJson = mACache.getAsString(Constant.ADDRESS_CACHE);
        if (!TextUtils.isEmpty(addressJson) && currentTime - lastTime <= Constant.TIME_CACHE) {//如果缓存是新鲜的
            showAddressSelect(addressJson);
            return;
        }
        showProgressDialog.show();
        RequestUtil.request(false, Constant.URL_REGION_LIST, null, 55, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(ResumeBaseActivity.this, getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                long currentTime = System.currentTimeMillis();
                mACache.put(Constant.TIME_ADDRESS_CACHE, String.valueOf(currentTime));
                mACache.put(Constant.ADDRESS_CACHE, obj);
                showAddressSelect(obj);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 现实地区选择Dialog
     */
    public void showAddressSelect(String address) {
        try {
            List<ProvinceBean> provinceBeen = GsonTools.parseDatas(address, ProvinceBean.class);
            final ProvincePicker picker = new ProvincePicker(ResumeBaseActivity.this, provinceBeen);
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

    @OnClick(R.id.rly_ability_type)
    public void showAbilityType() {
        Bundle bundle = new Bundle();
        bundle.putString(ClassifyTypeActivity.CLASSIFY_TYPE, ClassifyTypeActivity.CLASSIFY_TYPE_TALENT);
        invokActivity(this, ClassifyTypeActivity.class, bundle, JumpCode.FLAG_REQ_CLASSIFY_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case JumpCode.FLAG_RES_CLASSIFY_TYPE_CONFIRM:
                if (data != null) {
                    classifyTypeAdded = data.getParcelableArrayListExtra(ClassifyTypeActivity.CLASSIFY_TYPE_CONFIRM);
                    String classifyType = "";
                    for (int i = 0; i < classifyTypeAdded.size(); i++) {
                        if (i == classifyTypeAdded.size() - 1) {
                            classifyType += classifyTypeAdded.get(i).getName();
                        } else {
                            classifyType += classifyTypeAdded.get(i).getName() + "  ";
                        }
                    }
                    tvAbilityType.setText(classifyType);
                }
                break;
        }
    }
}
