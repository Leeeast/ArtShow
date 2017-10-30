package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.CreatorIntorAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.dialog.MemberAddDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.Staff;
import com.art.huakai.artshow.entity.TalentDetailInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProjectCreatorFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ShowProgressDialog showProgressDialog;
    private ArrayList<Staff> mStaffs;
    private MemberAddDialog memberAddDialog;

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

        CreatorIntorAdapter creatorIntorAdapter = new CreatorIntorAdapter(mStaffs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(creatorIntorAdapter);
        creatorIntorAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClickListener(int position) {
                if (memberAddDialog == null) {
                    memberAddDialog = MemberAddDialog.newInstence();
                    memberAddDialog.setCommitCallBack(new MemberAddDialog.CommitCallBack() {
                        @Override
                        public void createNewStaff(Staff staff) {

                        }

                        @Override
                        public void updateStaff(int position, Staff staff) {

                        }
                    });
                }
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
    }
}
