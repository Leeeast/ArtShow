package com.art.huakai.artshow.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类，定义了一些基础的方法，注意方法顺序,根据需求部分步骤可以省略
 * Created by lidongliang on 2017/9/27.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected Unbinder mUnBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immerseStatusBar();
        setContentView(getLayoutID());
        mUnBinder = ButterKnife.bind(this);
        initData();
        initView();
        setView();
    }

    /**
     * 沉浸状态栏 step_1
     */
    public abstract void immerseStatusBar();

    /**
     * 设置跟布局 step_2
     *
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 获取数据 step_3
     */
    public abstract void initData();

    /**
     * 初始化UI step_4
     */
    public abstract void initView();

    /**
     * 填充数据 step_5
     */
    public abstract void setView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) mUnBinder.unbind();
    }
}

