package com.art.huakai.artshow.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类，定义了一些基础的方法，注意方法顺序
 * Created by lidongliang on 2017/9/27.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected Unbinder mUnBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mUnBinder = ButterKnife.bind(this, view);
        setView();
    }

    public void showToast(String toastStr) {
        Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取数据 实例化Fragment需要传传参数，放在Bundle中，使用Bundle时候，需要做为空校验
     * step_1
     */
    public abstract void initData(@Nullable Bundle bundle);

    /**
     * 设置跟布局
     * step_2
     *
     * @return 资源布局ID
     */
    public abstract int getLayoutID();

    /**
     * 初始化UI
     * step_3
     */
    public abstract void initView(View rootView);

    /**
     * 填充数据
     * step_4
     */
    public abstract void setView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) mUnBinder.unbind();
    }

    /**
     * 获取自定义TAG
     *
     * @return
     */
    public String getTAG() {
        return TAG;
    }
}
