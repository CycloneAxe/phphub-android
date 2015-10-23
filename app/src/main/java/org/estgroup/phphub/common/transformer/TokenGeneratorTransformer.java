package org.estgroup.phphub.common.transformer;

import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;

import org.estgroup.phphub.api.entity.element.Token;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.TokenModel;

import rx.Notification;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static org.estgroup.phphub.common.Constant.*;

public class TokenGeneratorTransformer<T> extends RetryTransformer implements Observable.Transformer<T, T> {
    private TokenModel tokenModel;

    private Prefser prefser;

    public TokenGeneratorTransformer(TokenModel tokenModel, Prefser prefser) {
        this.tokenModel = tokenModel;
        this.prefser = prefser;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable.retry(new Func2<Integer, Throwable, Boolean>() {
            @Override
            public Boolean call(Integer retryCount, Throwable throwable) {
                final boolean[] needRetry = {false};

                if (retryCount <= RETRY_COUNT && Utils.hasUnauthorized(throwable)) {
                    tokenModel.tokenGenerator()
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
                                    prefser.put(GUEST_TOKEN_KEY, notification.getValue().getToken());
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
