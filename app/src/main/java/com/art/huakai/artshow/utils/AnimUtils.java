package com.art.huakai.artshow.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by lining on 17-10-2.
 */
public class AnimUtils {

    private final static String TAG="AnimUtils";

    public static void scaleZoom(View v, float scaleX, float scaleY, int duration) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", scaleX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", scaleY);
        animatorX.setDuration(duration);
        animatorY.setDuration(duration);
        set.play(animatorX).with(animatorY);
        set.start();
    }

    public static void translationXTo(View view, int toX, int duration) {
        view.clearAnimation();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", toX);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public static void rotate(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 3600f);
        animator.setDuration(5000);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.RESTART);
        animator.start();
    }


    public static void translationYTo(View view, int toY) {
        translationYTo(view, toY, 200);
    }

    public static void translationYTo(View view, int toY, int duration) {
        if (view == null) return;
        view.clearAnimation();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", toY);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public static void translationY(View view,int startY ,int toY, int duration) {
        if (view == null) return;
        view.clearAnimation();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startY,toY);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public static void rotation(View view, float fromDegrees, float toDegrees) {
        rotation(view, fromDegrees, toDegrees, 200);
    }

    public static void rotation(View view, float fromDegrees, float toDegrees, int duration) {
        if (view == null) return;
        view.clearAnimation();
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }







}
