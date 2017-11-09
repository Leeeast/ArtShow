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
import com.art.huakai.artshow.entity.TalentDetailBean;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.eventbus.TalentNotifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class PersonalDetailworksFragment extends HeaderViewPagerFragment {
    private static final String PARAMS_TALENT = "PARAMS_TALENT";
    private View scrollView;
    private TalentDetailBean mTalentDetailBean;
    private ImageView ivPageEmpty;
    private WebView webViewRich;

    public static PersonalDetailworksFragment newInstance(TalentDetailBean talentDetailBean) {
        PersonalDetailworksFragment personalDetailworksFragment = new PersonalDetailworksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS_TALENT, talentDetailBean);
        personalDetailworksFragment.setArguments(bundle);
        return personalDetailworksFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTalentDetailBean = (TalentDetailBean) getArguments().getSerializable(PARAMS_TALENT);
        }
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_scrollview, container, false);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPageEmpty = (ImageView) view.findViewById(R.id.iv_page_empty);
        webViewRich = (WebView) view.findViewById(R.id.webview_rich);
        if (mTalentDetailBean != null && !TextUtils.isEmpty(mTalentDetailBean.getWorksDescpt())) {
            ivPageEmpty.setVisibility(View.GONE);
            webViewRich.setVisibility(View.VISIBLE);
            initWebView();
            setRichText(mTalentDetailBean.getWorksDescpt());
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
        //String CSS_STYLE = "<style>* {font-size:160px;line-height:20px;}p {color:#928470;}</style>";
        //String CSS_STYLE_ALL = "<style>* {font-size:16px;line-height:20px;} p {color:#f00;} a {color:#3E62A6;} img {max-width:310px;}pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}</style>";
        //String STYLE_RICH = "<font size=\"3\" color=\"#928470\">%s</font>";
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
    public void onEventLogin(TalentNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isDetached()) {
            return;
        }
        TalentDetailInfo t = TalentDetailInfo.getInstance();
        switch (event.getActionCode()) {
            case TalentNotifyEvent.NOTIFY_WORKS_DES:
                if (!TextUtils.isEmpty(t.getWorksDescpt())) {
                    ivPageEmpty.setVisibility(View.GONE);
                    webViewRich.setVisibility(View.VISIBLE);
                    setRichText(t.getWorksDescpt());
                } else {
                    ivPageEmpty.setVisibility(View.VISIBLE);
                    webViewRich.setVisibility(View.GONE);
                }
                break;
        }
    }
}
