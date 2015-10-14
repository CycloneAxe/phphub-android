package org.phphub.app.common.transformer;

import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.api.entity.element.Token;
import org.phphub.app.common.util.ApiUtils;
import org.phphub.app.model.TokenModel;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static org.phphub.app.common.Constant.*;

public class TokenGeneratorTransformer<T> implements Observable.Transformer<T, T> {
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

                if (retryCount == 1 && ApiUtils.hasUnauthorized(throwable)) {
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
                                    ApiUtils.asRequestInterceptor()
                                            .setToken(token.getToken());
                                    needRetry[0] = true;
                                }
                            });
                }

                return needRetry[0];
            }
        });
    }
}
