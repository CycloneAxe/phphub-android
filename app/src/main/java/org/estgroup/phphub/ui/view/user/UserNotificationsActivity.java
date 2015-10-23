package org.estgroup.phphub.ui.view.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.adapter.NotificationItemView;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.UserNotificationsPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT;
import static com.kennyc.view.MultiStateView.VIEW_STATE_ERROR;
import static com.kennyc.view.MultiStateView.VIEW_STATE_LOADING;
import static org.estgroup.phphub.common.qualifier.ClickType.CLICK_TYPE_TOPIC_CLICKED;

@RequiresPresenter(UserNotificationsPresenter.class)
public class UserNotificationsActivity extends BaseActivity<UserNotificationsPresenter> implements
        ViewEventListener<Notification>{

    RecyclerMultiAdapter adapter;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshView;

    @Bind(R.id.recycler_view)
    RecyclerView notificationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationListView.setLayoutManager(new LinearLayoutManager(this));

        adapter = SmartAdapter.empty()
                .map(Notification.class, NotificationItemView.class)
                .listener(this)
                .into(notificationListView);

        refreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getPresenter().refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getPresenter().nextPage();
            }
        });

        multiStateView.setViewState(VIEW_STATE_LOADING);
        refreshView.autoRefresh();

    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, UserNotificationsActivity.class);
        return callingIntent;
    }

    public void onChangeItems(List<Notification> notifications, int pageIndex) {
        Logger.d(String.valueOf(pageIndex));
        if (pageIndex == 1) {
            adapter.setItems(notifications);
            multiStateView.setViewState(VIEW_STATE_CONTENT);
            refreshView.finishRefresh();
        } else {
            adapter.addItems(notifications);
            refreshView.finishRefreshLoadMore();
        }
    }

    public void onNetworkError(Throwable throwable, int pageIndex) {
        Logger.e(throwable.getMessage());

        if (pageIndex == 1) {
            multiStateView.setViewState(VIEW_STATE_ERROR);
        }
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<UserNotificationsPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<UserNotificationsPresenter>() {
            @Override
            public UserNotificationsPresenter createPresenter() {
                UserNotificationsPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.my_messages);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.message_list;
    }

    @Override
    public void onViewEvent(int actionId, Notification notification, int postion, View view) {
        Topic topic = notification.getTopic().getData();

        switch (actionId) {
            case CLICK_TYPE_TOPIC_CLICKED:
                if (topic != null) {
                    navigator.navigateToTopicDetails(this, topic.getId());
                }

                break;
        }
    }

    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(VIEW_STATE_LOADING);
        getPresenter().refresh();
    }
}
