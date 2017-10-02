package com.art.huakai.artshow.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.utils.AnimUtils;


public class LoadingMoreFooter extends LinearLayout {

//    private SimpleViewSwitcher progressCon;
     public final static int STATE_LOADING = 0;
     public final static int STATE_COMPLETE = 1;
     public final static int STATE_NOMORE = 2;
     //    private TextView mText;
     //    private String loadingHint;
     //    private String noMoreHint;
     //    private String loadingDoneHint;
     ImageView iv_loading;
     //    private AVLoadingIndicatorView progressView;

     private LinearLayout mContainer;
     private TextView tv_loading;

     public LoadingMoreFooter(Context context) {
     super(context);
     initView();
     }

     /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

//    public void setLoadingHint(String hint) {
//        loadingHint = hint;
//    }
//
//    public void setNoMoreHint(String hint) {
//        noMoreHint = hint;
//    }
//
//    public void setLoadingDoneHint(String hint) {
//        loadingDoneHint = hint;
//    }

    public void initView() {
//        setGravity(Gravity.CENTER);
//        setLayoutParams(new RecyclerView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        progressCon = new SimpleViewSwitcher(getContext());
//        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        progressView = new AVLoadingIndicatorView(this.getContext());
//        progressView.setIndicatorColor(0xffB5B5B5);
//        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
//        progressCon.setView(progressView);

//        addView(progressCon);
//        mText = new TextView(getContext());
//        mText.setText(R.string.listview_loading);
//        loadingHint = (String) getContext().getText(R.string.listview_loading);
//        noMoreHint = (String) getContext().getText(R.string.nomore_loading);
//        loadingDoneHint = (String) getContext().getText(R.string.loading_done);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);
//        mText.setLayoutParams(layoutParams);
//        addView(mText);
        ImageView iv_progress=new ImageView(getContext());
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.BOTTOM);
        iv_loading= (ImageView) findViewById(R.id.iv_loading);
        tv_loading= (TextView) findViewById(R.id.tv_loading);

    }

//    public void setProgressStyle(int style) {
//        if (style == ProgressStyle.SysProgress) {
//            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
//        } else {
//            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
//            progressView.setIndicatorColor(0xffB5B5B5);
//            progressView.setIndicatorId(style);
//            progressCon.setView(progressView);
//        }
//    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
//                progressCon.setVisibility(View.VISIBLE);
//                mText.setText(loadingHint);
                iv_loading.setVisibility(View.VISIBLE);
                AnimUtils.rotate(iv_loading);
                tv_loading.setText(R.string.loading);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                tv_loading.setText(R.string.loading_done);
                iv_loading.clearAnimation();
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                iv_loading.clearAnimation();
                iv_loading.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

//    public TextView getFooterTextView() {
//        return mText;
//    }
//
//    public AVLoadingIndicatorView getFooterProgressBar() {
//        return progressView;
//    }
}
