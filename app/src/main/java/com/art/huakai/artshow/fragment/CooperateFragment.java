package com.art.huakai.artshow.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.CooperateAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.EnrollInfo;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * 合作Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class CooperateFragment extends BaseFragment implements SmartRecyclerview.LoadingListener {
    //Frament添加TAG
    public static final String TAG_FRAGMENT = CooperateFragment.class.getSimpleName();
    private EditText edtSearch;
    private SmartRecyclerview recyclerView;
    private int mPage = 1;
    private List<EnrollInfo> mEnrollInfos;
    private CooperateAdapter mAollAdapter;

    public CooperateFragment() {
    }

    public static CooperateFragment newInstance() {
        CooperateFragment fragment = new CooperateFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        mEnrollInfos = new ArrayList<>();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_cooperate;
    }

    @Override
    public void initView(View rootView) {
        Drawable drawableLeft = getResources().getDrawable(R.mipmap.chinashow_search);
        drawableLeft.setBounds(
                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX),
                0,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_16PX)
                        + getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_18PX));
        edtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        edtSearch.setCompoundDrawables(drawableLeft, null, null, null);

        recyclerView = (SmartRecyclerview) rootView.findViewById(R.id.recyvleview_coll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 加载招募列表
     *
     * @param page
     */
    public void loadEnrolData(int page) {
        Map<String, String> params = new TreeMap<>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(Constant.COUNT_PER_PAGE));
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        RequestUtil.request(true, Constant.URL_ENROLL_PAGE, params, 30, new RequestUtil.RequestListener() {

            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (mPage == 1) {
                    recyclerView.refreshComplete();
                    mEnrollInfos.clear();
                    mEnrollInfos.add(null);
                } else {
                    recyclerView.loadMoreComplete();
                }
                if (isSuccess) {
                    try {
                        List<EnrollInfo> enrollInfos = GsonTools.parseDatas(obj, EnrollInfo.class);
                        LogUtil.i(TAG, "mEnrollInfos.size = " + enrollInfos.size());
                        if (mPage != 1 && (enrollInfos == null || enrollInfos.size() == 0)) {
                            Toast.makeText(getContext(), getString(R.string.tip_no_more_date), Toast.LENGTH_SHORT).show();
                        }
                        mEnrollInfos.addAll(enrollInfos);
                        if (mAollAdapter != null) {
                            mAollAdapter.notifyDataSetChanged();
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
                if (mPage == 1) {
                    recyclerView.refreshComplete();
                } else {
                    recyclerView.loadMoreComplete();
                }
            }
        });
    }

    @Override
    public void setView() {
        mAollAdapter = new CooperateAdapter(mEnrollInfos);
        recyclerView.setAdapter(mAollAdapter);
        recyclerView.setLoadingListener(this);
        recyclerView.refresh();
        mAollAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if (mEnrollInfos.size() > 0) {
                    Toast.makeText(getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadEnrolData(mPage);
    }

    @Override
    public void onLoadMore() {
        ++mPage;
        loadEnrolData(mPage);
    }
}
