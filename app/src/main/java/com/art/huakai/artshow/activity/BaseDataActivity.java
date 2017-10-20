package com.art.huakai.artshow.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DoublePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 基本资料Activity
 * Created by lidongliang on 2017/10/14.
 */

public class BaseDataActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_graduate_institutions)
    TextView tvGraduateInstitu;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_base_data;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.resume_base_data);
        tvSubtitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(LocalUserInfo.getInstance().getName())) {
            tvUserName.setText(LocalUserInfo.getInstance().getName());
        }
    }

    @Override
    public void setView() {

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
        Toast.makeText(this, "确认信息", Toast.LENGTH_SHORT).show();
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
}
