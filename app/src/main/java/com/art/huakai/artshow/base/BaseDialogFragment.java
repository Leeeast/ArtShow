package com.art.huakai.artshow.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

/**
 * Created by lidongliang on 2017/10/1.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    private OnDismissListener mOnDismissListener;

    /**
     * Dialog消失监听
     */
    public interface OnDismissListener {
        void onDismiss();

    }

    public void setOnDismissListener(OnDismissListener dismissListener) {
        mOnDismissListener = dismissListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BaseDialog);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.base_dialog;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置宽满屏显示
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        //getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    /**
     * 获取数据 实例化DialogFragment需要传传参数，放在Bundle中，使用Bundle时候，需要做为空校验
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
}
