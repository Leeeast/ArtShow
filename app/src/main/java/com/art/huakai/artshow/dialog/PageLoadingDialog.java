package com.art.huakai.artshow.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.utils.DeviceUtils;


/**
 * 旋转等待
 * Created by lidongliang on 2015/5/29.
 */
public class PageLoadingDialog extends ProgressDialog implements DialogInterface.OnDismissListener {
    private final String TAG = PageLoadingDialog.class.getSimpleName();
    private Context context;
    private ImageView round_progress;//圆环加载图
    private ObjectAnimator rotationAnim;

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
                return true;
            }
        });
    }

    public void show() {
        if (!isShowing())
            super.show();
    }

    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
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
}
