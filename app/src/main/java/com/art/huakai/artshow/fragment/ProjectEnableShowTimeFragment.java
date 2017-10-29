package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.TalentResumeInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.eventbus.ProjectPerformTimeEvent;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

public class ProjectEnableShowTimeFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.tv_project_start_time)
    TextView tvProjectStartTime;
    @BindView(R.id.tv_project_end_time)
    TextView tvProjectEndTime;

    public ProjectEnableShowTimeFragment() {
    }

    public static ProjectEnableShowTimeFragment newInstance() {
        ProjectEnableShowTimeFragment fragment = new ProjectEnableShowTimeFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_project_enable_show_time;
    }

    @Override
    public void initView(View rootView) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_able_show_time);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
        tvProjectStartTime.setText(DateUtil.transTime(ProjectDetailInfo.getInstance().getPerformanceBeginDate()));
        tvProjectEndTime.setText(DateUtil.transTime(ProjectDetailInfo.getInstance().getPerformanceEndDate()));
    }

    @OnClick(R.id.lly_back)
    public void back() {
        getActivity().finish();
    }

    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void confirmInfo() {
        final String startTime = DateUtil.transTimestamp(tvProjectStartTime.getText().toString().trim());
        final String entTime = DateUtil.transTimestamp(tvProjectEndTime.getText().toString().trim());
        if (TextUtils.isEmpty(tvProjectStartTime.getText().toString().trim())) {
            showToast(getString(R.string.tip_project_per_start_time));
            return;
        }
        if (!TextUtils.isEmpty(tvProjectEndTime.getText().toString().trim())) {
            if (startTime.compareTo(entTime) > 0) {
                showToast(getString(R.string.tip_project_per_end_time));
                return;
            }
        }
        EventBus.getDefault().post(new ProjectPerformTimeEvent(startTime, entTime));
        getActivity().finish();
    }

    @OnClick(R.id.rly_start_time)
    public void selectStartTime() {
        final DatePicker picker = new DatePicker(getActivity());
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
                tvProjectStartTime.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    @OnClick(R.id.rly_end_time)
    public void selectEndTime() {
        final DatePicker picker = new DatePicker(getActivity());
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
                tvProjectEndTime.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

}
