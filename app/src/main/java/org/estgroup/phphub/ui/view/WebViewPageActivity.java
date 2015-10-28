package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;

public class WebViewPageActivity extends BaseActivity {
    private static final String WEB_URL = "web_url";

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.wv_content)
    WebView webContentView;

    String urlString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlString = getIntent().getStringExtra(WEB_URL);

        toolbarTitleView.setText(getString(R.string.loading));

        String userAgent = String.format("%s PHPHubBroswer/%s",
                getUserAgent(),
                BuildConfig.VERSION_NAME);


        webContentView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                webContentView.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                // TODO Auto-generated method stub
                Toast.makeText(WebViewPageActivity.this, "Oh no! " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });

        webContentView.getSettings().setUserAgentString(userAgent);
        webContentView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webContentView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webContentView.getSettings().setDefaultTextEncodingName("utf-8");
        webContentView.getSettings().setDomStorageEnabled(true);
        webContentView.loadUrl(urlString);

        webContentView.setWebChromeClient(new WebChromeClient() {
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
