package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kennyc.view.MultiStateView;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseWebViewActivity;

import butterknife.Bind;

public class ReplyListActivity extends BaseWebViewActivity {
    private static final String TOPIC_ID = "topic_id";
    private static final String TOPIC_REPLY_URL = "reply_url";

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    int topicId;

    String replyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.replyUrl = getIntent().getStringExtra(TOPIC_REPLY_URL);
        this.topicId = getIntent().getIntExtra(TOPIC_ID, 0);
        contentView.loadUrl(replyUrl, getHttpHeaderAuth());
        settings.setJavaScriptEnabled(true);

        contentView.setWebViewClient(new WebAppClient(this, navigator, multiStateView, contentView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_publish, menu);

        if (getIntent().getIntExtra(TOPIC_ID, 0) == 0) {
            menu.findItem(R.id.action_publish).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_publish:

                navigator.navigateToReplyTopic(this, topicId, replyUrl);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context, String replyUrl) {
        Intent callingIntent = new Intent(context, ReplyListActivity.class);
        callingIntent.putExtra(TOPIC_ID, 0);
        callingIntent.putExtra(TOPIC_REPLY_URL, replyUrl);
        return callingIntent;
    }

    public static Intent getCallingIntent(Context context, int topicId, String replyUrl) {
        Intent callingIntent = new Intent(context, ReplyListActivity.class);
        callingIntent.putExtra(TOPIC_ID, topicId);
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
