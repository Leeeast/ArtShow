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
import com.art.huakai.artshow.entity.TalentResumeInfo;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

    private Unbinder unbinder;
    private ShowProgressDialog showProgressDialog;

    public ProjectEnableShowTimeFragment() {
    }

    public static ProjectEnableShowTimeFragment newInstance() {
        ProjectEnableShowTimeFragment fragment = new ProjectEnableShowTimeFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_project_enable_show_time;
    }

    @Override
    public void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_able_show_time);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
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

    }

    @OnClick(R.id.rly_start_time)
    public void selectStartTime() {

    }

    @OnClick(R.id.rly_end_time)
    public void selectEndTime() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}