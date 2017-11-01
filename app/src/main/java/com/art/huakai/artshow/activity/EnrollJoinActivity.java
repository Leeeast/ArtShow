package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EnrollApplyAdapter;
import com.art.huakai.artshow.adapter.EnrollJoinAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class EnrollJoinActivity extends BaseActivity implements SmartRecyclerview.LoadingListener {

    public static final String PARAMS_TYPE = "PARAMS_TYPE";
    public static final String PARAMS_ENROLL = "PARAMS_ENROLL";
    public static final int TYPE_ENROLL_ALL = 0;
    public static final int TYPE_ENROLL_ENROLLED = 1;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycle_production)
    SmartRecyclerview recyclerview;

    private List<RepertorysBean> mRepertorys;
    private int mPage = 1;
    private EnrollJoinAdapter enrollJoinAdapter;
    public int enrollStatus;
    private EnrollDetailInfo mEnrollDetailInfo;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_enroll_join;
    }

    @Override
    public void initData() {
        mRepertorys = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            enrollStatus = extras.getInt(PARAMS_TYPE);
            mEnrollDetailInfo = (EnrollDetailInfo) extras.getSerializable(PARAMS_ENROLL);
        }
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.enroll_adopt);
    }

    @Override
    public void setView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        enrollJoinAdapter = new EnrollJoinAdapter(mRepertorys);
        enrollJoinAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(WorksDetailMessageActivity.PARAMS_ID, mRepertorys.get(position).getId());
                invokActivity(EnrollJoinActivity.this, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
            }
        });
        recyclerview.setAdapter(enrollJoinAdapter);
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
        params.put("id", mEnrollDetailInfo.enroll.id);
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("page", String.valueOf(mPage));
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        params.put("status", String.valueOf(enrollStatus));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_ENROLL_ENROLLED, params, 66, new RequestUtil.RequestListener() {
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
                        if (enrollJoinAdapter != null) {
                            enrollJoinAdapter.notifyDataSetChanged();
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
