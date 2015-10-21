package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.github.pwittchen.prefser.library.Prefser;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.common.transformer.TokenGeneratorTransformer;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.TopicModel;
import org.estgroup.phphub.ui.view.topic.TopicDetailsActivity;

import java.util.HashMap;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

import static org.estgroup.phphub.common.qualifier.TopicDetailType.*;

public class TopicDetailPresenter extends BaseRxPresenter<TopicDetailsActivity> {
    private static final int REQUEST_TOPIC_ID = 1;

    private static final int REQUEST_FAVOURITE_TOPIC_ID = 2;

    private static final int REQUEST_FOLLOWING_TOPIC_ID = 3;

    private static final int REQUEST_FAVOURITE_TOPIC_DELETE_ID = 4;

    private static final int REQUEST_FOLLOWING_TOPIC_DELETE_ID = 5;

    private static final int REQUEST_VOTE_UP_ID = 6;

    private static final int REQUEST_VOTE_DOWN_ID = 7;

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

    private static final HashMap<String, Integer> requests = new HashMap<>();

    static {
        requests.put(TOPIC_DETAIL_TYPE_FAVORITE, REQUEST_FAVOURITE_TOPIC_ID);
        requests.put(TOPIC_DETAIL_TYPE_FOLLOW, REQUEST_FOLLOWING_TOPIC_ID);
        requests.put(TOPIC_DETAIL_TYPE_FAVORITE_DEL, REQUEST_FAVOURITE_TOPIC_DELETE_ID);
        requests.put(TOPIC_DETAIL_TYPE_FOLLOW_DEL, REQUEST_FOLLOWING_TOPIC_DELETE_ID);
        requests.put(TOPIC_DETAIL_TYPE_VOTE_UP, REQUEST_VOTE_UP_ID);
        requests.put(TOPIC_DETAIL_TYPE_VOTE_DOWN, REQUEST_VOTE_DOWN_ID);
    }

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
                        return Observable.just(Utils.logined(context, accountManager)).flatMap(new Func1<Boolean, Observable<TopicEntity.ATopic>>() {
                            @Override
                            public Observable<TopicEntity.ATopic> call(Boolean logined) {
                                if (logined) {
                                    return topicModel.once()
                                            .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType))
                                            .getTopicDetailById(topicId)
                                            .compose(new RefreshTokenTransformer<TopicEntity.ATopic>(
                                                    tokenModel,
                                                    authAccountManager,
                                                    accountManager,
                                                    Utils.getActiveAccount(context, authAccountManager),
                                                    accountType,
                                                    tokenType

                                            ));

                                }
                                return topicModel.getTopicDetailById(topicId)
                                        .compose(new TokenGeneratorTransformer<TopicEntity.ATopic>(tokenModel, prefser));
                            }
                        })
                        .compose(new SchedulerTransformer<TopicEntity.ATopic>())
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


        restartableLatestCache(REQUEST_FAVOURITE_TOPIC_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .isFavorite(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_FAVORITE, true);
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });

        restartableLatestCache(REQUEST_FAVOURITE_TOPIC_DELETE_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .delFavorite(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_FAVORITE_DEL, true);
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });

        restartableLatestCache(REQUEST_FOLLOWING_TOPIC_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .isFollow(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_FOLLOW, true);
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });

        restartableLatestCache(REQUEST_FOLLOWING_TOPIC_DELETE_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .delFollow(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_FOLLOW_DEL, true);
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });

        restartableLatestCache(REQUEST_VOTE_UP_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .voteUp(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_VOTE_UP, jsonObject.get("vote-up").getAsBoolean());
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });

        restartableLatestCache(REQUEST_VOTE_DOWN_ID,
                new Func0<Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(Boolean logined) {
                                return ((TopicModel) topicModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .voteDown(topicId)
                                        .compose(new RefreshTokenTransformer<JsonObject>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                        .compose(new SchedulerTransformer<JsonObject>())
                        .map(new Func1<JsonObject, JsonObject>() {
                            @Override
                            public JsonObject call(JsonObject jsonObject) {
                                return jsonObject;
                            }
                        });
                    }
                },
                new Action2<TopicDetailsActivity, JsonObject>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, JsonObject jsonObject) {
                        topicDetailsActivity.setOptionState(TOPIC_DETAIL_TYPE_VOTE_DOWN, jsonObject.get("vote-down").getAsBoolean());
                    }
                },
                new Action2<TopicDetailsActivity, Throwable>() {
                    @Override
                    public void call(TopicDetailsActivity topicDetailsActivity, Throwable throwable) {
                        Logger.e(throwable.getMessage());
                    }
                });
    }

    public void request(int topicId) {
        if (topicId > 0) {
            this.topicId = topicId;
            start(REQUEST_TOPIC_ID);
        }
    }

    public void eventRequest(int topicId, String eventType) {
        if (topicId > 0) {
            this.topicId = topicId;
            start(requests.get(eventType));
        }
    }
}
