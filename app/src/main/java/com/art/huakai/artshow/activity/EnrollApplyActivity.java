package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EnrollApplyAdapter;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.EnrollDetailInfo;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class EnrollApplyActivity extends BaseActivity implements SmartRecyclerview.LoadingListener {

    public static final String PARAMS_ENROLL_DETAIL = "PARAMS_ENROLL_DETAIL";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.recycle_production)
    SmartRecyclerview recyclerview;

    private EnrollDetailInfo mEnrollDetailInfo;
    private List<RepertorysBean> mRepertorys;
    private List<RepertorysBean> mRepertorysAdded;
    private EnrollApplyAdapter enrollApplyAdapter;
    private int mPage = 1;
    private Map<Object, Object> params;
    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_enroll_apply;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        mRepertorys = new ArrayList<>();
        mRepertorysAdded = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mEnrollDetailInfo = (EnrollDetailInfo) extras.getSerializable(PARAMS_ENROLL_DETAIL);
        }
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.enroll_apply);

        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText(R.string.affirm_info);
    }

    @Override
    public void setView() {

        LinearLayoutManager layManager = new LinearLayoutManager(this);
        layManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layManager);
        enrollApplyAdapter = new EnrollApplyAdapter(mRepertorys, mRepertorysAdded, mEnrollDetailInfo);
        enrollApplyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(ProjectEditActivity.PARAMS_NEW, true);
                invokActivity(EnrollApplyActivity.this, ProjectEditActivity.class, bundle, JumpCode.FLAG_REQ_CREATE_PROJECT_IN_ENROLL);
            }
        });
        recyclerview.setAdapter(enrollApplyAdapter);

        recyclerview.setLoadingListener(this);
        recyclerview.setPullRefreshEnabled(true);
        recyclerview.setLoadingMoreEnabled(true);
        recyclerview.refresh();
    }

    /**
     * 加载剧场数据
     *
     * @param page
     */
    private void loadProjectData(final int page) {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("page", String.valueOf(mPage));
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_USER_REPERTORY, params, 60, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (page == 1) {
                    recyclerview.refreshComplete();
                    mRepertorys.clear();
                } else {
                    recyclerview.loadMoreComplete();
                }
                if (isSuccess) {
                    try {
                        List<RepertorysBean> repertorysBeans = GsonTools.parseDatas(obj, RepertorysBean.class);
                        LogUtil.i(TAG, "mEnrollInfos.size = " + repertorysBeans.size());
                        if (mPage == 1 && (repertorysBeans == null || repertorysBeans.size() == 0)) {
                            mRepertorys.clear();
                        } else if (mPage != 1 && (repertorysBeans == null || repertorysBeans.size() == 0)) {
                            showToast(getString(R.string.tip_no_more_date));
                        }
                        mRepertorys.addAll(repertorysBeans);
                        if (enrollApplyAdapter != null) {
                            enrollApplyAdapter.notifyDataSetChanged();
                        }
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
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    @OnClick(R.id.tv_subtitle)
    public void commitEnroll() {
        if (mRepertorysAdded.size() == 0) {
            showToast(getString(R.string.tip_enroll_select));
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (RepertorysBean repertory : mRepertorysAdded) {
            jsonArray.put(repertory.getId());
        }
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("id", mEnrollDetailInfo.enroll.id);
        params.put("entityIds", jsonArray.toString());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params =" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_ENROLL_ENROLL, params, 33, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.tip_enroll_commit_suc));
                    finish();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadProjectData(mPage);
    }

    @Override
    public void onLoadMore() {
        ++mPage;
        loadProjectData(mPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JumpCode.FLAG_REQ_CREATE_PROJECT_IN_ENROLL) {
            if (recyclerview != null) {
                recyclerview.refresh();
            }
        }
    }
}
