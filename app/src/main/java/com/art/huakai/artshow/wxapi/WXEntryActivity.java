package com.art.huakai.artshow.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.entity.InfoBaseResp;
import com.art.huakai.artshow.utils.LogUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    public final static String ACTION_WECHAT_AUTH_MESSAGE = "ACTION_WECHAT_AUTH_MESSAGE";
    public final static String ACTION_WECHAT_SEND_MESSAGE = "ACTION_WECHAT_SEND_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG, TAG + "zc onCreate");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APPID, false);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.i(TAG, TAG + "zc onNewIntent回调");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     */
    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        LogUtil.i(TAG, "zc onReq回调");
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     */
    @Override
    public void onResp(BaseResp arg0) {
        LogUtil.i(TAG, "zc onResp调用,errCode:" + arg0.errCode + "===arg0"
                + arg0.getClass());// -4
        Intent intent = new Intent();
        InfoBaseResp info = new InfoBaseResp(arg0);
        if (arg0 instanceof SendAuth.Resp) {
            intent.setAction(ACTION_WECHAT_AUTH_MESSAGE);
        } else if (arg0 instanceof SendMessageToWX.Resp) {
            intent.setAction(ACTION_WECHAT_SEND_MESSAGE);
        }
        intent.putExtra("onResp", info);
        sendBroadcast(intent, null);
        LogUtil.i(TAG, "zc onResp已经发送广播");
        finish();
    }
}
