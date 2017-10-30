package com.art.huakai.artshow.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.EnrollApplyAdapter;
import com.art.huakai.artshow.adapter.EnrollJoinAdapter;
import com.art.huakai.artshow.adapter.OnItemClickListener;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class EnrollJoinActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.recycle_production)
    SmartRecyclerview recyclerview;

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
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.enroll_produce_adopt);


    }

    @Override
    public void setView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("132");
        strings.add("132");
        strings.add("132");
        strings.add("132");
        EnrollJoinAdapter enrollJoinAdapter = new EnrollJoinAdapter(strings);
        enrollJoinAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(EnrollJoinActivity.this, "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerview.setAdapter(enrollJoinAdapter);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    @OnClick(R.id.tv_subtitle)
    public void changeAccountName() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
