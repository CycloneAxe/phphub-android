package org.estgroup.phphub.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.presenter.TopicPublishPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(TopicPublishPresenter.class)
public class TopicPublishActivity extends BaseActivity<TopicPublishPresenter> {

    @Bind(R.id.et_topic_title)
    EditText topicTitleView;

    @Bind(R.id.et_topic_body)
    EditText topicBodyView;

    @Bind(R.id.tv_select_node)
    TextView selectNodeView;

    @Inject
    TopicModel topicModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, TopicPublishActivity.class);
        return callingIntent;
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<TopicPublishPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicPublishPresenter>() {
            @Override
            public TopicPublishPresenter createPresenter() {
                TopicPublishPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.publish_topic;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.publish_topic);
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

    public void validationContent() {
        String topicTitle = topicTitleView.getText().toString();
        String topicBody = topicBodyView.getText().toString();
        int nodeId = (int) selectNodeView.getTag();

        if (topicTitle.trim().length() < 2) {

        } else if (topicBody.trim().length() < 2) {

        } else if (nodeId == 0) {

        }
    }

    public void onPublishSuccessful(Topic topic) {
        finish();
        navigator.navigateToTopicDetails(this, topic.getId());
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
        Toast.makeText(this, getString(R.string.publish_error), Toast.LENGTH_SHORT).show();
    }

}
