package org.estgroup.phphub.ui.presenter;

import android.content.Context;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.api.entity.NodeEntity;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Node;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.topic.TopicPublishActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import static org.estgroup.phphub.common.qualifier.AuthType.*;

public class TopicPublishPresenter extends BaseRxPresenter<TopicPublishActivity> {
    private static final int REQUEST_PUBLISH_TOPIC_ID = 1;

    private static final int REQUEST_GET_NODE_ID = 2;

    Topic topic;

    @Inject
    @ForApplication
    Context context;

    @Inject
    TopicModel topicModel;

    @Inject
    @Named(AUTH_TYPE_USER)
    TopicModel authTopicModel;

    @Inject
    TokenModel tokenModel;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_PUBLISH_TOPIC_ID,
                new Func0<Observable<Topic>>() {
                    @Override
                    public Observable<Topic> call() {
                        return authTopicModel.publishTopic(topic)
                                .map(new Func1<TopicEntity.ATopic, Topic>() {
                                    @Override
                                    public Topic call(TopicEntity.ATopic topic) {
                                        return topic.getData();
                                    }
                                })
                                .compose(new SchedulerTransformer<Topic>());
                    }
                },
                new Action2<TopicPublishActivity, Topic>() {
                    @Override
                    public void call(TopicPublishActivity topicPublishActivity, Topic topic) {
                        topicPublishActivity.onPublishSuccessful(topic);
                    }
                },
                new Action2<TopicPublishActivity, Throwable>() {
                    @Override
                    public void call(TopicPublishActivity topicPublishActivity, Throwable throwable) {
                        topicPublishActivity.onNetWorkError(throwable);
                    }
                });

        restartableLatestCache(REQUEST_GET_NODE_ID,
                new Func0<Observable<List<Node>>>() {
                    @Override
                    public Observable<List<Node>> call() {
                        return topicModel.getAllNodes()
                                .map(new Func1<NodeEntity.Nodes, List<Node>>() {
                                    @Override
                                    public List<Node> call(NodeEntity.Nodes nodes) {
                                        return nodes.getData();
                                    }
                                })
                                .compose(new SchedulerTransformer<List<Node>>());
                    }
                },
                new Action2<TopicPublishActivity, List<Node>>() {
                    @Override
                    public void call(TopicPublishActivity topicPublishActivity, List<Node> nodes) {
                        topicPublishActivity.setNodes(nodes);
                    }
                },
                new Action2<TopicPublishActivity, Throwable>() {
                    @Override
                    public void call(TopicPublishActivity topicPublishActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });
    }

    public void publish(Topic topic) {
        this.topic = topic;
        start(REQUEST_PUBLISH_TOPIC_ID);
    }

    public void request() {
        start(REQUEST_GET_NODE_ID);
    }
}
