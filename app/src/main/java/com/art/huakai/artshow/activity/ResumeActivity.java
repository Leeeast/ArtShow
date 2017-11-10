package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.OrgTalentAdapter;
import com.art.huakai.artshow.adapter.holder.OrgTalentHolder;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TalentBean;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.eventbus.TalentNotifyEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 简历列表activity
 * Created by lidongliang on 2017/10/14.
 */

public class ResumeActivity extends BaseActivity implements SmartRecyclerview.LoadingListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview_resume)
    SmartRecyclerview recyclerViewResume;

    private int mPage = 1;
    private ShowProgressDialog showProgressDialog;
    private List<TalentBean> mTalentBeans;
    private OrgTalentAdapter mOrgTalentAdapter;
    private RequestCall requestCall;
    private OrgTalentHolder mHolder;
    private boolean mIsNewCreate = false;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_resume;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        mTalentBeans = new ArrayList<>();
    }

    /**
     * 加人才简历列表数据
     *
     * @param page
     */
    private void loadTalentData(final int page) {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("page", String.valueOf(mPage));
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        requestCall = RequestUtil.request(true, Constant.URL_USER_TALENT, params, 60, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (page == 1) {
                    recyclerViewResume.refreshComplete();
                    mTalentBeans.clear();
                } else {
                    recyclerViewResume.loadMoreComplete();
                }
                if (isSuccess) {
                    try {
                        List<TalentBean> talents = GsonTools.parseDatas(obj, TalentBean.class);
                        LogUtil.i(TAG, "talents.size = " + talents.size());
                        if (mPage == 1 && (talents == null || talents.size() == 0)) {
                            mTalentBeans.clear();
                        } else if (mPage != 1 && (talents == null || talents.size() == 0)) {
                            showToast(getString(R.string.tip_no_more_date));
                        }
                        mTalentBeans.addAll(talents);
                        if (mOrgTalentAdapter != null) {
                            mOrgTalentAdapter.notifyDataSetChanged();
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
        tvTitle.setText(R.string.talent_title_my);
        tvSubtitle.setVisibility(View.VISIBLE);
        tvSubtitle.setText(R.string.talent_upload_new);
    }

    @Override
    public void setView() {
        LinearLayoutManager layManager = new LinearLayoutManager(this);
        layManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewResume.setLayoutManager(layManager);
        mOrgTalentAdapter = new OrgTalentAdapter(mTalentBeans);
        recyclerViewResume.setAdapter(mOrgTalentAdapter);
        mOrgTalentAdapter.setOnItemClickListener(new OrgTalentAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, OrgTalentHolder holder) {
                mIsNewCreate = false;
                mHolder = holder;
                TalentBean talentBean = mTalentBeans.get(position);
                TalentDetailInfo.getInstance().setId(talentBean.getId());
                Bundle bundle = new Bundle();
                bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, talentBean.getId());
                bundle.putBoolean(PersonalDetailMessageActivity.PARAMS_ORG, true);
                invokActivity(ResumeActivity.this, PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
            }
        });
        recyclerViewResume.setLoadingListener(this);
        recyclerViewResume.setPullRefreshEnabled(true);
        recyclerViewResume.setLoadingMoreEnabled(true);
        recyclerViewResume.refresh();
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
        mIsNewCreate = true;
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProjectEditActivity.PARAMS_NEW, true);
        invokActivity(this, ResumeEditActivity.class, bundle, JumpCode.FLAG_REQ_TALENT_EDIT);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadTalentData(mPage);
    }

    @Override
    public void onLoadMore() {
        ++mPage;
        loadTalentData(mPage);
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
    public void onEventLogin(TalentNotifyEvent event) {
        if (mIsNewCreate) {
            return;
        }
        if (event == null) {
            return;
        }
        if (this.isFinishing()) {
            return;
        }
        TalentDetailInfo t = TalentDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TalentNotifyEvent.NOTIFY_AVATAR:
                mHolder.sdvTalent.setImageURI(t.getLogo());
                break;
            case TalentNotifyEvent.NOTIFY_BASE_INFO:
                mHolder.tvTalentName.setText(t.getName());
                List<String> classifyNames = t.getClassifyNames();
                String s = "";
                for (int i = 0; i < classifyNames.size(); i++) {
                    if (i != classifyNames.size() - 1) {
                        s += classifyNames.get(i) + ",";
                    } else {
                        s += classifyNames.get(i);
                    }
                }
                mHolder.tvClassifyType.setText(s);
                mHolder.tvTalentAge.setText(String.valueOf(t.getAge()));
                break;
            case TalentNotifyEvent.NOTIFY_SEND:
                if (t.getStatus() == 1) {
                    mHolder.tvTalentStatus.setText(R.string.send_status);
                    mHolder.tvTalentStatus.setTextColor(getResources().getColor(R.color.theatre_send_suc));
                } else {
                    mHolder.tvTalentStatus.setText(R.string.unsend_status);
                    mHolder.tvTalentStatus.setTextColor(getResources().getColor(R.color.theatre_send_fail));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == JumpCode.FLAG_RES_ADD_TALENT) {
            try {
                TalentDetailInfo t = TalentDetailInfo.getInstance();
                if (TextUtils.isEmpty(t.getId())) {
                    return;
                }
                /**
                 * id : 402894115f38179a015f38179aea0000
                 * logo : http://139.224.47.213/image/8a999cce5f35db15015f381749680004.jpg
                 * name : xdssdf
                 * school : 北京舞蹈学院
                 * age : 13
                 * birthday : 1096560000000
                 * classifyNames : ["话剧演员","影视演员"]
                 * classifyNamesStr : 话剧演员 影视演员
                 * regionName : 西城区
                 * status : 1
                 * createTime : 1508474586000
                 * userId : 2c91faca5f37a50f015f37a6b7f80000
                 * authentication : 1
                 * agency : 收到发生的发
                 * linkTel : 18511320250
                 */
                TalentBean talentBean = new TalentBean();
                talentBean.setId(t.getId());
                talentBean.setLogo(t.getLogo());
                talentBean.setName(t.getName());
                talentBean.setSchool(t.getSchool());
                talentBean.setAge(t.getAge());
                talentBean.setBirthday(Long.valueOf(
                        TextUtils.isEmpty(t.getBirthday()) ? "0" : t.getBirthday()
                ));
                talentBean.setClassifyNames(t.getClassifyNames());
                //talentBean.setClassifyNamesStr(t.getClassifyNames());
                talentBean.setRegionName(t.getRegionName());
                talentBean.setStatus(t.getStatus());
                talentBean.setCreateTime(t.getCreateTime());
                talentBean.setUserId(t.getUserId());
                talentBean.setAuthentication(Integer.valueOf(
                        TextUtils.isEmpty(t.getAuthentication()) ? "0" : t.getAuthentication()
                ));
                talentBean.setAgency(t.getAgency());
                talentBean.setLinkTel(t.getLinkTel());
                mTalentBeans.add(talentBean);
                mOrgTalentAdapter.notifyDataSetChanged();
                recyclerViewResume.scrollToPosition(mTalentBeans.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
