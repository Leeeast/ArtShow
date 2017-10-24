package com.art.huakai.artshow.activity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 剧场基本资料
 * Created by lidongliang on 2017/10/14.
 */

public class TheatreBaseActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;

    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_base;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
    }


    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_data_base);

        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText(R.string.affirm_info);
    }

    @Override
    public void setView() {
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void confirmInfo() {
        Toast.makeText(this, "确认信息", Toast.LENGTH_SHORT).show();
    }

}
