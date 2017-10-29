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
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.eventbus.ProjectPerformTimeEvent;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.CitySelectUtil;
import com.art.huakai.artshow.utils.ClassifySelectUtil;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import butterknife.OnItemClick;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

/**
 * 剧场基本资料
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectBaseActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.edt_project_name)
    EditText edtProjectName;
    @BindView(R.id.edt_project_actor_count)
    EditText edtProjectActorCount;
    @BindView(R.id.tv_project_type)
    TextView tvProjectType;
    @BindView(R.id.tv_live_city)
    TextView tvLiveCity;
    @BindView(R.id.tv_fit_theatre)
    TextView tvfitTheatre;
    @BindView(R.id.tv_first_show_time)
    TextView tvFirstShowTime;
    @BindView(R.id.tv_able_show_time)
    TextView tvAbleShowTime;
    @BindView(R.id.edt_project_show_hour)
    EditText edtProjectShowHour;
    @BindView(R.id.edt_project_show_session)
    EditText edtProjectShowSession;
    @BindView(R.id.edt_project_show_price)
    EditText edtProjectShowPrice;
    @BindView(R.id.edt_connect_name)
    EditText edtLinkMan;
    @BindView(R.id.edt_connect_phone)
    EditText edtLinkTel;

    private ShowProgressDialog showProgressDialog;
    private ProjectDetailInfo projectDetailInfo;
    private ArrayList<ClassifyTypeBean> classifyTypeAdded;
    private String classifyId;
    private String mRegionId;
    private String mFitTheatre;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_project_base;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        projectDetailInfo = ProjectDetailInfo.getInstance();
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
        edtProjectName.setText(projectDetailInfo.getTitle());
        //tvProjectType.setText(projectDetailInfo.setpr);
        edtProjectActorCount.setText(projectDetailInfo.getPeopleNum());
        edtProjectShowHour.setText(projectDetailInfo.getShowLast());
        edtProjectShowSession.setText(projectDetailInfo.getRounds());
        edtProjectShowPrice.setText(projectDetailInfo.getExpense());
        edtLinkMan.setText(projectDetailInfo.getLinkman());
        edtLinkTel.setText(projectDetailInfo.getLinkTel());

        updateEditUI();
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
    public void confirmInfo() {
        final String title = edtProjectName.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showToast(getString(R.string.tip_project_input_title));
            return;
        }
        if (TextUtils.isEmpty(classifyId)) {
            showToast(getString(R.string.tip_project_input_classify));
            return;
        }
        final String peopleNum = edtProjectActorCount.getText().toString().trim();
        if (TextUtils.isEmpty(peopleNum)) {
            showToast(getString(R.string.tip_project_input_people_num));
            return;
        }
        if (TextUtils.isEmpty(mRegionId)) {
            showToast(getString(R.string.tip_project_input_city));
            return;
        }
        if (TextUtils.isEmpty(mFitTheatre)) {
            showToast(getString(R.string.tip_project_input_fit));
            return;
        }
        final String showLast = edtProjectShowHour.getText().toString().trim();
        if (TextUtils.isEmpty(showLast)) {
            showToast(getString(R.string.tip_project_input_show_last));
            return;
        }
        final String firstShowTime = tvFirstShowTime.getText().toString().trim();
        if (TextUtils.isEmpty(firstShowTime)) {
            showToast(getString(R.string.tip_project_input_first_show));
            return;
        }
        final String showRounds = edtProjectShowSession.getText().toString().trim();
        final String expense = edtProjectShowPrice.getText().toString().trim();
        if (TextUtils.isEmpty(expense)) {
            showToast(getString(R.string.tip_project_input_show_expense));
            return;
        }
        final String linkMan = edtLinkMan.getText().toString().trim();
        if (TextUtils.isEmpty(linkMan)) {
            showToast(getString(R.string.tip_theatre_input_linkman));
            return;
        }
        final String linkTel = edtLinkTel.getText().toString().trim();
        if (TextUtils.isEmpty(linkTel)) {
            showToast(getString(R.string.tip_theatre_input_linktel));
            return;
        }
        if (TextUtils.isEmpty(mStartTime)) {
            showToast(getString(R.string.tip_project_per_start_time));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(projectDetailInfo.getId())) {
            params.put("id", projectDetailInfo.getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("title", title);
        params.put("classifyId", classifyId);
        params.put("peopleNum", peopleNum);
        params.put("regionId", mRegionId);
        params.put("seatingRequir", mFitTheatre);
        params.put("showLast", showLast);
        params.put("premiereTime", DateUtil.transTimestamp(firstShowTime));
        params.put("rounds", showRounds);
        params.put("expense", expense);
        params.put("linkman", linkMan);
        params.put("linkTel", linkTel);
        params.put("performanceBeginDate", mStartTime);
        params.put("performanceEndDate", mEndTime);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params =" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_EDIT_BASE, params, 80, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.tip_theatre_des_change_suc));
                        //{"id":"8a999cce5f5da93b015f5f338d0a0020"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String projectId = jsonObject.getString("id");
                        projectDetailInfo.setId(projectId);
                        projectDetailInfo.setTitle(title);
                        projectDetailInfo.setClassifyId(classifyId);
                        projectDetailInfo.setPeopleNum(peopleNum);
                        projectDetailInfo.setRegionId(mRegionId);
                        projectDetailInfo.setSeatingRequir(mFitTheatre);
                        projectDetailInfo.setShowLast(showLast);
                        projectDetailInfo.setPremiereTime(DateUtil.transTimestamp(firstShowTime));
                        projectDetailInfo.setRounds(showRounds);
                        projectDetailInfo.setExpense(expense);
                        projectDetailInfo.setLinkman(linkMan);
                        projectDetailInfo.setLinkTel(linkTel);
                        projectDetailInfo.setPerformanceBeginDate(mStartTime);
                        projectDetailInfo.setPerformanceEndDate(mEndTime);
                        EventBus.getDefault().post(new ProjectInfoChangeEvent());
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

    /**
     * 选择可演出时间
     */
    @OnClick(R.id.rly_able_show_time)
    public void selectEnableShowTime() {
        Bundle bundle = new Bundle();
        bundle.putInt(ProjectFillActivity.PARAMS_ACTION, ProjectFillActivity.CODE_ACTION_PROJECT_ENABLE_SHOW_TIME);
        invokActivity(this, ProjectFillActivity.class, bundle, JumpCode.FLAG_REQ_PROJECT_FILL);
    }

    @OnClick(R.id.rly_project_type)
    public void selectProjectType() {
        showProgressDialog.show();
        ClassifySelectUtil.getClassifyJson(ClassifyTypeActivity.CLASSIFY_TYPE_REPERTORY, new ClassifySelectUtil.ClassifyRequestListener() {
            @Override
            public void onSuccess(String s) {
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showClassifySelect(s);
            }

            @Override
            public void onFail() {
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 现实类型选择
     */
    public void showClassifySelect(String classify) {
        try {
            final List<ClassifyTypeBean> classifyTypeBeanList = GsonTools.parseDatas(classify, ClassifyTypeBean.class);
            SinglePicker picker = new SinglePicker(ProjectBaseActivity.this, classifyTypeBeanList);
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
            picker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
                @Override
                public void onItemPicked(int index, Object item) {
                    ClassifyTypeBean classifyTypeBean = classifyTypeBeanList.get(index);
                    tvProjectType.setText(classifyTypeBean.getName());
                    classifyId = String.valueOf(classifyTypeBean.getId());
                }
            });
            picker.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rly_live_city)
    public void selectLiveCity() {
        showProgressDialog.show();
        CitySelectUtil.getCityJson(new CitySelectUtil.CityDataRequestListener() {
            @Override
            public void onSuccess(String s) {
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showAddressSelect(s);
            }

            @Override
            public void onFail() {
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
            final ProvincePicker picker = new ProvincePicker(ProjectBaseActivity.this, provinceBeen);
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
                    mRegionId = String.valueOf(regionId);
                }
            });
            picker.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rly_fit_theatre)
    public void selectFitTheatre() {
        try {
            String[] stringArray = getResources().getStringArray(R.array.project_fit_theatre);
            SinglePicker<String> picker = new SinglePicker<String>(this, stringArray);
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
            picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
                @Override
                public void onItemPicked(int index, String item) {
                    tvfitTheatre.setText(item);
                    if (index == 0) {
                        mFitTheatre = "300";
                    } else if (index == 1) {
                        mFitTheatre = "600";
                    } else if (index == 2) {
                        mFitTheatre = "1200";
                    } else if (index == 3) {
                        mFitTheatre = "1600";
                    }
                }
            });
            picker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rly_first_show_time)
    public void firstShowTime() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setCycleDisable(true);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setTextSize(23);

        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int yearStart = c.get(Calendar.YEAR);
        int yearEnd = yearStart + 1;
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);

        picker.setRangeEnd(yearEnd, month, date);
        picker.setRangeStart(yearStart, month, date);
        picker.setSelectedItem(yearStart, month, date);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvFirstShowTime.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    /**
     * 项目信息更新，通知页面变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProjectChange(ProjectInfoChangeEvent event) {
        if (event == null) {
            return;
        }
        updateEditUI();
    }

    /**
     * 剧场信息更新，通知页面变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPerforTime(ProjectPerformTimeEvent event) {
        if (event == null) {
            return;
        }
        mStartTime = event.getStartTime();
        mEndTime = event.getEndTime();
        String perforTime = DateUtil.transTime(mStartTime) + "~" + DateUtil.transTime(mEndTime);
        tvAbleShowTime.setText(perforTime);
    }

    /**
     * 更新页面数据
     */
    public void updateEditUI() {
        String firstShowTime = TextUtils.isEmpty(projectDetailInfo.getPremiereTime()) ?
                getString(R.string.app_un_fill) :
                DateUtil.transTime(projectDetailInfo.getPremiereTime());
        tvFirstShowTime.setText(firstShowTime);

        String enableShowTime = TextUtils.isEmpty(projectDetailInfo.getPerformanceBeginDate()) ?
                getString(R.string.app_un_fill) :
                DateUtil.transTime(projectDetailInfo.getPerformanceBeginDate()) + "~" +
                        DateUtil.transTime(projectDetailInfo.getPerformanceEndDate());
        tvAbleShowTime.setText(enableShowTime);

        if (!TextUtils.isEmpty(projectDetailInfo.getRegionId())) {
            mRegionId = projectDetailInfo.getRegionId();
            CitySelectUtil.getCity(projectDetailInfo.getRegionId(), new CitySelectUtil.CityDataRequestListener() {
                @Override
                public void onSuccess(String s) {
                    tvLiveCity.setText(s);
                }

                @Override
                public void onFail() {

                }
            });
        }
        if (!TextUtils.isEmpty(projectDetailInfo.getClassifyId())) {
            classifyId = projectDetailInfo.getClassifyId();
            ClassifySelectUtil.getClassify(ClassifyTypeActivity.CLASSIFY_TYPE_REPERTORY, projectDetailInfo.getClassifyId(), new ClassifySelectUtil.ClassifyRequestListener() {
                @Override
                public void onSuccess(String s) {
                    tvProjectType.setText(s);
                }

                @Override
                public void onFail() {

                }
            });
        }
        String theatreFit = "";
        String[] stringArray = getResources().getStringArray(R.array.project_fit_theatre);
        if (!TextUtils.isEmpty(projectDetailInfo.getSeatingRequir())) {
            mFitTheatre = projectDetailInfo.getSeatingRequir();
            switch (projectDetailInfo.getSeatingRequir()) {
                case "300":
                    theatreFit = stringArray[0];
                    break;
                case "600":
                    theatreFit = stringArray[1];
                    break;
                case "1200":
                    theatreFit = stringArray[2];
                    break;
                case "1600":
                    theatreFit = stringArray[3];
                    break;
            }
            tvfitTheatre.setText(theatreFit);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
