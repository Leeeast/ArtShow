package com.art.huakai.artshow.widget.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.art.huakai.artshow.R;

/**
 * banner底部的指示线，后期加入其他样式，比如原点型。
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-08-30  9:17
 */

public class BannerLine extends View {

    private Paint mPaint;
    private float mWidth;
    private int mPageSize;
    private float mPageWidth = 0f;
    private int mPosition;
    private float mPositionOffset;
    private int mLineColor = ContextCompat.getColor(getContext(), R.color.banner_blue);

    public BannerLine(Context context) {
        this(context, null);
    }

    public BannerLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1000f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPosition == 0) {
            canvas.drawLine((mPageSize - 3) * mPageWidth + mPageWidth * mPositionOffset, 0, (mPageSize - 2) * mPageWidth + mPageWidth * mPositionOffset, 0, mPaint);
            canvas.drawLine(0, 0, mPageWidth * mPositionOffset, 0, mPaint);
        } else if (mPosition == mPageSize - 2) {
            canvas.drawLine((mPosition - 1) * mPageWidth + mPageWidth * mPositionOffset, 0, mPosition * mPageWidth + mPageWidth * mPositionOffset, 0, mPaint);
            canvas.drawLine(0, 0, mPageWidth * mPositionOffset, 0, mPaint);
        } else {
            canvas.drawLine((mPosition - 1) * mPageWidth + mPageWidth * mPositionOffset, 0, mPosition * mPageWidth + mPageWidth * mPositionOffset, 0, mPaint);
        }

    }

    public void setPageWidth(int pageSize) {
        mPageSize = pageSize;
        calcPageWidth();
    }

    private void calcPageWidth() {
        this.mPageWidth = this.mWidth / (this.mPageSize - 2);
    }

    public void setPageScrolled(int position, float positionOffset) {
        mPosition = position;
        mPositionOffset = positionOffset;
        invalidate();
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
        mPaint.setColor(mLineColor);
    }
}
