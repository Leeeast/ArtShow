package com.art.huakai.artshow.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.base.ShowApplication;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.InfoBaseResp;
import com.art.huakai.artshow.utils.Util;
import com.art.huakai.artshow.wxapi.WXEntryActivity;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.OnClick;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class ShareDialog extends BaseDialogFragment implements WbShareCallback, IUiListener {
    public static final String PARAMS_TITLE = "PARAMS_TITLE";
    public static final String PARAMS_URL = "PARAMS_URL";
    private String mTitle;
    private String mUrl;
    private IWXAPI wxapi;
    private ShowProgressDialog showProgressDialog;
    private WbShareHandler mShareHandler;
    private Tencent mTencent;

    public static ShareDialog newInstence(String title, String url) {
        ShareDialog typeConfirmDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_TITLE, title);
        bundle.putString(PARAMS_URL, url);
        typeConfirmDialog.setArguments(bundle);
        return typeConfirmDialog;
    }

    /**
     * 注册微博相关
     *
     * @param activity
     * @return
     */
    public static WbShareHandler regToWeibo(Activity activity) {
        WbSdk.install(activity, new AuthInfo(activity, Constant.WB_APP_KEY, Constant.WB_REDIRECT_URL, null));
        WbShareHandler mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        return mShareHandler;
    }

    public void setShareHandler(WbShareHandler wbShareHandler) {
        this.mShareHandler = wbShareHandler;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mTitle = bundle.getString(PARAMS_TITLE);
            mUrl = bundle.getString(PARAMS_URL);
        }
        showProgressDialog = new ShowProgressDialog(getContext());
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
        return R.layout.dialog_share_new;
    }

    @Override
    public void initView(View rootView) {
    }


    @Override
    public void setView() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.ShareStyle);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.base_dialog;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        Window window = getActivity().getWindow();
        window.setBackgroundDrawableResource(R.color.share_bg);
        return dialog;
    }

    @OnClick(R.id.btn_cancle)
    public void btnCancel() {
        this.dismiss();
    }

//    @OnClick(R.id.tv_url_copy)
//    public void urlCopy() {
//        if (!TextUtils.isEmpty(mUrl)) {
//            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//            // 创建普通字符型ClipData
//            ClipData mClipData = ClipData.newPlainText("SHARE", mUrl);
//            // 将ClipData内容放到系统剪贴板里。
//            cm.setPrimaryClip(mClipData);
//            showToast(getString(R.string.tip_share_copy_suc));
//        } else {
//            showToast(getString(R.string.tip_share_url_err));
//        }
//    }

//    @OnClick(R.id.tv_url_open)
//    public void urlOpenWidthBrowser() {
//        try {
//            if (!TextUtils.isEmpty(mUrl)) {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(mUrl);
//                intent.setData(content_url);
//                getContext().startActivity(intent);
//            } else {
//                showToast(getString(R.string.tip_share_url_err));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @OnClick(R.id.fly_share_wechat)
    public void shareWechat() {
        showProgressDialog.show();
        if (wxapi.isWXAppInstalled()) {
            reigstWeixinLoginRiciver();
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
        } else {
            if (showProgressDialog.isShowing()) {
                showProgressDialog.dismiss();
            }
            showToast(getString(R.string.weixin_no));
        }
    }

    /**
     * 用来接收从WXEntryActivity传入的微信回调的广播接收器
     */
    private void reigstWeixinLoginRiciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXEntryActivity.ACTION_WECHAT_SEND_MESSAGE);
        filter.setPriority(Integer.MAX_VALUE);
        getActivity().registerReceiver(wxReceiver, filter);
    }

    private BroadcastReceiver wxReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getSerializableExtra("onResp") != null) {
                onResp((InfoBaseResp) intent.getSerializableExtra("onResp"));
            }
        }
    };

    public void onResp(InfoBaseResp baseResp) {
        String authCode = baseResp.getCode();
        String state = baseResp.getState();
        int errCode = baseResp.getErrCode();
        switch (baseResp.getErrCode()) {
            case BaseResp.ErrCode.ERR_OK:
                showToast(getString(R.string.share_success));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                showToast(getString(R.string.share_cancel));
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                showToast(getString(R.string.share_fail));
                break;
            default:
                break;
        }
        getActivity().unregisterReceiver(wxReceiver);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @OnClick(R.id.fly_share_wechat_momnet)
    public void shareWechatMoment() {
        showProgressDialog.show();
        if (wxapi.isWXAppInstalled()) {
            reigstWeixinLoginRiciver();
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
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            wxapi.sendReq(req);
        } else {
            if (showProgressDialog.isShowing()) {
                showProgressDialog.dismiss();
            }
            showToast(getString(R.string.weixin_no));
        }
    }

    @OnClick(R.id.fly_share_qq)
    public void shareQQ() {
        QQshare(getContext(), mUrl);
//        Bundle bundle = new Bundle();
//        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(mUrl, "http://connect.qq.com/");
//        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
//        bundle.putString(Constants.PARAM_TITLE, "我在测试");
//        //分享的图片URL
//        bundle.putString(Constants.PARAM_IMAGE_URL, "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
//        //分享的消息摘要，最长50个字
//        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "测试");
//        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
//        bundle.putString(Constants.PARAM_APPNAME, "??我在测试");
//        //标识该消息的来源应用，值为应用名称+AppId。
//        bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + AppId);
//        mTencent.shareToQQ(this, bundle, listener);
    }

    public void QQshare(Context context, String shareUrl) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constant.QQ_APPID, ShowApplication.getAppContext());
        }
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mTitle);
        //分享的图片URL
//        if (bitmapForShare != null) {
//            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ImageUrl);
//        } else {
//            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
//                    "http://show.sina.com.cn/images/SinaShow.ico");
//        }
        //bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, "file:///android_asset/icon_share_img.png");
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, getContext().getString(R.string.tip_share_url_des));
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        //标识该消息的来源应用，值为应用名称+AppId。
        //bundle.putString(QQShare.SHARE_TO_QQ_APP_SOURCE, "星期几" + AppId);
        //bundle.putInt(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://sina.show.com");
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        mTencent.shareToQQ((Activity) context, bundle, this);

    }


    @OnClick(R.id.fly_share_sina_weibo)
    public void shareSinaWeibo() {
        if (mShareHandler == null) {
            return;
        }
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        weiboMessage.mediaObject = getWebpageObj();
        weiboMessage.imageObject = getImageObj();
        showProgressDialog.show();
        mShareHandler.shareMessage(weiboMessage, false);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        //textObject.title = mTitle;
        textObject.text = mTitle;
        //textObject.description = getContext().getString(R.string.tip_share_url_des);
        textObject.actionUrl = mUrl;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_share_img);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//        bitmap.recycle();
        //textObject.setThumbImage(bitmap);
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_share_img);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        //mediaObject.title = mTitle;
        mediaObject.description = getContext().getString(R.string.tip_share_url_des);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_share_img);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        mediaObject.setThumbImage(thumbBmp);
        mediaObject.actionUrl = mUrl;
        mediaObject.defaultText = getContext().getString(R.string.tip_share_url_des);
        return mediaObject;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showProgressDialog != null && showProgressDialog.isShowing()) {
            showProgressDialog.dismiss();
        }
        if (isVisible()) {
            dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (showProgressDialog.isShowing()) {
            showProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(wxReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWbShareSuccess() {
        showToast(getString(R.string.share_success));
        if (showProgressDialog.isShowing()) {
            showProgressDialog.dismiss();
        }
    }

    @Override
    public void onWbShareCancel() {
        showToast(getString(R.string.share_cancel));
        if (showProgressDialog.isShowing()) {
            showProgressDialog.dismiss();
        }
    }

    @Override
    public void onWbShareFail() {
        showToast(getString(R.string.share_fail));
        if (showProgressDialog.isShowing()) {
            showProgressDialog.dismiss();
        }
    }

    @Override
    public void onComplete(Object o) {
        showToast(getString(R.string.share_success));
    }

    @Override
    public void onError(UiError uiError) {
        showToast(getString(R.string.share_fail));
    }

    @Override
    public void onCancel() {
        showToast(getString(R.string.share_cancel));
    }
}
