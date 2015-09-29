package org.phphub.app.ui.presenter;

import android.os.Bundle;

import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.view.topic.TopicFragment;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import static org.phphub.app.common.qualified.TopicType.*;

public class TopicPresenter extends BaseRxPresenter<TopicFragment> {
    public static final int REQUEST_RECENT_ID = 1;

    public static final int REQUEST_VOTE_ID = 2;

    public static final int REQUEST_NOBODY_ID = 3;

    public static final int REQUEST_JOBS_ID = 4;

    @Inject
    TopicModel topicModel;

    protected int pageIndex = 1;

    private static final HashMap<String, Integer> requests = new HashMap<>();

    static {
        requests.put(TOPIC_TYPE_RECENT, REQUEST_RECENT_ID);
        requests.put(TOPIC_TYPE_VOTE, REQUEST_VOTE_ID);
        requests.put(TOPIC_TYPE_NOBODY, REQUEST_NOBODY_ID);
        requests.put(TOPIC_TYPE_JOBS, REQUEST_JOBS_ID);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        Action2<TopicFragment, List<Topic>> onNext = new Action2<TopicFragment, List<Topic>>() {
            @Override
            public void call(TopicFragment topicFragment, List<Topic> topics) {
                topicFragment.onChangeItems(topics, pageIndex);
            }
        };

        Action2<TopicFragment, Throwable> onError = new Action2<TopicFragment, Throwable>() {
            @Override
            public void call(TopicFragment topicFragment, Throwable throwable) {
                topicFragment.onNetworkError(throwable, pageIndex);
            }
        };

        restartableLatestCache(REQUEST_RECENT_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByRecent(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

        restartableLatestCache(REQUEST_VOTE_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByVote(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

        restartableLatestCache(REQUEST_NOBODY_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByNobody(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

        restartableLatestCache(REQUEST_JOBS_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return topicModel.getTopicsByJobs(pageIndex)
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);
    }

    public void refresh(String topicType) {
        pageIndex = 1;
        start(requests.get(topicType));
    }

    public void nextPage(String topicType) {
        pageIndex++;
        start(requests.get(topicType));
    }
}
