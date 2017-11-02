package com.art.huakai.artshow.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.CommonTipDialog;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.InfoBaseResp;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.LoginEvent;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.fragment.BindPhoneFragment;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 账户信息
 * Created by lidongliang on 2017/10/14.
 */

public class BindWechatActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.fly_right_img)
    FrameLayout fLyRightImg;
    @BindView(R.id.switch_bind_wechat)
    Switch switchBingWechat;
    private ShowProgressDialog showProgressDialog;
    private CommonTipDialog commonTip;
    private IWXAPI wxapi;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_bind_wechat;
    }

    @Override
    public void initData() {
        showProgressDialog = new ShowProgressDialog(this);
        regToWx();
    }

    /**
     * 微信相关初始化
     */
    private void regToWx() {
        wxapi = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APPID, true);
        wxapi.registerApp(Constant.WEIXIN_APPID);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.account_bind_wachat);
        fLyRightImg.setVisibility(View.GONE);
        ivRightImg.setImageResource(R.drawable.ic_more_horiz);
    }

    @Override
    public void setView() {
        switchBingWechat.setChecked(LocalUserInfo.getInstance().getWechatOpenid().equals("true"));
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    /**
     * title右边选项
     */
    @OnClick(R.id.iv_right_img)
    public void moreSelect() {
        Toast.makeText(this, "攻城狮正在开发中...", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.view_bind_wechat)
    public void viewBindWechat() {
        if (switchBingWechat.isChecked()) {
            if (commonTip == null) {
                String content = String.format(getString(R.string.wechat_unbind_tip), getString(R.string.app_name));
                commonTip = CommonTipDialog.getInstance(content, getString(R.string.cancel), getString(R.string.wechat_unbind));
                commonTip.setOnDismissListener(new CommonTipDialog.OnDismissListener() {
                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void sure() {
                        unBindWX();
                    }
                });
            }
            commonTip.show(getSupportFragmentManager(), "COMMONTIP.DIALOG");
        } else {
            showProgressDialog.show();
            if (wxapi.isWXAppInstalled()) {
                reigstWeixinLoginRiciver();
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                wxapi.sendReq(req);
            } else {
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showToast(getString(R.string.weixin_no));
            }
        }
    }

    /**
     * 用来接收从WXEntryActivity传入的微信回调的广播接收器
     */
    private void reigstWeixinLoginRiciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXEntryActivity.ACTION_WECHAT_AUTH_MESSAGE);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(wxReceiver, filter);
    }

    private BroadcastReceiver wxReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getSerializableExtra("onResp") != null) {
                onResp((InfoBaseResp) intent.getSerializableExtra("onResp"));
            }
        }
    };

    public void onResp(InfoBaseResp arg0) {
        final String authCode = arg0.getCode();
        String state = arg0.getState();
        int errCode = arg0.getErrCode();
        if (errCode == 0) {// 用户同意授权
            bindWX(authCode);
        } else {
            if (showProgressDialog.isShowing()) {
                showProgressDialog.dismiss();
            }
        }
        unregisterReceiver(wxReceiver);
    }

    //绑定微信
    public void bindWX(String authCode) {
        Map<String, String> params = new TreeMap<>();
        params.put("wxcode", authCode);
        params.put("scene", "afterLogin");
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params :" + params);
        RequestUtil.request(true, Constant.URL_USER_WXBIND, params, 22, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.wechat_bind_success));
                    switchBingWechat.setChecked(true);
                    LocalUserInfo.getInstance().setWechatOpenid("true");
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showToast(getString(R.string.wechat_bind_fail));
            }
        });
    }

    //解绑微信
    public void unBindWX() {
        Map<String, String> params = new TreeMap<>();
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params :" + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_USER_WXUNBIND, params, 23, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    showToast(getString(R.string.wechat_unbind_success));
                    switchBingWechat.setChecked(false);
                    LocalUserInfo.getInstance().setWechatOpenid("false");
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                showToast(getString(R.string.wechat_unbind_fail));
            }
        });
    }

}
