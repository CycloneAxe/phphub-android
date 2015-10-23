package org.estgroup.phphub.common.transformer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.estgroup.phphub.api.entity.element.Token;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.TokenModel;

import eu.unicate.retroauth.AuthAccountManager;
import eu.unicate.retroauth.exceptions.AuthenticationCanceledException;
import rx.Notification;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_REFRESH;

public class RefreshTokenTransformer<T> extends RetryTransformer implements Observable.Transformer<T, T> {
    private TokenModel tokenModel;

    private AuthAccountManager authAccountManager;

    private AccountManager accountManager;

    private Account account;

    private String accountType, tokenType;

    public RefreshTokenTransformer(@NonNull TokenModel tokenModel,
                                   AuthAccountManager authAccountManager,
                                   AccountManager accountManager,
                                   @Nullable Account account,
                                   String accountType,
                                   String tokenType) {
        this.tokenModel = tokenModel;
        this.authAccountManager = authAccountManager;
        this.accountManager = accountManager;
        this.account = account;
        this.accountType = accountType;
        this.tokenType = tokenType;
    }

    private boolean hasAuthentication(Throwable throwable) {
        return (throwable instanceof AuthenticationCanceledException || Utils.hasUnauthorized(throwable));
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable.retry(new Func2<Integer, Throwable, Boolean>() {
            @Override
            public Boolean call(Integer retryCount, Throwable throwable) {
                final boolean[] needRetry = {false};

                if (retryCount <= RETRY_COUNT && hasAuthentication(throwable) && account != null) {
                    tokenModel.refreshToken(authAccountManager.getUserData(accountType, AUTH_TYPE_REFRESH))
                            .materialize()
                            .filter(new Func1<Notification<Token>, Boolean>() {
                                @Override
                                public Boolean call(Notification<Token> notification) {
                                    return !notification.isOnCompleted();
                                }
                            })
                            .filter(new Func1<Notification<Token>, Boolean>() {
                                @Override
                                public Boolean call(Notification<Token> notification) {
                                    Token token = notification.getValue();
                                    return (token != null && !TextUtils.isEmpty(token.getToken()));
                                }
                            })
                            .doOnNext(new Action1<Notification<Token>>() {
                                @Override
                                public void call(Notification<Token> notification) {
                                    Token token = notification.getValue();
                                    accountManager.setAuthToken(account, tokenType, token.getToken());
                                    accountManager.setUserData(account, AUTH_TYPE_REFRESH, token.getRefreshToken());
                                }
                            })
                            .toBlocking()
                            .forEach(new Action1<Notification<Token>>() {
                                @Override
                                public void call(Notification<Token> notification) {
                                    needRetry[0] = true;
                                }
                            });
                }

                return needRetry[0];
            }
        });
    }
}
