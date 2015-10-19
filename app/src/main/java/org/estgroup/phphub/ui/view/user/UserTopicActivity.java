package org.estgroup.phphub.ui.view.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.adapter.TopicItemView;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.UserTopicsPresenter;

import java.util.List;

import butterknife.Bind;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT;
import static com.kennyc.view.MultiStateView.VIEW_STATE_ERROR;
import static com.kennyc.view.MultiStateView.VIEW_STATE_LOADING;
import static org.estgroup.phphub.common.Constant.USER_ID_KEY;
import static org.estgroup.phphub.common.qualifier.ClickType.*;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

@RequiresPresenter(UserTopicsPresenter.class)
public class UserTopicActivity extends BaseActivity<UserTopicsPresenter> implements
        ViewEventListener<Topic>{

    private static final String USER_ID = "user_id";
    private static final String USER_TYPE = "user_type";

    private boolean isPrepared;

    RecyclerMultiAdapter adapter;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshView;

    @Bind(R.id.recycler_view)
    RecyclerView topicListView;

    int userId;

    String userTopicType;

    AccountManager accountManager;

    String accountType, tokenType;

    Account[] accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accountManager = AccountManager.get(this);

        accounts = accountManager.getAccountsByType(accountType);

        userId = getIntent().getIntExtra(USER_ID, 0);
        userTopicType = getIntent().getStringExtra(USER_TYPE);

        if (TextUtils.isEmpty(userTopicType)) {
            userTopicType = USER_TOPIC_TYPE;
        }

        if (userId > 0) {
            topicListView.setLayoutManager(new LinearLayoutManager(this));

            adapter = SmartAdapter.empty()
                    .map(Topic.class, TopicItemView.class)
                    .listener(this)
                    .into(topicListView);

            refreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
                @Override
                public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                    getPresenter().refresh(userTopicType, userId);
                }

                @Override
                public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                    super.onRefreshLoadMore(materialRefreshLayout);
                    getPresenter().nextPage(userTopicType, userId);
                }
            });

            multiStateView.setViewState(VIEW_STATE_LOADING);
            refreshView.autoRefresh();
        }

    }


    public static Intent getCallingIntent(Context context, int userId, String topicType) {
        Intent callingIntent = new Intent(context, UserTopicActivity.class);
        callingIntent.putExtra(USER_ID, userId);
        callingIntent.putExtra(USER_TYPE, topicType);
        return callingIntent;
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<UserTopicsPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<UserTopicsPresenter>() {
            @Override
            public UserTopicsPresenter createPresenter() {
                UserTopicsPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.topic_normal_list;
    }

    @Override
    protected CharSequence getTitleName() {
        String title = getString(R.string.my_topics);

        if (accounts.length > 0 && Integer.parseInt(accountManager.getUserData(accounts[0], USER_ID_KEY)) == userId) {
            switch (userTopicType) {
                case USER_TOPIC_TYPE:
                    title = getString(R.string.my_topics);
                    break;
                case USER_TOPIC_FOLLOW_TYPE:
                    title = getString(R.string.my_following);
                    break;
                case USER_TOPIC_FAVORITE_TYPE:
                    title = getString(R.string.my_favorite);
                    break;
            }
        } else {
            switch (userTopicType) {
                case USER_TOPIC_TYPE:
                    title = getString(R.string.ta_topics);
                    break;
                case USER_TOPIC_FOLLOW_TYPE:
                    title = getString(R.string.ta_following);
                    break;
                case USER_TOPIC_FAVORITE_TYPE:
                    title = getString(R.string.ta_favorite);
                    break;
            }
        }

        return title;
    }

    @Override
    public void onViewEvent(int actionId, Topic topic, int option, View view) {
        switch (actionId) {
            case CLICK_TYPE_TOPIC_CLICKED:
                navigator.navigateToTopicDetails(this, topic.getId());
                break;
        }
    }

    public void onChangeItems(List<Topic> topics, int pageIndex) {
        if (pageIndex == 1) {
            adapter.setItems(topics);
            multiStateView.setViewState(VIEW_STATE_CONTENT);
            refreshView.finishRefresh();
        } else {
            adapter.addItems(topics);
            refreshView.finishRefreshLoadMore();
        }
    }

    public void onNetworkError(Throwable throwable, int pageIndex) {
        Logger.e(throwable.getMessage());

        if (pageIndex == 1) {
            multiStateView.setViewState(VIEW_STATE_ERROR);
        }
    }
}
