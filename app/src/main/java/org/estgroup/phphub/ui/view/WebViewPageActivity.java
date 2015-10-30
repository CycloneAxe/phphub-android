package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kennyc.view.MultiStateView;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseWebViewActivity;

import butterknife.Bind;

public class WebViewPageActivity extends BaseWebViewActivity {
    private static final String WEB_URL = "web_url";

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    String urlString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlString = getIntent().getStringExtra(WEB_URL);

        toolbarTitleView.setText(getString(R.string.loading));

        String userAgent = String.format("%s PHPHubBroswer/%s",
                getUserAgent(),
                BuildConfig.VERSION_NAME);


        contentView.setWebViewClient(new WebAppClient(this, navigator, multiStateView, contentView));
        settings.setUserAgentString(userAgent);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        contentView.loadUrl(urlString);

        contentView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbarTitleView.setText(title);
            }
        });
    }

    public static Intent getCallingIntent(Context context, String webUrl) {
        Intent callingIntent = new Intent(context, WebViewPageActivity.class);
        callingIntent.putExtra(WEB_URL, webUrl);
        return callingIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.webview_page;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.loading);
    }

    private String getUserAgent() {
        if (Build.VERSION.SDK_INT < 19) {
            WebView webView = new WebView(this);
            WebSettings settings = webView.getSettings();
            return settings.getUserAgentString();
        }

        // api >= 19
        return WebSettings.getDefaultUserAgent(this);
    }
}
