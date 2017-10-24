package com.art.huakai.artshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.DataItem;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class TheatreEditActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.dataitem_base)
    DataItem dataItemBase;

    private ShowProgressDialog showProgressDialog;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_edit;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
    }


    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_title_my);


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
     * 基本资料
     */
    @OnClick(R.id.dataitem_base)
    public void jumpTheatreDataBase() {
        invokActivity(this, TheatreBaseActivity.class, null, JumpCode.FLAG_REQ_THEATRE_BASE);
    }

    /**
     * 简介
     */
    @OnClick(R.id.dataitem_intro)
    public void jumpTheatreIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_INTRO);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 详细介绍
     */
    @OnClick(R.id.dataitem_detail_intro)
    public void jumpTheatreDetailIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_DETAIL_INTRO);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 票区图
     */
    @OnClick(R.id.dataitem_seat_pic)
    public void jumpTheatreTicket() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_TICKET);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 剧场相片
     */
    @OnClick(R.id.dataitem_pic)
    public void jumpTheatrePic() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_PIC);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }

    /**
     * 剧场相片
     */
    @OnClick(R.id.dataitem_technical_parameters)
    public void jumpTheatreTechParam() {
        Bundle bundle = new Bundle();
        bundle.putInt(TheatreFillActivity.PARAMS_ACTION, TheatreFillActivity.CODE_ACTION_THEATRE_TECH_PARAM);
        invokActivity(this, TheatreFillActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_FILL);
    }
}
