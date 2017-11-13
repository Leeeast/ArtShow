package com.art.huakai.artshow.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.entity.ClientBean;


/**
 * Created by lidongliang on 2017/4/18.
 */
public class UpgradeDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = UpgradeDialog.class.getSimpleName();
    private static final String PARAMS_CONTENT = "PARAMS_CONTENT";

    private TextView tvContent;
    private ClientBean clientBean;
    private Toast toast;
    private boolean mIsMandatory;


    public static UpgradeDialog getInstance(ClientBean clientBean) {
        UpgradeDialog permissionRequestDialog = new UpgradeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_CONTENT, clientBean);
        permissionRequestDialog.setArguments(bundle);
        return permissionRequestDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            clientBean = (ClientBean) getArguments().getSerializable(PARAMS_CONTENT);
        }
        mIsMandatory = clientBean.clientVersion.isMandatory == 1;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        if (mIsMandatory) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            String exitTip = String.format(getString(R.string.upgrade_exit_tip), getString(R.string.app_name));
                            showToast(exitTip);
                        }
                        return true;
                    }
                    return false;
                }
            });
        } else {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        }
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.mystyleforred;
        dialogWindow.getAttributes().width = getResources().getDisplayMetrics().widthPixels;
        dialogWindow.setGravity(Gravity.CENTER);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_upgrade, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_down).setOnClickListener(this);
        view.findViewById(R.id.btn_exit).setOnClickListener(this);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText(clientBean.clientVersion.versionDescpt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down:
                try {
                    jumpOutBrowser(clientBean.clientVersion.downloadUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!mIsMandatory) {
                    dismiss();
                }
                break;
            case R.id.btn_exit:
                if (mIsMandatory) {
                    String exitTip = String.format(getString(R.string.upgrade_exit_tip), getString(R.string.app_name));
                    showToast(exitTip);
                    return;
                }
                dismiss();
                break;
        }
    }

    /**
     * 跳转到外部浏览器
     *
     * @param toUrl
     */
    private void jumpOutBrowser(String toUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(toUrl);
        intent.setData(content_url);
        getContext().startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }
}
