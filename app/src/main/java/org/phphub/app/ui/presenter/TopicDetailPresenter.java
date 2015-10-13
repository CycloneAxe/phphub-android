package org.phphub.app.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.R;
import org.phphub.app.api.RequestInterceptorImpl;
import org.phphub.app.api.TopicApi;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.element.Topic;
import org.phphub.app.common.base.BaseRxPresenter;
import org.phphub.app.common.internal.di.qualifier.ForApplication;
import org.phphub.app.model.TokenModel;
import org.phphub.app.model.TopicModel;
import org.phphub.app.ui.view.topic.TopicDetailsActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.phphub.app.common.Constant.*;
import static org.phphub.app.common.qualifier.AuthType.*;

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

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableLatestCache(REQUEST_TOPIC_ID,
                new Func0<Observable<Topic>>() {
                    @Override
                    public Observable<Topic> call() {
                        Observable<RequestInterceptorImpl> observable = Observable.create(new Observable.OnSubscribe<RequestInterceptorImpl>() {
                            @Override
                            public void call(Subscriber<? super RequestInterceptorImpl> subscriber) {
                                String accountType = context.getString(R.string.auth_account_type),
                                        tokenType = context.getString(R.string.auth_token_type);
                                Account[] accounts = accountManager.getAccountsByType(accountType);

                                RequestInterceptorImpl requestInterceptor = new RequestInterceptorImpl();
                                if (accounts.length > 0) {
                                    requestInterceptor.setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType));
                                    requestInterceptor.setAuthType(AUTH_TYPE_USER);
                                } else {
                                    requestInterceptor.setToken(prefser.get(GUEST_TOKEN_KEY, String.class, ""));
                                    requestInterceptor.setAuthType(AUTH_TYPE_GUEST);
                                }

                                subscriber.onNext(requestInterceptor);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<RequestInterceptorImpl, Observable<TopicEntity.ATopic>>() {
                            @Override
                            public Observable<TopicEntity.ATopic> call(RequestInterceptorImpl requestInterceptor) {
                                Map<String, String> options = new HashMap<>();
                                options.put("include", "user,node");
                                options.put("columns", "user(signature)");
                                TopicApi api = topicModel.getService(requestInterceptor);
                                if (AUTH_TYPE_USER.equals(requestInterceptor.getAuthType())) {
                                    api.getTopic(topicId, options);
                                }
                                return api.getTopic(topicId, options)
                                        .compose(TopicDetailPresenter.this.<TopicEntity.ATopic>applyRetryByGuest(tokenModel, prefser));
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
