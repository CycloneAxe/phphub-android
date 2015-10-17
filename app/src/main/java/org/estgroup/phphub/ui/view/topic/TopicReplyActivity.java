package org.estgroup.phphub.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Reply;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.TopicReplyPresenter;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(TopicReplyPresenter.class)
public class TopicReplyActivity extends BaseActivity<TopicReplyPresenter> {
    private static final String TOPIC_ID = "topic_id";
    private static final String TOPIC_REPLY_URL = "reply_url";

    @Bind(R.id.et_topic_title)
    EditText topicTitleView;

    @Bind(R.id.et_topic_body)
    EditText topicBodyView;

    @Bind(R.id.tv_select_node)
    TextView selectNodeView;

    int topicId;

    String topicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topicTitleView.setVisibility(View.GONE);
        selectNodeView.setVisibility(View.GONE);

        this.topicId = getIntent().getIntExtra(TOPIC_ID, 0);
        this.topicUrl = getIntent().getStringExtra(TOPIC_REPLY_URL);
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<TopicReplyPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicReplyPresenter>() {
            @Override
            public TopicReplyPresenter createPresenter() {
                TopicReplyPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_posting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            validationContent();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context, int topicId, String replyUrl) {
        Intent callingIntent = new Intent(context, TopicReplyActivity.class);
        callingIntent.putExtra(TOPIC_ID, topicId);
        callingIntent.putExtra(TOPIC_REPLY_URL, replyUrl);
        return callingIntent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.publish_topic;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.publish_reply);
    }

    public void validationContent() {
        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        SweetAlertDialog loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        String reply = topicBodyView.getText().toString();

        if (reply.trim().length() < 1) {
            errorDialog.setTitleText("Oops...");
            errorDialog.setContentText(getString(R.string.reply_empty));
            errorDialog.show();
            return;
        }

        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#4394DA"));
        loadingDialog.setContentText(getString(R.string.submitting));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        System.out.println(String.valueOf(topicId));
        getPresenter().request(topicId, reply);
    }

    public void onPublicSuccessful(Reply reply) {
        finish();
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }
}
