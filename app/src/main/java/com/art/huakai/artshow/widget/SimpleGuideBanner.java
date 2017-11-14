package com.art.huakai.artshow.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.utils.ViewFindUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;

import static com.art.huakai.artshow.utils.ViewFindUtil.find;

public class SimpleGuideBanner extends BaseIndicatorBanner<Integer, SimpleGuideBanner> {
    public SimpleGuideBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.layout_guide_adapter, null);
        SimpleDraweeView sdv = find(inflate, R.id.sdv);
        LinearLayout lLyJump = find(inflate, R.id.lly_jump);
        Button btnLogin = ViewFindUtil.find(inflate, R.id.btn_login);
        Button btnJump = ViewFindUtil.find(inflate, R.id.btn_jump);

        final Integer resId = mDatas.get(position);
        lLyJump.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);
        //sdv.setBackground(getResources().getDrawable(resId));
        Uri uri = Uri.parse("res://com.art.huakai.artshow/" + resId);
        sdv.setImageURI(uri);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickL != null)
                    onJumpClickL.onJumpLogin();
            }
        });
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJumpClickL != null)
                    onJumpClickL.onJumpClick();
            }
        });
        return inflate;
    }

    private OnJumpClickL onJumpClickL;

    public interface OnJumpClickL {
        void onJumpClick();

        void onJumpLogin();
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }
}
