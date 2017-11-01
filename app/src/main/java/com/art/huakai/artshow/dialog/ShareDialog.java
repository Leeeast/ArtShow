package com.art.huakai.artshow.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.OnClick;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class ShareDialog extends BaseDialogFragment {
    public static final String PARAMS_TITLE = "PARAMS_TITLE";
    public static final String PARAMS_URL = "PARAMS_URL";
    private String mTitle;
    private String mUrl;
    private IWXAPI wxapi;

    public static ShareDialog newInstence(String title, String url) {
        ShareDialog typeConfirmDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_TITLE, title);
        bundle.putString(PARAMS_URL, url);
        typeConfirmDialog.setArguments(bundle);
        return typeConfirmDialog;
    }

    public static ShareDialog newInstence() {
        ShareDialog typeConfirmDialog = new ShareDialog();

        return typeConfirmDialog;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mTitle = bundle.getString(PARAMS_TITLE);
            mUrl = bundle.getString(PARAMS_URL);
        }
        regToWx();
    }


    /**
     * 微信相关初始化
     */
    private void regToWx() {
        wxapi = WXAPIFactory.createWXAPI(getContext(), Constant.WEIXIN_APPID, true);
        wxapi.registerApp(Constant.WEIXIN_APPID);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_share;
    }

    @Override
    public void initView(View rootView) {
    }


    @Override
    public void setView() {

    }

    @OnClick(R.id.btn_cancle)
    public void btnCancel() {
        this.dismiss();
    }

    @OnClick(R.id.tv_url_copy)
    public void urlCopy() {
        if (!TextUtils.isEmpty(mUrl)) {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("SHARE", mUrl);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showToast(getString(R.string.tip_share_copy_suc));
        } else {
            showToast(getString(R.string.tip_share_url_err));
        }
    }

    @OnClick(R.id.tv_url_open)
    public void urlOpenWidthBrowser() {
        try {
            if (!TextUtils.isEmpty(mUrl)) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mUrl);
                intent.setData(content_url);
                getContext().startActivity(intent);
            } else {
                showToast(getString(R.string.tip_share_url_err));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fly_share_wechat)
    public void shareWechat() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = TextUtils.isEmpty(mTitle) ? getString(R.string.app_name) : mTitle;
        msg.description = getContext().getString(R.string.tip_share_url_des);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_share_img);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @OnClick(R.id.fly_share_wechat_momnet)
    public void shareWechatMoment() {

    }

    @OnClick(R.id.fly_share_qq)
    public void shareQQ() {

    }

    @OnClick(R.id.fly_share_sina_weibo)
    public void shareSinaWeibo() {

    }

    @Override
    public void onResume() {
        super.onResume();
        showToast("fuck");
    }
}
