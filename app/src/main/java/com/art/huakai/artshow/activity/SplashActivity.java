package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.utils.SharePreUtil;

public class SplashActivity extends BaseActivity {
    private final int CODE_JUMP_GUIDE = 10;
    private final int CODE_JUMP_MAIN = 11;
    private boolean mFirstEnterApp;
    private Handler mHandler = new Handler() {
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
    };

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
    }

    @Override
    public void initView() {

    }

    @Override
    public void setView() {
        if (mFirstEnterApp) {
            mHandler.sendEmptyMessageDelayed(CODE_JUMP_GUIDE, Constant.SPLASH_TIME_OFFSET);
            SharePreUtil.getInstance().setIsFirstEnterApp(false);
        } else {
            mHandler.sendEmptyMessageDelayed(CODE_JUMP_MAIN, Constant.SPLASH_TIME_OFFSET);
        }
    }

    /**
     * 跳转至MainActivity
     */
    private void jumpMain() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    /**
     * 跳转至引导页
     */
    private void jumpGuide() {
        startActivity(new Intent(this, GuideActivity.class));
        this.finish();
    }
}
