package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.utils.SharePreUtil;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity {
    private static final int CODE_JUMP_GUIDE = 10;
    private static final int CODE_JUMP_MAIN = 11;
    private boolean mFirstEnterApp;
    private JumpHandler mJumpHandler;

    @Override
    public void immerseStatusBar() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData() {
        mFirstEnterApp = SharePreUtil.getInstance().isFirstEnterApp();
        mJumpHandler = new JumpHandler(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {
        if (mFirstEnterApp) {
            mJumpHandler.sendEmptyMessageDelayed(CODE_JUMP_GUIDE, Constant.SPLASH_TIME_OFFSET);
            SharePreUtil.getInstance().setIsFirstEnterApp(false);
        } else {
            mJumpHandler.sendEmptyMessageDelayed(CODE_JUMP_MAIN, Constant.SPLASH_TIME_OFFSET);
        }
    }

    private static class JumpHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        public JumpHandler(SplashActivity activity) {
            mActivity = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_JUMP_GUIDE:
                    jumpGuide();
                    break;
                case CODE_JUMP_MAIN:
                    jumpMain();
                    break;
            }
        }

        /**
         * 跳转至MainActivity
         */
        private void jumpMain() {
            if (mActivity.get() != null) {
                mActivity.get().startActivity(new Intent(mActivity.get(), MainActivity.class));
                mActivity.get().finish();
            }
        }

        /**
         * 跳转至引导页
         */
        private void jumpGuide() {
            if (mActivity.get() != null) {
                mActivity.get().startActivity(new Intent(mActivity.get(), GuideActivity.class));
                mActivity.get().finish();
            }
        }
    }
}
