package com.art.huakai.artshow.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.ShowApplication;


public class MyCountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 60000;//时间防止从59s开始显示（以倒计时60s为例子）
    private TextView tvVerify;
    private int endStrRid;
    private int normalColor, timingColor;//未计时的文字颜色，计时期间的文字颜色

    /**
     * 参数 millisInFuture         倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * <p/>
     * 参数 btn               点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * <p/>
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    public MyCountTimer(long millisInFuture, long countDownInterval, TextView btn, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.tvVerify = btn;
        this.endStrRid = endStrRid;
    }


    /**
     * 参数上面有注释
     */
    public MyCountTimer(TextView btn, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.tvVerify = btn;
        this.endStrRid = endStrRid;
    }

    public MyCountTimer(TextView btn) {
        super(TIME_COUNT, 1000);
        this.tvVerify = btn;
        this.endStrRid = R.string.resend_verify_code;
//        this.normalColor = Color.argb(255, 73, 144, 226);
//        this.timingColor = Color.argb(255, 170, 166, 162);
    }

    public MyCountTimer(TextView btn, boolean isTishi) {
        super(TIME_COUNT, 1000);
        this.tvVerify = btn;
        if (isTishi) {
            this.endStrRid = R.string.resend_verify_code;
        }
    }


    public MyCountTimer(TextView tv_varify, int normalColor, int timingColor) {
        this(tv_varify);
        this.normalColor = normalColor;
        this.timingColor = timingColor;
    }


    public MyCountTimer(TextView tv_varify, boolean tishi, int normalColor, int timingColor) {
        this(tv_varify, tishi);
        this.normalColor = normalColor;
        this.timingColor = timingColor;
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        tvVerify.setTextColor(normalColor);
        tvVerify.setText(endStrRid);
        tvVerify.setEnabled(true);
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        tvVerify.setTextColor(timingColor);
        tvVerify.setEnabled(false);
        String format = String.format(ShowApplication.getAppContext().getString(R.string.resend_verify_number), millisUntilFinished / 1000);
        tvVerify.setText(format);
    }
}
