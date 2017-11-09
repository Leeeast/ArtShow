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
import android.widget.ImageView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreNotifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TheatreDetailDesFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_THEATRE = "PARAMS_THEATRE";
    private View scrollView;
    private TheatreDetailBean mTheatreDetailBean;
    private ImageView ivPageEmpty;
    private WebView webViewRich;

    public static TheatreDetailDesFragment newInstance(TheatreDetailBean theatreDetailBean) {
        TheatreDetailDesFragment fragment = new TheatreDetailDesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_THEATRE, theatreDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mTheatreDetailBean = (TheatreDetailBean) getArguments().getSerializable(PARAMS_THEATRE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_theatre_detail_des, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        webViewRich = (WebView) view.findViewById(R.id.webview_rich);
        if (mTheatreDetailBean != null && !TextUtils.isEmpty(mTheatreDetailBean.getDetailedIntroduce())) {
            ivPageEmpty.setVisibility(View.GONE);
            webViewRich.setVisibility(View.VISIBLE);
            initWebView();
            setRichText(mTheatreDetailBean.getDetailedIntroduce());
        } else {
            ivPageEmpty.setVisibility(View.VISIBLE);
            webViewRich.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化webview相关
     */
    private void initWebView() {
        WebSettings settings = webViewRich.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webViewRich.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webViewRich.setWebChromeClient(new WebChromeClient());
        webViewRich.setWebViewClient(new WebViewClient());
        webViewRich.getSettings().setDefaultTextEncodingName("UTF-8");
        webViewRich.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewRich.getSettings().setMixedContentMode(webViewRich.getSettings()
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
    public void setRichText(String content) {
        if (webViewRich == null) {
            return;
        }
        String STYLE_RICH = "<font color=\"#928470\">%s</font>";
        @SuppressLint("DefaultLocale")
        String contentNewStyle = String.format(
                STYLE_RICH,
                content);
        webViewRich.loadDataWithBaseURL(
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
        return scrollView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(TheatreNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        TheatreDetailInfo t = TheatreDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TheatreNotifyEvent.NOTIFY_THEATRE_INTRODUCE_DETAIL:

                if (!TextUtils.isEmpty(t.getDetailedIntroduce())) {
                    ivPageEmpty.setVisibility(View.GONE);
                    webViewRich.setVisibility(View.VISIBLE);
                    initWebView();
                    setRichText(t.getDetailedIntroduce());
                } else {
                    ivPageEmpty.setVisibility(View.VISIBLE);
                    webViewRich.setVisibility(View.GONE);
                }

                break;
        }
    }

}
