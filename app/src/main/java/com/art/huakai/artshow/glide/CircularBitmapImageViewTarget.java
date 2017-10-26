package com.art.huakai.artshow.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.art.huakai.artshow.utils.LogUtil;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by Administrator on 2017/10/26.
 */

public class CircularBitmapImageViewTarget extends BitmapImageViewTarget {
    private static final String TAG = CircularBitmapImageViewTarget.class.getSimpleName();
    private Context context;
    private ImageView imageView;
    private int mRadius;

    public CircularBitmapImageViewTarget(ImageView view) {
        super(view);
    }

    public CircularBitmapImageViewTarget(Context context, ImageView view, int radius) {
        super(view);
        this.context = context;
        this.imageView = view;
        this.mRadius = radius;
    }

    /**
     * 重写 setResource（），生成圆角的图片
     *
     * @param resource
     */
    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(this.context.getResources(), resource);
        /**
         *   设置图片的shape为圆形.
         *
         *   若是需要制定圆角的度数，则调用setCornerRadius（）。
         */
        bitmapDrawable.setCircular(true);
        bitmapDrawable.setCornerRadius(mRadius);
        if (resource != null) {
            int imageViewWidth = this.imageView.getWidth();
            int width = resource.getWidth();
            int height = resource.getHeight();
            LogUtil.i(TAG, "width&height = " + width + "&" + height);
        }
        this.imageView.setImageDrawable(bitmapDrawable);

    }
}
