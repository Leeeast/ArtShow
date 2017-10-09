package com.art.huakai.artshow.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;

/**
 * Created by Administrator on 2017/10/9.
 */

public class SmartToast {
    private Toast mToast;

    private SmartToast(Context context, CharSequence text, Drawable tipDrawable, int duration) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_smart, null);
        ImageView ivToast = (ImageView) toastView.findViewById(R.id.iv_toast);
        TextView tvToast = (TextView) toastView.findViewById(R.id.tv_toast);
        if (!TextUtils.isEmpty(text)) {
            tvToast.setText(text);
        }
        if (tipDrawable != null) {
            ivToast.setBackground(tipDrawable);
        }
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(toastView);
        setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, context.getResources().getDimensionPixelSize(R.dimen.DIMEN_120PX));
    }

    public static SmartToast makeToast(Context context, CharSequence text, Drawable tipDrawable, int duration) {
        return new SmartToast(context, text, tipDrawable, duration);
    }

    /**
     * 显示
     */
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    /**
     * 设置位置
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
