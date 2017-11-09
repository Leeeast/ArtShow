package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.OrgTheatreAdapter;
import com.art.huakai.artshow.adapter.holder.OrgTheatreHolder;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.Theatre;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreNotifyEvent;
import com.art.huakai.artshow.listener.OnHolderCallBack;
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
    private RequestCall requestCall;
    private OrgTheatreHolder mHolder;
    private boolean isNewCreate = false;

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
        EventBus.getDefault().register(this);
        showProgressDialog = new ShowProgressDialog(this);
        mTheatres = new ArrayList<>();
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
        requestCall = RequestUtil.request(true, Constant.URL_USER_THEATER, params, 60, new RequestUtil.RequestListener() {
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
        mOrgTheatreAdapter.setOnItemClickListener(new OnHolderCallBack() {
            @Override
            public void onItemClickListener(int position, RecyclerView.ViewHolder holder) {
                mHolder = (OrgTheatreHolder) holder;
                isNewCreate = false;
                Theatre theatre = mTheatres.get(position);
                TheatreDetailInfo.getInstance().setId(theatre.getId());
                Bundle bundle = new Bundle();
                bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, theatre.getId());
                bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, true);
                invokActivity(TheatreActivity.this, TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
            }
        });
        recyclerViewTheatre.setLoadingListener(this);
        recyclerViewTheatre.setPullRefreshEnabled(true);
        recyclerViewTheatre.setLoadingMoreEnabled(true);
        recyclerViewTheatre.refresh();
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
        isNewCreate = true;
        Bundle bundle = new Bundle();
        bundle.putBoolean(TheatreEditActivity.PARAMS_NEW_CREATE, true);
        invokActivity(this, TheatreEditActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_EDIT);
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
    public void onEventLogin(TheatreNotifyEvent event) {
        if (isNewCreate) {
            return;
        }
        if (event == null) {
            return;
        }
        if (this.isFinishing()) {
            return;
        }
        TheatreDetailInfo t = TheatreDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TheatreNotifyEvent.NOTIFY_THEATRE_AVATAR:
                mHolder.sdvTheatre.setImageURI(t.getLinkman());
                break;
            case TheatreNotifyEvent.NOTIFY_THEATRE_BASE_INFO:
                mHolder.tvTheatreName.setText(t.getName());
                mHolder.tvSeatCount.setText(t.getSeating());
                mHolder.tvTheatrePosition.setText(t.getRegionName());
                String price = String.format(getString(R.string.me_theatre_price), Integer.valueOf(t.getExpense()));
                mHolder.tvTheatrePrice.setText(price);
                break;
            case TheatreNotifyEvent.NOTIFY_THEATRE_SEND:
                if (t.getStatus() == 1) {
                    mHolder.tvTheatreStatus.setText(R.string.send_status);
                    mHolder.tvTheatreStatus.setTextColor(getResources().getColor(R.color.theatre_send_suc));
                } else {
                    mHolder.tvTheatreStatus.setText(R.string.unsend_status);
                    mHolder.tvTheatreStatus.setTextColor(getResources().getColor(R.color.theatre_send_fail));
                }
                break;
        }
    }
}
