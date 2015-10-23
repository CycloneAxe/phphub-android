package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.UserModel;
import org.estgroup.phphub.ui.view.user.UserNotificationsActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_USER;

public class UserNotificationsPresenter extends BaseRxPresenter<UserNotificationsActivity> {

    @Inject
    @Named(AUTH_TYPE_USER)
    UserModel userModel;

    @Inject
    TokenModel tokenModel;

    @Inject
    @ForApplication
    Context context;

    @Inject
    AuthAccountManager authAccountManager;

    @Inject
    AccountManager accountManager;

    String tokenType, accountType;

    Account[] accounts;

    private static final int REQUEST_NOTIFICATION_ID = 1;

    protected int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        accountType = context.getString(R.string.auth_account_type);
        tokenType = context.getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);

        Action2<UserNotificationsActivity, List<Notification>> onNext = new Action2<UserNotificationsActivity, List<Notification>>() {
            @Override
            public void call(UserNotificationsActivity userNotificationsActivity, List<Notification> notificationList) {
                userNotificationsActivity.onChangeItems(notificationList, pageIndex);
            }
        };

        Action2<UserNotificationsActivity, Throwable> onError = new Action2<UserNotificationsActivity, Throwable>() {
            @Override
            public void call(UserNotificationsActivity userNotificationsActivity, Throwable throwable) {
                userNotificationsActivity.onNetworkError(throwable, pageIndex);
            }
        };

        restartableLatestCache(REQUEST_NOTIFICATION_ID,
                new Func0<Observable<List<Notification>>>() {
                    @Override
                    public Observable<List<Notification>> call() {

                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<NotificationEntity>>() {
                            @Override
                            public Observable<NotificationEntity> call(Boolean aBoolean) {
                                return ((UserModel) userModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType)))
                                        .getMyNotifications(pageIndex)
                                        .compose(new RefreshTokenTransformer<NotificationEntity>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                                .compose(new SchedulerTransformer<NotificationEntity>())
                                .map(new Func1<NotificationEntity, List<Notification>>() {
                                    @Override
                                    public List<Notification> call(NotificationEntity notificationEntity) {
                                        return notificationEntity.getData();
                                    }
                                });
                    }
                }, onNext, onError);
    }

    public void refresh() {
        pageIndex = 1;
        start(REQUEST_NOTIFICATION_ID);
    }

    public void nextPage() {
        pageIndex++;
        start(REQUEST_NOTIFICATION_ID);
    }
}
