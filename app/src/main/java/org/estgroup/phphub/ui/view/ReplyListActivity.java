package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kennyc.view.MultiStateView;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.Constant;
import org.estgroup.phphub.common.base.BaseActivity;

import butterknife.Bind;

public class ReplyListActivity extends BaseActivity {
    private static final String TOPIC_REPLY_URL = "reply_url";

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.wv_content)
    WebView contentView;

    String replyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.replyUrl = getIntent().getStringExtra(TOPIC_REPLY_URL);
        contentView.loadUrl(replyUrl, getHttpHeaderAuth());
        contentView.getSettings().setJavaScriptEnabled(true);

        contentView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(Constant.DEEP_LINK_PREFIX)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        startActivity(Intent.createChooser(intent, "请选择浏览器"));
                    }
                }
                return true;
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
        });
    }

    public static Intent getCallingIntent(Context context, String replyUrl) {
        Intent callingIntent = new Intent(context, ReplyListActivity.class);
        callingIntent.putExtra(TOPIC_REPLY_URL, replyUrl);
        return callingIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.reply_list;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.reply_list);
    }
}
