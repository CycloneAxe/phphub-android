package org.estgroup.phphub.ui.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.UserEntity;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseRxPresenter;
import org.estgroup.phphub.common.internal.di.qualifier.ForApplication;
import org.estgroup.phphub.common.transformer.RefreshTokenTransformer;
import org.estgroup.phphub.common.transformer.SchedulerTransformer;
import org.estgroup.phphub.model.TokenModel;
import org.estgroup.phphub.model.UserModel;
import org.estgroup.phphub.ui.view.user.EditUserProfileActivity;

import javax.inject.Inject;
import javax.inject.Named;

import eu.unicate.retroauth.AuthAccountManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_USER;

public class EditUserProfilePresenter extends BaseRxPresenter<EditUserProfileActivity> {
    private static final int REQUEST_EDIT_ID = 1;

    User userInfo;

    @Inject
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

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        accountType = context.getString(R.string.auth_account_type);
        tokenType = context.getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);

        restartableLatestCache(REQUEST_EDIT_ID,
                new Func0<Observable<User>>() {
                    @Override
                    public Observable<User> call() {
                        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                subscriber.onNext(accounts.length > 0);
                                subscriber.onCompleted();
                            }
                        });

                        return observable.flatMap(new Func1<Boolean, Observable<UserEntity.AUser>>() {
                            @Override
                            public Observable<UserEntity.AUser> call(Boolean aBoolean) {
                                return userModel.once()
                                        .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType))
                                        .saveUserProfile(userInfo)
                                        .compose(new RefreshTokenTransformer<UserEntity.AUser>(
                                                tokenModel,
                                                authAccountManager,
                                                accountManager,
                                                (accounts.length > 0 ? accounts[0] : null),
                                                accountType,
                                                tokenType
                                        ));
                            }
                        })
                                .compose(new SchedulerTransformer<UserEntity.AUser>())
                                .map(new Func1<UserEntity.AUser, User>() {
                                    @Override
                                    public User call(UserEntity.AUser aUser) {
                                        return aUser.getData();
                                    }
                                });
                    }
                },
                new Action2<EditUserProfileActivity, User>() {
                    @Override
                    public void call(EditUserProfileActivity editUserProfileActivity, User user) {
                        editUserProfileActivity.onSaveSuccessful(user);
                    }
                },
                new Action2<EditUserProfileActivity, Throwable>() {
                    @Override
                    public void call(EditUserProfileActivity editUserProfileActivity, Throwable throwable) {
                        editUserProfileActivity.onNetWorkError(throwable);
                    }
                });

    }

    public void request(User user) {
        if (user.getId() > 0) {
            this.userInfo = user;
            start(REQUEST_EDIT_ID);
        }

    }

}
