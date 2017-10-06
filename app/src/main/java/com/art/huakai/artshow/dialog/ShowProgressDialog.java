package com.art.huakai.artshow.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
    private int width;
    private int widthdp = 185;
    private String mLoadingText;
    private Context context;
    private ImageView round_progress;//圆环加载图
    private ObjectAnimator rotationAnim;

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (rotationAnim.isRunning()) {
            rotationAnim.cancel();
        }
    }

    public enum ProgressType {
        DEFAULT_ROUND(0), GIT_ANIMAL(1);
        // 定义私有变量
        private int typeNum;

        // 构造函数，枚举类型只能为私有
        ProgressType(int _nCode) {
            this.typeNum = _nCode;
        }

        @Override
        public String toString() {

            return String.valueOf(this.typeNum);

        }
    }

    public ProgressType getmProgressType() {
        return mProgressType;
    }

    public void setmProgressType(ProgressType mProgressType) {
        this.mProgressType = mProgressType;
    }

    private ProgressType mProgressType = ProgressType.DEFAULT_ROUND;

    public ShowProgressDialog(Context context, int theme) {
        super(context, theme);
        width = DeviceUtils.dip2px(context, widthdp);
    }

    public ShowProgressDialog(Context context) {
        super(context, R.style.TransDialog);
        this.context = context;
        width = DeviceUtils.dip2px(context, widthdp);
    }

    public ShowProgressDialog(Context context, String strText) {
        this(context, R.style.TransDialog);
        mLoadingText = strText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        getWindow().setAttributes(params);
        params.alpha = 1.0f;
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        round_progress = (ImageView) findViewById(R.id.round_progress);
        rotationAnim = ObjectAnimator.ofFloat(round_progress, "rotation", 0f, 360f);
        rotationAnim.setDuration(1500);
        rotationAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnim.setInterpolator(new LinearInterpolator());
        rotationAnim.setRepeatMode(ValueAnimator.RESTART);
        setOnDismissListener(this);
        if (mLoadingText != null && mLoadingText.length() > 0) {
            TextView textLoading = (TextView) findViewById(R.id.loading_text);
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
        if (getmProgressType() == ProgressType.DEFAULT_ROUND) {
            if (hasFocus) {
                round_progress.setVisibility(View.VISIBLE);
                rotationAnim.start();
            } else {
                rotationAnim.cancel();
            }
        }
    }
}
