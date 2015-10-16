package org.estgroup.phphub.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.adapter.TopicItemView;
import org.estgroup.phphub.common.base.LazyFragment;
import org.estgroup.phphub.ui.presenter.RecommendedPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import static org.estgroup.phphub.common.qualifier.ClickType.*;
import static com.kennyc.view.MultiStateView.*;

@RequiresPresenter(RecommendedPresenter.class)
public class RecommendedFragment extends LazyFragment<RecommendedPresenter> implements
        ViewEventListener<Topic>{
    private boolean isPrepared;

    RecyclerMultiAdapter adapter;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.refresh)
    MaterialRefreshLayout refreshView;

    @Bind(R.id.recycler_view)
    RecyclerView topicListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<RecommendedPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<RecommendedPresenter>() {
            @Override
            public RecommendedPresenter createPresenter() {
                RecommendedPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_normal_list, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_publish, menu);

        if (!isLogin()) {
            menu.findItem(R.id.action_publish).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isLogin()) {
            toolbarView.inflateMenu(R.menu.menu_publish);
            toolbarView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    navigator.navigateToPublishTopic(getContext());
                    return true;
                }
            });
        }

        topicListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = SmartAdapter.empty()
                .map(Topic.class, TopicItemView.class)
                .listener(this)
                .into(topicListView);

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

        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }

        if (!canLoadData(multiStateView, adapter)) {
            return;
        }

        multiStateView.setViewState(VIEW_STATE_LOADING);
        refreshView.autoRefresh();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.recommended);
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

    @Override
    public void onViewEvent(int actionId, Topic topic, int position, View view) {
        User userInfo = topic.getUser().getData();

        switch (actionId) {
            case CLICK_TYPE_TOPIC_CLICKED:
                navigator.navigateToTopicDetails(getActivity(), topic.getId());
                break;

            case CLICK_TYPE_USER_CLICKED:
                navigator.navigateToUserSpace(getContext(), userInfo.getId());
                break;
        }
    }

    @OnClick(R.id.retry)
    public void retry() {
        multiStateView.setViewState(VIEW_STATE_LOADING);
        getPresenter().refresh();
    }
}