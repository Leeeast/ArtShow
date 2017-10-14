package com.art.huakai.artshow.utils;

import android.content.ComponentName;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lining on 16-9-8.
 */
public class ToastUtils {


    public static void showToast(Context context,int size,String text){

        Toast toast =Toast.makeText(context,"",Toast.LENGTH_SHORT);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(size);
        toast.setText(text);
        toast.show();
    }

    public static void showLongToast(Context context,int size,String text){

        Toast toast =Toast.makeText(context,"",Toast.LENGTH_LONG);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(size);
        toast.setText(text);
        toast.show();
    }


}
