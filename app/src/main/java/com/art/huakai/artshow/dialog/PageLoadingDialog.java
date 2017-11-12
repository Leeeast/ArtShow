package com.art.huakai.artshow.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.listener.PageLoadingListener;
import com.art.huakai.artshow.utils.DeviceUtils;


/**
 * 旋转等待
 * Created by lidongliang on 2015/5/29.
 */
public class PageLoadingDialog extends ProgressDialog implements DialogInterface.OnDismissListener, View.OnClickListener {
    private final String TAG = PageLoadingDialog.class.getSimpleName();
    private Context context;
    private ImageView round_progress;//圆环加载图
    private ObjectAnimator rotationAnim;
    private PageLoadingListener mListener;
    private LinearLayout lLyRetry;
    private Button btnRetry;

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (rotationAnim.isRunning()) {
            rotationAnim.cancel();
        }
    }

    public PageLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public PageLoadingDialog(Context context) {
        super(context, R.style.TransDialog);
        this.context = context;
    }

    public void setPageLoadingListener(PageLoadingListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_page_loading);
        setCanceledOnTouchOutside(false);
        round_progress = (ImageView) findViewById(R.id.round_progress);
        rotationAnim = ObjectAnimator.ofFloat(round_progress, "rotation", 0f, 360f);
        rotationAnim.setDuration(1500);
        rotationAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnim.setInterpolator(new LinearInterpolator());
        rotationAnim.setRepeatMode(ValueAnimator.RESTART);
        setOnDismissListener(this);

        lLyRetry = (LinearLayout) findViewById(R.id.lly_retry);
        btnRetry = (Button) findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);
        FrameLayout flyRoot = (FrameLayout) findViewById(R.id.fly_root);
        int screenHeight = DeviceUtils.getScreenHeight(ShowApplication.getAppContext());
        int screenWidth = DeviceUtils.getScreenWeight(ShowApplication.getAppContext());
        int statusBarHeight = DeviceUtils.getStatusBarHeight(ShowApplication.getAppContext());

        ViewGroup.LayoutParams layoutParams = flyRoot.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = screenHeight - statusBarHeight;

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (mListener != null) {
                            PageLoadingDialog.this.dismiss();
                            mListener.onClose();
                        }
                    }
                    return true;

                }
                return false;
            }
        });
    }

    public void show() {
        try {
            if (!isShowing())
                super.show();
        } catch (Exception e) {

        }
    }

    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public void showLoading() {
        round_progress.setVisibility(View.VISIBLE);
        lLyRetry.setVisibility(View.GONE);
    }

    public void showErrorLoading() {
        round_progress.setVisibility(View.INVISIBLE);
        lLyRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            round_progress.setVisibility(View.VISIBLE);
            rotationAnim.start();
        } else {
            rotationAnim.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                showLoading();
                if (mListener != null) {
                    mListener.onRetry();
                }
                break;
        }
    }
}
