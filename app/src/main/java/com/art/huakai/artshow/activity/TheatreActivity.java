package com.art.huakai.artshow.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.OrgTheatreAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.Theatre;
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

public class TheatreActivity extends BaseActivity implements SmartRecyclerview.LoadingListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview_theatre)
    SmartRecyclerview recyclerViewTheatre;

    private List<Theatre> mTheatres;
    private OrgTheatreAdapter mOrgTheatreAdapter;
    private int mPage = 1;
    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        mTheatres = new ArrayList<>();
        loadTheatreData(mPage);
    }

    /**
     * 加载剧场数据
     *
     * @param page
     */
    private void loadTheatreData(final int page) {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("page", String.valueOf(mPage));
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_USER_THEATER, params, 60, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (page == 1) {
                    recyclerViewTheatre.refreshComplete();
                    mTheatres.clear();
                } else {
                    recyclerViewTheatre.loadMoreComplete();
                }
                if (isSuccess) {
                    try {
                        List<Theatre> theatres = GsonTools.parseDatas(obj, Theatre.class);
                        LogUtil.i(TAG, "mEnrollInfos.size = " + theatres.size());
                        if (mPage == 1 && (theatres == null || theatres.size() == 0)) {
                            mTheatres.clear();
                        } else if (mPage != 1 && (theatres == null || theatres.size() == 0)) {
                            showToast(getString(R.string.tip_no_more_date));
                        }
                        mTheatres.addAll(theatres);
                        if (mOrgTheatreAdapter != null) {
                            mOrgTheatreAdapter.notifyDataSetChanged();
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
        tvTitle.setText(R.string.theatre_title_my);
        tvSubtitle.setVisibility(View.VISIBLE);
        tvSubtitle.setText(R.string.theatre_upload);

    }

    @Override
    public void setView() {
        LinearLayoutManager layManager = new LinearLayoutManager(this);
        layManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTheatre.setLayoutManager(layManager);

        mOrgTheatreAdapter = new OrgTheatreAdapter(mTheatres);
        recyclerViewTheatre.setAdapter(mOrgTheatreAdapter);
        recyclerViewTheatre.setLoadingListener(this);
        recyclerViewTheatre.setPullRefreshEnabled(true);
        recyclerViewTheatre.setLoadingMoreEnabled(true);

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
        invokActivity(this, TheatreEditActivity.class, null, JumpCode.FLAG_REQ_THEATRE_EDIT);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadTheatreData(mPage);
    }

    @Override
    public void onLoadMore() {
        ++mPage;
        loadTheatreData(mPage);
    }
}