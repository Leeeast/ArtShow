package com.art.huakai.artshow.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.art.huakai.artshow.R;


/**
 * Created by lidongliang on 2017/4/18.
 */
public class CommonTipDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = CommonTipDialog.class.getSimpleName();
    private static final String PARAMS_CONTENT = "PARAMS_CONTENT";
    private static final String PARAMS_CANCEL_TEXT = "PARAMS_CANCEL_TEXT";
    private static final String PARAMS_AFFIRM_TEXT = "PARAMS_AFFIRM_TEXT";


    private OnDismissListener mOnDismissListener;
    private String mConstent;
    private String mCancel;
    private String mAffirm;

    public static CommonTipDialog getInstance(String content, String cancel, String affirm) {
        CommonTipDialog permissionRequestDialog = new CommonTipDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_CONTENT, content);
        bundle.putString(PARAMS_CANCEL_TEXT, cancel);
        bundle.putString(PARAMS_AFFIRM_TEXT, affirm);
        permissionRequestDialog.setArguments(bundle);
        return permissionRequestDialog;
    }

    public interface OnDismissListener {
        void cancel();

        void sure();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mConstent = getArguments().getString(PARAMS_CONTENT);
            mCancel = getArguments().getString(PARAMS_CANCEL_TEXT);
            mAffirm = getArguments().getString(PARAMS_AFFIRM_TEXT);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.mystyleforred;
        dialogWindow.getAttributes().width = getResources().getDisplayMetrics().widthPixels;
        dialogWindow.setGravity(Gravity.CENTER);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_common_tip, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnSure = (Button) view.findViewById(R.id.btn_sure);
        if (!TextUtils.isEmpty(mConstent)) {
            tvContent.setText(mConstent);
        }
        if (!TextUtils.isEmpty(mCancel)) {
            btnCancel.setText(mCancel);
        }
        if (!TextUtils.isEmpty(mAffirm)) {
            btnSure.setText(mAffirm);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dismiss();
            if (mOnDismissListener != null) {
                mOnDismissListener.cancel();
            }

        } else if (i == R.id.btn_sure) {
            dismiss();
            if (mOnDismissListener != null) {
                mOnDismissListener.sure();
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
