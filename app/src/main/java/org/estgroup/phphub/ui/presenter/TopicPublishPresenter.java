package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NodeEntity;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Node;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.topic.TopicPublishActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import static org.estgroup.phphub.common.qualifier.AuthType.*;

public class TopicPublishPresenter extends BaseRxPresenter<TopicPublishActivity> {
    private static final int REQUEST_PUBLISH_TOPIC_ID = 1;

    private static final int REQUEST_GET_NODE_ID = 2;

    Topic topic;

    @Inject
    AuthAccountManager authAccountManager;

    @Inject
    AccountManager accountManager;

    @Inject
    @ForApplication
    Context context;

    @Inject
    TopicModel topicModel;

    @Inject
    TopicModel authTopicModel;

    @Inject
    TokenModel tokenModel;

    String tokenType, accountType;

    Account[] accounts;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        accountType = context.getString(R.string.auth_account_type);
        tokenType = context.getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);

        restartableLatestCache(REQUEST_PUBLISH_TOPIC_ID,
                new Func0<Observable<Topic>>() {
                    @Override
                    public Observable<Topic> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<TopicEntity.ATopic>>() {
                            @Override
                            public Observable<TopicEntity.ATopic> call(Boolean aBoolean) {
                                return authTopicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType))
                                        .publishTopic(topic)
                                        .compose(new RefreshTokenTransformer<TopicEntity.ATopic>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                                .compose(new SchedulerTransformer<TopicEntity.ATopic>())
                                .map(new Func1<TopicEntity.ATopic, Topic>() {
                                    @Override
                                    public Topic call(TopicEntity.ATopic aTopic) {
                                        return aTopic.getData();
                                    }
                                });
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
