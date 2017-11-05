package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.adapter.OrgProjectAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class ProjectActivity extends BaseActivity implements SmartRecyclerview.LoadingListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview_project)
    SmartRecyclerview recyclerViewProject;

    private int mPage = 1;
    private ShowProgressDialog showProgressDialog;
    private List<RepertorysBean> mRepertorys;
    private OrgProjectAdapter mOrgProjectAdapter;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_project;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        mRepertorys = new ArrayList<>();
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
                    recyclerViewProject.refreshComplete();
                    mRepertorys.clear();
                } else {
                    recyclerViewProject.loadMoreComplete();
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
                        if (mOrgProjectAdapter != null) {
                            mOrgProjectAdapter.notifyDataSetChanged();
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

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_title_my);
        tvSubtitle.setVisibility(View.VISIBLE);
        tvSubtitle.setText(R.string.project_upload);

    }

    @Override
    public void setView() {
        LinearLayoutManager layManager = new LinearLayoutManager(this);
        layManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProject.setLayoutManager(layManager);
        mOrgProjectAdapter = new OrgProjectAdapter(mRepertorys);
        recyclerViewProject.setAdapter(mOrgProjectAdapter);
        mOrgProjectAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                RepertorysBean repertorys = mRepertorys.get(position);
                ProjectDetailInfo.getInstance().setId(repertorys.getId());
                Bundle bundle = new Bundle();
                bundle.putString(WorksDetailMessageActivity.PARAMS_ID, repertorys.getId());
                bundle.putBoolean(WorksDetailMessageActivity.PARAMS_ORG, true);
                invokActivity(ProjectActivity.this, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
            }
        });
        recyclerViewProject.setLoadingListener(this);
        recyclerViewProject.setPullRefreshEnabled(true);
        recyclerViewProject.setLoadingMoreEnabled(true);
        recyclerViewProject.refresh();
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 上传剧场
     */
    @OnClick(R.id.tv_subtitle)
    public void uploadTheatre() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProjectEditActivity.PARAMS_NEW, true);
        invokActivity(this, ProjectEditActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_EDIT);
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
}
