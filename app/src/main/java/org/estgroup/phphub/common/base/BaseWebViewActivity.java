package org.estgroup.phphub.common.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.polok.localify.LocalifyClient;
import com.github.polok.localify.module.LocalifyModule;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.Navigator;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;

import java.util.HashMap;

import butterknife.Bind;
import nucleus.presenter.Presenter;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.kennyc.view.MultiStateView.*;
import static org.estgroup.phphub.common.Constant.*;

public abstract class BaseWebViewActivity<PresenterType extends Presenter> extends BaseActivity<PresenterType> {

    @Bind(R.id.wv_content)
    public WebView contentView;

    protected WebSettings settings;

    protected final static String PLATFORM = "Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = contentView.getSettings();

        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    protected static class WebAppClient extends WebViewClient {
        private Context context;

        private Navigator navigator;

        private MultiStateView multiStateView;

        private WebView contentView;

        public WebAppClient(Context context,
                            Navigator navigator,
                            MultiStateView multiStateView,
                            WebView contentView) {
            this.context = context;
            this.navigator = navigator;
            this.multiStateView = multiStateView;
            this.contentView = contentView;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getHost().contains(PHPHUB_HOST)) {
                HashMap<String, String> segments = new HashMap<>();
                String key = null;
                for (String segment : uri.getPathSegments()) {
                    if (key == null) {
                        key = segment;
                    } else {
                        segments.put(key, segment);
                        key = null;
                    }
                }
                if (segments.size() > 0) {
                    if (segments.containsKey(PHPHUB_TOPIC_PATH) && !TextUtils.isEmpty(segments.get(PHPHUB_TOPIC_PATH))) {
                        url = String.format("%s%s?id=%s", DEEP_LINK_PREFIX, PHPHUB_TOPIC_PATH, segments.get(PHPHUB_TOPIC_PATH));
                    } else if (segments.containsKey(PHPHUB_USER_PATH) && !TextUtils.isEmpty(segments.get(PHPHUB_USER_PATH))) {
                        url = String.format("%s%s?id=%s", DEEP_LINK_PREFIX, PHPHUB_USER_PATH, segments.get(PHPHUB_USER_PATH));
                    }
                }
            }

            if (url.contains(DEEP_LINK_PREFIX)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
                }
            } else {
                navigator.navigateToWebView(context, url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (multiStateView != null) {
                multiStateView.setViewState(VIEW_STATE_LOADING);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (multiStateView != null) {
                multiStateView.setViewState(VIEW_STATE_CONTENT);
            }
            addImageClickEvent();
        }

        private void addImageClickEvent() {
            LocalifyModule localify = new LocalifyClient.Builder()
                    .withAssetManager(context.getAssets())
                    .build()
                    .localify();

            localify.rx()
                    .loadAssetsFile("js/ImageClickEvent.js")
                    .compose(new SchedulerTransformer<String>())
                    .filter(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String javascript) {
                            return !TextUtils.isEmpty(javascript);
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String javascript) {
                            contentView.loadUrl(javascript.replace("{platform}", PLATFORM));
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Logger.e(throwable.toString());
                        }
                    });
        }
    }

    protected static class WebAppInterface {
        private Context context;

        private Navigator navigator;

        public WebAppInterface(Context context, Navigator navigator) {
            this.context = context;
            this.navigator = navigator;
        }

        @JavascriptInterface
        public void openImage(String url) {
            navigator.navigateToGallery(context, url);
        }
    }
}
