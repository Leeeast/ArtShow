package com.art.huakai.artshow.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        int i = v.getId();

        switch (v.getId()) {
            case R.id.btn_down:
                dismiss();
                break;
            case R.id.btn_exit:
                if (clientBean.clientVersion.isMandatory == 1) {
                    String exitTip = String.format(getString(R.string.upgrade_exit_tip), getString(R.string.app_name));
                    showToast(exitTip);
                    return;
                }
                dismiss();
                break;
        }
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
