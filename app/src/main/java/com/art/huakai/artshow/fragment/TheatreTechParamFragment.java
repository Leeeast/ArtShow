package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.ClassifyTypeSmallAdapter;
import com.art.huakai.artshow.adapter.TechParamsAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TalentResumeInfo;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class TheatreTechParamFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private ShowProgressDialog showProgressDialog;
    private String mDescription;
    private ArrayList<String> techParams;
    private ArrayList<String> techParamsAdded;

    public TheatreTechParamFragment() {
    }

    public static TheatreTechParamFragment newInstance() {
        TheatreTechParamFragment fragment = new TheatreTechParamFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        mDescription = TalentResumeInfo.getInstance().getDescription();
        techParams = new ArrayList<>();
        techParamsAdded = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.theatre_tech_param);
        techParams.addAll(Arrays.asList(stringArray));
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_theatre_tech_param;
    }

    @Override
    public void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_technical_parameters);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
        TechParamsAdapter techParamsAdapter = new TechParamsAdapter(techParams, techParamsAdded);
        GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(
                3,
                GridLayoutManager.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_20PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(gridLayoutItemDecorationone);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(techParamsAdapter);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
