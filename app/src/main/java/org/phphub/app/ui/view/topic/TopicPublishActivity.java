package org.phphub.app.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.TopicPublishPresenter;

import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(TopicPublishPresenter.class)
public class TopicPublishActivity extends BaseActivity<TopicPublishPresenter> {

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
        return super.onOptionsItemSelected(item);
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
