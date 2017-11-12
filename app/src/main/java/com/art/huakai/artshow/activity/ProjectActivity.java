package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.holder.OrgProjectHolder;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectNotifyEvent;
import com.art.huakai.artshow.eventbus.TheatreNotifyEvent;
import com.art.huakai.artshow.listener.OnHolderCallBack;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.adapter.OrgProjectAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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
    private RequestCall requestCall;
    private boolean isNewCreate = false;
    private OrgProjectHolder mHolder;

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
        EventBus.getDefault().register(this);
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
        requestCall = RequestUtil.request(true, Constant.URL_USER_REPERTORY, params, 60, new RequestUtil.RequestListener() {
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
        mOrgProjectAdapter.setOnItemClickListener(new OnHolderCallBack() {
            @Override
            public void onItemClickListener(int position, RecyclerView.ViewHolder holder) {
                isNewCreate = false;
                mHolder = (OrgProjectHolder) holder;
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
        onBackPressed();
    }

    /**
     * 上传剧场
     */
    @OnClick(R.id.tv_subtitle)
    public void uploadTheatre() {
        isNewCreate = true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestCall != null) {
            requestCall.cancel();
            requestCall = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(ProjectNotifyEvent event) {
        if (isNewCreate) {
            return;
        }
        if (event == null) {
            return;
        }
        if (this.isFinishing()) {
            return;
        }
        ProjectDetailInfo p = ProjectDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case ProjectNotifyEvent.NOTIFY_AVATAR:
                mHolder.sdvProject.setImageURI(p.getLogo());
                break;
            case ProjectNotifyEvent.NOTIFY_BASE_INFO:
                mHolder.tvProjectTitle.setText(p.getTitle());
                mHolder.tvActorCount.setText(p.getPeopleNum());
                mHolder.tvFirstShowTime.setText(DateUtil.transTime(p.getPremiereTime(), "yyyy.M.d"));
                String price = String.format(getResources().getString(R.string.me_theatre_price), Integer.valueOf(p.getExpense()));
                mHolder.tvProjectPrice.setText(price);
                break;
            case ProjectNotifyEvent.NOTIFY_SEND:
                if (p.getStatus() == 1) {
                    mHolder.tvProjectStatus.setText(R.string.send_status);
                    mHolder.tvProjectStatus.setTextColor(getResources().getColor(R.color.theatre_send_suc));
                } else {
                    mHolder.tvProjectStatus.setText(R.string.unsend_status);
                    mHolder.tvProjectStatus.setTextColor(getResources().getColor(R.color.theatre_send_fail));
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == JumpCode.FLAG_RES_ADD_PROJECT) {
            ProjectDetailInfo p = ProjectDetailInfo.getInstance();
            if (TextUtils.isEmpty(p.getId())) {
                return;
            }
            try {
                /**
                 * id : 8a999cce5d3237f0015d327125cf0001
                 * logo : https://www.showonline.com.cn/image/2017/07/12/ddbd5716035840629afd7e75786e8a89@thumb.JPG
                 * title : 夜·班
                 * agency : 北京龙百合艺术传媒有限公司
                 * expense : 30000
                 * peopleNum : 8
                 * premiereTime : 1508256000000
                 * classifyId : 14
                 * classifyName : 实验当代
                 * rounds : 35
                 * status : 1
                 * createTime : 1508336509499
                 */
                RepertorysBean r = new RepertorysBean();
                r.setId(p.getId());
                r.setLogo(p.getLogo());
                r.setTitle(p.getTitle());
                r.setExpense(Integer.valueOf(
                        TextUtils.isEmpty(p.getExpense()) ? "0" : p.getExpense()
                ));
                r.setPeopleNum(Integer.valueOf(
                        TextUtils.isEmpty(p.getPeopleNum()) ? "0" : p.getPeopleNum()
                ));
                r.setPremiereTime(Long.valueOf(
                        TextUtils.isEmpty(p.getPremiereTime()) ? "0" : p.getPremiereTime()
                ));
                r.setClassifyId(Integer.valueOf(
                        TextUtils.isEmpty(p.getClassifyId()) ? "0" : p.getPremiereTime()
                ));
                r.setClassifyName(p.getClassifyName());
                r.setRounds(Integer.valueOf(
                        TextUtils.isEmpty(p.getRounds()) ? "0" : p.getRounds()
                ));
                r.setStatus(p.getStatus());
                r.setCreateTime(p.getCreateTime());

                mRepertorys.add(r);
                mOrgProjectAdapter.notifyDataSetChanged();
                recyclerViewProject.scrollToPosition(mRepertorys.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        LocalUserInfo.getInstance().setRepertoryCount(mRepertorys.size());
        finish();
    }
}
