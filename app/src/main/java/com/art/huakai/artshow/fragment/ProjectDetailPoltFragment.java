package com.art.huakai.artshow.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.WorksDetailBean;
import com.art.huakai.artshow.eventbus.ProjectNotifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ProjectDetailPoltFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_PROJECT = "PARAMS_PROJECT";

    private View scrollView;
    private WorksDetailBean mWorksDatailBean;
    private ImageView ivPageEmpty;
    private WebView webViewRich, webViewAward;
    private FrameLayout fLyAward;

    public static ProjectDetailPoltFragment newInstance(WorksDetailBean worksDetailBean) {
        ProjectDetailPoltFragment fragment = new ProjectDetailPoltFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_PROJECT, worksDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mWorksDatailBean = (WorksDetailBean) getArguments().getSerializable(PARAMS_PROJECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProjectDetailPoltFragment projectDetailPoltFragment = this;
        scrollView = inflater.inflate(R.layout.fragment_project_detail_polt, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        fLyAward = (FrameLayout) view.findViewById(R.id.fly_award);
        webViewRich = (WebView) view.findViewById(R.id.webview_rich);
        webViewAward = (WebView) view.findViewById(R.id.webview_award);
        if (mWorksDatailBean != null &&
                (!TextUtils.isEmpty(mWorksDatailBean.getPlot()) ||
                        !TextUtils.isEmpty(mWorksDatailBean.getAwardsDescpt()))) {
            ivPageEmpty.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mWorksDatailBean.getPlot())) {
                initWebView(webViewRich);
                setRichText(webViewRich, mWorksDatailBean.getPlot());
            }
            if (!TextUtils.isEmpty(mWorksDatailBean.getAwardsDescpt())) {
                fLyAward.setVisibility(View.VISIBLE);
                initWebView(webViewAward);
                setRichText(webViewAward, mWorksDatailBean.getAwardsDescpt());
            } else {
                fLyAward.setVisibility(View.GONE);
            }

        } else {
            ivPageEmpty.setVisibility(View.VISIBLE);
            webViewRich.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化webview相关
     */
    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(webView.getSettings()
                    .MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
    }

    /**
     * 解析富文本
     *
     * @param htmltext
     * @return
     */
    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }

    /**
     * 显示富文本
     *
     * @param content
     */
    public void setRichText(WebView webView, String content) {
        String STYLE_RICH = "<font color=\"#928470\">%s</font>";
        @SuppressLint("DefaultLocale")
        String contentNewStyle = String.format(
                STYLE_RICH,
                content);
        webView.loadDataWithBaseURL(
                null,
                getNewContent(contentNewStyle),
                "text/html",
                "UTF-8", null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View getScrollableView() {
        ProjectDetailPoltFragment projectDetailPoltFragment = this;
        return scrollView;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(ProjectNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        try {
            ProjectDetailInfo p = ProjectDetailInfo.getInstance();
            switch (event.getActionCode()) {
                case ProjectNotifyEvent.NOTIFY_INTRODUCE_SHOW:
                    if (!TextUtils.isEmpty(p.getPlot())) {
                        setRichText(webViewRich, p.getPlot());
                    }
                    break;
                case ProjectNotifyEvent.NOTIFY_AWARD_DES:
                    if (!TextUtils.isEmpty(p.getAwardsDescpt())) {
                        fLyAward.setVisibility(View.VISIBLE);
                        setRichText(webViewAward, p.getAwardsDescpt());
                    } else {
                        fLyAward.setVisibility(View.GONE);
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
