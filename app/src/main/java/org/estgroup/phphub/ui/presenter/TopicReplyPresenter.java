package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.ReplyEntity;
import org.estgroup.phphub.api.entity.element.Reply;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.topic.TopicReplyActivity;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

public class TopicReplyPresenter extends BaseRxPresenter<TopicReplyActivity> {
    private static final int REQUEST_REPLY_ID = 1;

    int topicId;

    String body;

    @Inject
    TopicModel topicModel;

    @Inject
    @ForApplication
    Context context;

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

        restartableLatestCache(REQUEST_REPLY_ID,
                new Func0<Observable<Reply>>() {
                    @Override
                    public Observable<Reply> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<ReplyEntity.AReply>>() {
                            @Override
                            public Observable<ReplyEntity.AReply> call(Boolean aBoolean) {
                                return ( (TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)) )
                                        .publishReply(topicId, body)
                                        .compose(new RefreshTokenTransformer<ReplyEntity.AReply>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<ReplyEntity.AReply>())
                        .map(new Func1<ReplyEntity.AReply, Reply>() {
                            @Override
                            public Reply call(ReplyEntity.AReply aReply) {
                                return aReply.getData();
                            }
                        });
                    }
                },
                new Action2<TopicReplyActivity, Reply>() {
                    @Override
                    public void call(TopicReplyActivity topicReplyActivity, Reply reply) {
                        topicReplyActivity.onPublicSuccessful(reply);
                    }
                },
                new Action2<TopicReplyActivity, Throwable>() {
                    @Override
                    public void call(TopicReplyActivity topicReplyActivity, Throwable throwable) {
                        topicReplyActivity.onNetWorkError(throwable);
                    }
                });
    }

    public void request(int topicId, String body) {
        if (topicId > 0 && body.trim().length() > 1){
            this.topicId = topicId;
            this.body = body;

            start(REQUEST_REPLY_ID);
        }

    }

}
