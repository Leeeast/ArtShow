package com.art.huakai.artshow.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.utils.DeviceUtils;


/**
 * 旋转等待
 * Created by lidongliang on 2015/5/29.
 */
public class ShowProgressDialog extends ProgressDialog implements DialogInterface.OnDismissListener {
    private final String TAG = ShowProgressDialog.class.getSimpleName();
    private String mLoadingText;
    private Context context;
    private ImageView round_progress;//圆环加载图
    private ObjectAnimator rotationAnim;
    private TextView textLoading;

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (rotationAnim.isRunning()) {
            rotationAnim.cancel();
        }
    }

    public ShowProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public ShowProgressDialog(Context context) {
        super(context, R.style.TransDialog);
        this.context = context;
    }

    public ShowProgressDialog(Context context, String strText) {
        this(context, R.style.TransDialog);
        mLoadingText = strText;
    }

    public void setLoadingText(String loadingText) {
        if (!TextUtils.isEmpty(loadingText)) {
            textLoading.setText(loadingText);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        round_progress = (ImageView) findViewById(R.id.round_progress);
        textLoading = (TextView) findViewById(R.id.loading_text);
        rotationAnim = ObjectAnimator.ofFloat(round_progress, "rotation", 0f, 360f);
        rotationAnim.setDuration(1500);
        rotationAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnim.setInterpolator(new LinearInterpolator());
        rotationAnim.setRepeatMode(ValueAnimator.RESTART);
        setOnDismissListener(this);
        if (mLoadingText != null && mLoadingText.length() > 0) {
            textLoading.setText(mLoadingText);
        }
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
