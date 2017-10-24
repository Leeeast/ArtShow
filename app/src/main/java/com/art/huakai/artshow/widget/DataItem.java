package com.art.huakai.artshow.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.huakai.artshow.R;

/**
 * Created by lidongliang on 2017/10/6.
 */

public class DataItem extends FrameLayout {
    private Context mContext;
    private View mRootView;
    private String mItemText, mItemDes;
    private Drawable mItemArrow;
    private TextView mTvItemDes;

    public DataItem(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DataItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCustomAttributes(attrs);
        init();
    }

    public DataItem(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mItemArrow = ta.getDrawable(R.styleable.SettingItem_item_arrow);
        mItemDes = ta.getString(R.styleable.SettingItem_item_des);

        ta.recycle();
    }

    /**
     * 初始化
     */
    public void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_data_item, this, true);
        ImageView mIvItemArrow = (ImageView) mRootView.findViewById(R.id.iv_item_arrow);
        TextView mTvItemTitle = (TextView) mRootView.findViewById(R.id.tv_item_title);
        mTvItemDes = (TextView) mRootView.findViewById(R.id.tv_item_des);
        if (!TextUtils.isEmpty(mItemText)) {
            mTvItemTitle.setText(mItemText);
        }
        if (mItemArrow != null) {
            mIvItemArrow.setBackground(mItemArrow);
        }
        if (TextUtils.isEmpty(mItemDes)) {
            mTvItemDes.setVisibility(View.GONE);
        } else {
            mTvItemDes.setText(mItemDes);
            mTvItemDes.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置描述
     */
    public void setDesText(String desText) {
        mTvItemDes.setText(desText);
        mTvItemDes.setVisibility(View.VISIBLE);
    }
}
