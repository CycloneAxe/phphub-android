package org.phphub.app.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kennyc.view.MultiStateView;
import com.kmshack.topscroll.TopScrollHelper;
import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Link;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.api.entity.element.User;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.TopicDetailPresenter;

import butterknife.Bind;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.*;

@RequiresPresenter(TopicDetailPresenter.class)
public class TopicDetailsActivity extends BaseActivity<TopicDetailPresenter> {
    private static final String INTENT_EXTRA_PARAM_TOPIC_ID = "topic_id";

    int topicId;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.wv_content)
    WebView topicContentView;

    @Bind(R.id.tv_username)
    TextView userNameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_praise_count)
    TextView PraiseView;

    @Bind(R.id.bga_llyt_reply_count)
    BGABadgeLinearLayout replyCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        topicId = intent.getIntExtra(INTENT_EXTRA_PARAM_TOPIC_ID, 0);

        getPresenter().request(topicId);

        TopScrollHelper.getInstance(getApplicationContext())
                        .addTargetScrollView(topicContentView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopScrollHelper.getInstance(getApplicationContext())
                        .removeTargetScrollView(topicContentView);
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<TopicDetailPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicDetailPresenter>() {
            @Override
            public TopicDetailPresenter createPresenter() {
                TopicDetailPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });
    }

    public void initView(Topic topic) {
        Link link = topic.getLinks();
        User user = topic.getUser().getData();
        String voteCount = topic.getVoteCount() > 99 ? "99+" : String.valueOf(topic.getVoteCount());
        String replyCount = topic.getReplyCount() > 99 ? "99+" : String.valueOf(topic.getReplyCount());

        avatarView.setImageURI(Uri.parse(user.getAvatar()));
        userNameView.setText(user.getName());
        signView.setText(user.getSignature());
        PraiseView.setText(voteCount);
        topicContentView.loadUrl(link.getDetailsWebView(), getHttpHeaderAuth());
        replyCountView.showTextBadge(replyCount);

        multiStateView.setViewState(VIEW_STATE_CONTENT);
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
        multiStateView.setViewState(VIEW_STATE_ERROR);
    }
}
