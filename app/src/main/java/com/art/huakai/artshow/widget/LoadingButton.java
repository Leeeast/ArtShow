package com.art.huakai.artshow.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/6.
 */

public class LoadingButton extends FrameLayout {
    private final int LOADING_DURATION_CIRCLE = 1800;
    private Context mContext;
    private View mRootView;
    private String mLoadingText;
    private int mLoadingTextSize;
    private int mLoadingTextColor;
    private Drawable mLoadingDrawableRight;
    private int mLoadingDrawablePadding;
    private TextView tvLoading;
    private ImageView ivLoading;
    private ObjectAnimator mRotationAnim;

    public LoadingButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCustomAttributes(attrs);
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setCustomAttributes(attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 获取属性
     *
     * @param attrs
     */
    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        mLoadingText = ta.getString(R.styleable.LoadingButton_loading_text);
        mLoadingTextSize = ta.getDimensionPixelSize(R.styleable.LoadingButton_loading_textSize, 45);
        mLoadingTextColor = ta.getColor(R.styleable.LoadingButton_loading_textColor, Color.WHITE);
        mLoadingDrawableRight = ta.getDrawable(R.styleable.LoadingButton_loading_drawableRight);
        mLoadingDrawablePadding = ta.getDimensionPixelSize(R.styleable.LoadingButton_loading_drawablePadding, 0);
        ta.recycle();
    }

    /**
     * 初始化
     */
    public void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_btn, this, true);
        tvLoading = (TextView) mRootView.findViewById(R.id.tv_loading);
        ivLoading = (ImageView) mRootView.findViewById(R.id.iv_loading);

        tvLoading.setText(mLoadingText);
        tvLoading.setTextColor(mLoadingTextColor);
        tvLoading.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize);
        ivLoading.setBackground(mLoadingDrawableRight);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.rightMargin = mLoadingDrawablePadding;
        ivLoading.setLayoutParams(layoutParams);
    }

    /**
     * 开始加载
     */
    public void startLoading() {
        ivLoading.setVisibility(View.VISIBLE);
        //第二个参数“rotation”表明要执行旋转
        // 0f - > 360f，从旋转360度，也可以是负值，负值即为逆时针旋转，正值是顺时针旋转。
        mRotationAnim = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
        //动画的持续时间
        mRotationAnim.setDuration(LOADING_DURATION_CIRCLE);
        mRotationAnim.setInterpolator(new LinearInterpolator());
        mRotationAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRotationAnim.setRepeatMode(ObjectAnimator.RESTART);
        mRotationAnim.start();
    }

    /**
     * 停止加载
     */
    public void stopLoading() {
        ivLoading.setVisibility(View.INVISIBLE);
        ivLoading.clearAnimation();
        mRotationAnim.cancel();
    }
}
