package com.art.huakai.artshow.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

public class SettingItem extends FrameLayout {
    private Context mContext;
    private View mRootView;
    private String mItemText;
    private Drawable mItemIcon, mItemArrow;

    public SettingItem(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCustomAttributes(attrs);
        init();
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.SettingItem);

        mItemText = ta.getString(R.styleable.SettingItem_item_title);
        mItemIcon = ta.getDrawable(R.styleable.SettingItem_item_icon);
        mItemArrow = ta.getDrawable(R.styleable.SettingItem_item_arrow);

        ta.recycle();
    }

    /**
     * 初始化
     */
    public void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_set_item, this, true);
        ImageView mIvItemIcon = (ImageView) mRootView.findViewById(R.id.iv_item_icon);
        ImageView mIvItemArrow = (ImageView) mRootView.findViewById(R.id.iv_item_arrow);
        TextView mTvItemTitle = (TextView) mRootView.findViewById(R.id.tv_item_title);
        if (!TextUtils.isEmpty(mItemText)) {
            mTvItemTitle.setText(mItemText);
        }
        if (mItemIcon != null) {
            mIvItemIcon.setBackground(mItemIcon);
        }
        if (mItemArrow != null) {
            mIvItemArrow.setBackground(mItemArrow);
        }
    }
}
