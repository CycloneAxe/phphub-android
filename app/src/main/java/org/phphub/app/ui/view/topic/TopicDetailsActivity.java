package org.phphub.app.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Link;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.TopicDetailPresenter;

import java.util.Map;

import butterknife.Bind;
import io.nlopez.smartadapters.utils.ViewEventListener;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.VIEW_STATE_ERROR;

@RequiresPresenter(TopicDetailPresenter.class)
public class TopicDetailsActivity extends BaseActivity<TopicDetailPresenter> {
    private static final String INTENT_EXTRA_PARAM_TOPIC_ID = "topic_id";

    int topicId;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshLayout;

    @Bind(R.id.wv_content)
    WebView topicContentView;

    @Bind(R.id.tv_username)
    TextView userNameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_reply_count)
    Text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PresenterFactory<TopicDetailPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicDetailPresenter>() {
            @Override
            public TopicDetailPresenter createPresenter() {
                TopicDetailPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        topicId = intent.getIntExtra(INTENT_EXTRA_PARAM_TOPIC_ID, 0);

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        refreshLayout.autoRefresh();

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getPresenter().request(topicId);
            }
        });
        getPresenter().request(topicId);
    }

    public void initView(Topic topic) {
        Link link = topic.getLinks();
        User user = topic.getUser().getData();

        avatarView.setImageURI(Uri.parse(user.getAvatar()));
        userNameView.setText(user.getName());
        signView.setText(user.getSignature());
        topicContentView.loadUrl(link.getDetailsWebView(), getHttpHeaderAuth());

        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        refreshLayout.finishRefresh();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.topic_details;
    }

    public static Intent getCallingIntent(Context context, int TopicId) {
        Intent callingIntent = new Intent(context, TopicDetailsActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_TOPIC_ID, TopicId);
        return callingIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    public void onNetworkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }


}
