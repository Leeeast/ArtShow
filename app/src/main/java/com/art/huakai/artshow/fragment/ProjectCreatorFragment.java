package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.CreatorIntorAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.MemberAddDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.Staff;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ProjectCreatorFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ShowProgressDialog showProgressDialog;
    private ArrayList<Staff> mStaffs;

    public ProjectCreatorFragment() {
    }

    public static ProjectCreatorFragment newInstance() {
        ProjectCreatorFragment fragment = new ProjectCreatorFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        mStaffs = new ArrayList<>();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_project_creator;
    }

    @Override
    public void initView(View rootView) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_creator_intro);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
        final CreatorIntorAdapter creatorIntorAdapter = new CreatorIntorAdapter(mStaffs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(creatorIntorAdapter);
        creatorIntorAdapter.setOnItemClickListener(new CreatorIntorAdapter.OnItemClickListener() {

            @Override
            public void onCreateNew(final int position) {
                MemberAddDialog memberAddDialog = MemberAddDialog.newInstence();
                memberAddDialog.setCommitCallBack(new MemberAddDialog.CommitCallBack() {
                    @Override
                    public void createNewStaff(Staff staff) {
                        mStaffs.add(staff);
                        creatorIntorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void updateStaff(int positio, Staff staff) {

                    }
                });
                memberAddDialog.show(getChildFragmentManager(), "MEMBERADD.DIALOG");
            }

            @Override
            public void onUpdate(final int position) {
                MemberAddDialog memberAddDialog = MemberAddDialog.newInstence(position, mStaffs.get(position));
                memberAddDialog.setCommitCallBack(new MemberAddDialog.CommitCallBack() {
                    @Override
                    public void createNewStaff(Staff staff) {

                    }

                    @Override
                    public void updateStaff(int position, Staff staff) {
                        mStaffs.remove(position);
                        mStaffs.add(position, staff);
                        creatorIntorAdapter.notifyItemChanged(position);
                    }
                });
                memberAddDialog.show(getChildFragmentManager(), "MEMBERADD.DIALOG");

            }
        });
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
        if (mStaffs.size() <= 0) {
            showToast(getString(R.string.tip_project_staff_add));
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(ProjectDetailInfo.getInstance().getId())) {
            params.put("id", ProjectDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        JSONArray jsonArray = new JSONArray();
        for (Staff staff : mStaffs) {
            jsonArray.put(staff.toString());
        }
        String jsongStaffs = jsonArray.toString();
        params.put("staffs", jsongStaffs);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_EDIT_TAFFS, params, 90, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.tip_project_stuff_change_suc));
                        JSONObject jsonObject = new JSONObject(obj);
                        String projectId = jsonObject.getString("id");
                        ProjectDetailInfo.getInstance().setId(projectId);
                        ProjectDetailInfo.getInstance().setStaffs(mStaffs);
                        EventBus.getDefault().post(new ProjectInfoChangeEvent());
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
                showToast(getString(R.string.tip_data_commit_fail));
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }
}
