package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.topic.TopicPublishActivity;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TopicPublishPresenter extends BaseRxPresenter<TopicPublishActivity> {
    private static final int REQUEST_PUBLISH_TOPIC_ID = 1;

    Topic topicInfo;

    @Inject
    @ForApplication
    Context context;

    @Inject
    TopicModel topicModel;

    @Inject
    TokenModel tokenModel;

    @Inject
    AccountManager accountManager;

    @Inject
    AuthAccountManager authAccountManager;

    String accountType, tokenType;

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

                        return observable.flatMap(
                                new Func1<Boolean, Observable<TopicEntity.ATopic>>() {
                                    @Override
                                    public Observable<TopicEntity.ATopic> call(Boolean aBoolean) {
                                        return ((TopicModel) topicModel.local(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                                .publishTopic(topicInfo)
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
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
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
                }
        );
    }

    public void request(Topic topicInfo) {
        this.topicInfo = topicInfo;
        start(REQUEST_PUBLISH_TOPIC_ID);
    }
}
