package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.WorksDetailMessageActivity;
import com.art.huakai.artshow.adapter.MeUnloginAdapter;
import com.art.huakai.artshow.listener.OnItemClickListener;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.RepertorysBean;
import com.art.huakai.artshow.eventbus.ActionTypeEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class MeUnloginFragment extends BaseFragment {

    private RecyclerView rlvRecommend;
    private RequestCall requestCall;
    private List<RepertorysBean> mRepertorysBeen;
    private MeUnloginAdapter meUnloginAdapter;

    public MeUnloginFragment() {
        // Required empty public constructor
    }

    public static MeUnloginFragment newInstance() {
        MeUnloginFragment fragment = new MeUnloginFragment();
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        mRepertorysBeen = new ArrayList<>();
        mRepertorysBeen.add(null);
    }

    /**
     * 获取推荐项目
     */
    private void getRecommendRepertory() {
        requestCall = RequestUtil.request(false, Constant.URL_INDEX_REPERTORY, null, 10, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        List<RepertorysBean> repertorysBeen = GsonTools.parseDatas(obj, RepertorysBean.class);
                        mRepertorysBeen.addAll(repertorysBeen);
                        meUnloginAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(getString(R.string.tip_data_parsing_failure));
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
    public int getLayoutID() {
        return R.layout.fragment_me_unlogin;
    }

    @Override
    public void initView(View rootView) {
        rlvRecommend = (RecyclerView) rootView.findViewById(R.id.rlv_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvRecommend.setLayoutManager(linearLayoutManager);
        rlvRecommend.setNestedScrollingEnabled(false);

        meUnloginAdapter = new MeUnloginAdapter(mRepertorysBeen);
        meUnloginAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(WorksDetailMessageActivity.PARAMS_ID, mRepertorysBeen.get(position).getId());
                invokActivity(getContext(), WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
                EventBus.getDefault().post(new ActionTypeEvent(true));
            }
        });
        rlvRecommend.setAdapter(meUnloginAdapter);
        getRecommendRepertory();
    }

    @Override
    public void setView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestCall != null) {
            requestCall.cancel();
        }
    }
}
