package org.phphub.app.ui.presenter;

import android.os.Bundle;

import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.App;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.view.RecommendedFragment;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

public class RecommendedPresenter extends BaseRxPresenter<RecommendedFragment> {
    public static final int REQUEST_ID = 1;

    protected TopicModel topicModel;

    protected int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        topicModel = new TopicModel(App.getInstance());

        restartableLatestCache(REQUEST_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByExcellent(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                },
                new Action2<RecommendedFragment, List<Topic>>() {
                    @Override
                    public void call(RecommendedFragment recommendedFragment, List<Topic> topics) {
                        recommendedFragment.onChangeItems(topics, pageIndex);
                    }
                },
                new Action2<RecommendedFragment, Throwable>() {
                    @Override
                    public void call(RecommendedFragment recommendedFragment, Throwable throwable) {
                        recommendedFragment.onNetworkError(throwable, pageIndex);
                    }
                });
    }

    public void refresh() {
        pageIndex = 1;
        start(REQUEST_ID);
    }

    public void nextPage() {
        pageIndex++;
        start(REQUEST_ID);
    }
}
