package org.phphub.app.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.R;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.common.internal.di.qualifier.ForApplication;
import org.phphub.app.common.transformer.RefreshTokenTransformer;
import org.phphub.app.common.transformer.TokenGeneratorTransformer;
import org.phphub.app.model.TokenModel;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.view.topic.TopicDetailsActivity;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TopicDetailPresenter extends BaseRxPresenter<TopicDetailsActivity> {
    private static final int REQUEST_TOPIC_ID = 1;

    int topicId;

    @Inject
    @ForApplication
    Context context;

    @Inject
    TopicModel topicModel;

    @Inject
    TokenModel tokenModel;

    @Inject
    Prefser prefser;

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

        restartableLatestCache(REQUEST_TOPIC_ID,
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
                            public Observable<TopicEntity.ATopic> call(Boolean logined) {
                                if (logined) {
                                    return ( (TopicModel) topicModel.local(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)) )
                                                            .getTopicDetailById(topicId)
                                                            .compose(new RefreshTokenTransformer<TopicEntity.ATopic>(
                                                                    tokenModel,
                                                                    authAccountManager,
                                                                    accountManager,
                                                                    (accounts.length > 0 ? accounts[0] : null),
                                                                    accountType,
                                                                    tokenType

                                                            ));

                                }
                                return topicModel.getTopicDetailById(topicId)
                                        .compose(new TokenGeneratorTransformer<TopicEntity.ATopic>(tokenModel, prefser));
                            }
                        })
                        .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<TopicEntity.ATopic, Topic>() {
                                    @Override
                                    public Topic call(TopicEntity.ATopic topic) {
                                        return topic.getData();
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, Topic>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Topic topic) {
                        topicDetailsActivity.initView(topic);
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        topicDetailsActivity.onNetworkError(throwable);
                    }
                });
    }

    public void request(int topicId) {
        if (topicId > 0) {
            this.topicId = topicId;
            start(REQUEST_TOPIC_ID);
        }
    }
}
