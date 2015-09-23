package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.orhanobut.logger.Logger;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.adapter.TopicItemView;
import org.phphub.app.common.base.BaseFragment;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.presenter.RecommendedPresenter;

import java.util.List;

import butterknife.Bind;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(RecommendedPresenter.class)
public class RecommendedFragment extends BaseFragment<RecommendedPresenter> implements
        ViewEventListener<Topic>{
    TopicModel topicModel;

    RecyclerMultiAdapter adapter;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.recycler_view)
    RecyclerView topicListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommended_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topicListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = SmartAdapter.empty()
                .map(Topic.class, TopicItemView.class)
                .listener(this)
                .into(topicListView);

        getPresenter().refresh();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.recommended);
    }

    public void onChangeItems(List<Topic> topics, int pageIndex) {
        if (pageIndex == 1) {
            adapter.setItems(topics);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            adapter.addItems(topics);
        }
    }

    public void onNetworkError(Throwable throwable, int pageIndex) {
        Logger.e(throwable.getMessage());
        if (pageIndex == 1) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void onViewEvent(int actionId, Topic topic, int position, View view) {

    }
}