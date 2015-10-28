package org.estgroup.phphub.ui.presenter;

import android.os.Bundle;

import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.UserModel;
import org.estgroup.phphub.ui.view.user.UserTopicActivity;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

import static org.estgroup.phphub.common.qualifier.AuthType.*;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

public class UserTopicsPresenter extends BaseRxPresenter<UserTopicActivity> {

    private static final int REQUEST_USER_TOPIC_ID = 1;
    private static final int REQUEST_USER_FOLLOW_ID = 2;
    private static final int REQUEST_USER_FAVORITE_ID = 3;

    int userId;

    @Inject
    UserModel userModel;

    protected int pageIndex = 1;

    private static final HashMap<String, Integer> requests = new HashMap<>();

    static {
        requests.put(USER_TOPIC_TYPE, REQUEST_USER_TOPIC_ID);
        requests.put(USER_TOPIC_FOLLOW_TYPE, REQUEST_USER_FOLLOW_ID);
        requests.put(USER_TOPIC_FAVORITE_TYPE, REQUEST_USER_FAVORITE_ID);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        Action2<UserTopicActivity, List<Topic>> onNext = new Action2<UserTopicActivity, List<Topic>>() {
            @Override
            public void call(UserTopicActivity userTopicActivity, List<Topic> topics) {
                userTopicActivity.onChangeItems(topics, pageIndex);
            }
        };

        Action2<UserTopicActivity, Throwable> onError = new Action2<UserTopicActivity, Throwable>() {
            @Override
            public void call(UserTopicActivity userTopicActivity, Throwable throwable) {
                userTopicActivity.onNetworkError(throwable, pageIndex);
            }
        };

        restartableLatestCache(REQUEST_USER_TOPIC_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return userModel.getTopics(userId, pageIndex)
                                .compose(new SchedulerTransformer<TopicEntity>())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

        restartableLatestCache(REQUEST_USER_FOLLOW_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return userModel.getAttentions(userId, pageIndex)
                                .compose(new SchedulerTransformer<TopicEntity>())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

        restartableLatestCache(REQUEST_USER_FAVORITE_ID,
                new Func0<Observable<List<Topic>>>() {
                    @Override
                    public Observable<List<Topic>> call() {
                        return userModel.getFavorites(userId, pageIndex)
                                .compose(new SchedulerTransformer<TopicEntity>())
                                .map(new Func1<TopicEntity, List<Topic>>() {
                                    @Override
                                    public List<Topic> call(TopicEntity topicEntity) {
                                        return topicEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);

    }

    public void refresh(String topicType, int userId) {
        this.userId = userId;
        pageIndex = 1;
        start(requests.get(topicType));
    }

    public void nextPage(String topicType, int userId) {
        this.userId = userId;
        pageIndex++;
        start(requests.get(topicType));
    }
}
