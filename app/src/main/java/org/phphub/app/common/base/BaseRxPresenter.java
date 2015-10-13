package org.phphub.app.common.base;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.api.RequestInterceptorImpl;
import org.phphub.app.api.entity.element.Token;
import org.phphub.app.model.TokenModel;

import eu.unicate.retroauth.AuthAccountManager;
import icepick.Icepick;
import nucleus.presenter.RxPresenter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import static org.phphub.app.common.Constant.*;
import static org.phphub.app.common.qualifier.AuthType.*;

public class BaseRxPresenter<View> extends RxPresenter<View> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }

    protected boolean hasUnauthorized(Throwable throwable) {
        if (!(throwable instanceof RetrofitError)) {
            return false;
        }

        Response r = ((RetrofitError) throwable).getResponse();
        return r != null && r.getStatus() == 401;
    }

    protected <T> Observable.Transformer<T, T> applyRetryByUser(@NonNull final TokenModel tokenModel,
                                                                final AuthAccountManager authAccountManager,
                                                                final AccountManager accountManager,
                                                                @Nullable final Account account,
                                                                final String accountType,
                                                                final String tokenType) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer retryCount, Throwable throwable) {
                        final boolean[] needRetry = {false};

                        if (retryCount == 1 && hasUnauthorized(throwable) && account != null) {
                            tokenModel.refreshToken(authAccountManager.getUserData(accountType, AUTH_TYPE_REFRESH))
                                    .filter(new Func1<Token, Boolean>() {
                                        @Override
                                        public Boolean call(Token token) {
                                            return (token != null && !TextUtils.isEmpty(token.getToken()));
                                        }
                                    })
                                    .doOnNext(new Action1<Token>() {
                                        @Override
                                        public void call(Token token) {
                                            accountManager.setAuthToken(account, tokenType, token.getToken());
                                            accountManager.setUserData(account, AUTH_TYPE_REFRESH, token.getRefreshToken());
                                        }
                                    })
                                    .toBlocking()
                                    .forEach(new Action1<Token>() {
                                        @Override
                                        public void call(Token token) {
                                            needRetry[0] = true;
                                        }
                                    });
                        }

                        return needRetry[0];
                    }
                });
            }
        };
    }

    protected <T> Observable.Transformer<T, T> applyRetryByGuest(@NonNull final TokenModel tokenModel, @NonNull final Prefser prefser) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer retryCount, Throwable throwable) {
                        final boolean[] needRetry = {false};

                        if (retryCount == 1 && hasUnauthorized(throwable)) {
                            tokenModel.tokenGenerator()
                                    .filter(new Func1<Token, Boolean>() {
                                        @Override
                                        public Boolean call(Token token) {
                                            return (token != null && !TextUtils.isEmpty(token.getToken()));
                                        }
                                    })
                                    .doOnNext(new Action1<Token>() {
                                        @Override
                                        public void call(Token token) {
                                            prefser.put(GUEST_TOKEN_KEY, token.getToken());
                                        }
                                    })
                                    .toBlocking()
                                    .forEach(new Action1<Token>() {
                                        @Override
                                        public void call(Token token) {
                                            RequestInterceptorImpl requestInterceptor = tokenModel.getRequestInterceptor();
                                            requestInterceptor.setToken(token.getToken());
                                            requestInterceptor.setIgnore(false);
                                            needRetry[0] = true;
                                        }
                                    });
                        }

                        return needRetry[0];
                    }
                });
            }
        };
    }
}