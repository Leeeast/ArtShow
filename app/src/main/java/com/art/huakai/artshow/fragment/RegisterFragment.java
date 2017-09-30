package com.art.huakai.artshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;

/**
 * 注册新账户Fragment
 * Created by lidongliang on 2017/9/27.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView(View rootView) {
        TextView tvProtocol = (TextView) rootView.findViewById(R.id.tv_protocol);
        SpannableString spannableString = new SpannableString(getString(R.string.register_protocol_tip));
        spannableString.setSpan(
                new TextAppearanceSpan(getContext(), R.style.protocol_style_gray),
                0,
                spannableString.length() - 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(
                new TextAppearanceSpan(getContext(), R.style.protocol_style_blue),
                spannableString.length() - 7,
                spannableString.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(spannableString);


        rootView.findViewById(R.id.lly_back).setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_protocol:
                break;
        }
    }
}
