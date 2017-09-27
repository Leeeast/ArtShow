package com.art.huakai.artshow.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immerseStatusBar();
        setContentView(getLayoutID());
        initData();
        initView();
        setView();
    }

    /**
     * 沉浸状态栏
     */
    public abstract void immerseStatusBar();

    /**
     * 设置跟布局
     *
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 获取数据
     */
    public abstract void initData();

    /**
     * 初始化UI
     */
    public abstract void initView();

    /**
     * 填充数据
     */
    public abstract void setView();
}

