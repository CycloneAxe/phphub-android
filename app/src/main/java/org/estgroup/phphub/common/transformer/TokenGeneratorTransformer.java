package org.estgroup.phphub.common.transformer;

import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;

import org.estgroup.phphub.api.entity.element.Token;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.TokenModel;

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
                                    needRetry[0] = true;
                                }
                            });
                }

                return needRetry[0];
            }
        });
    }
}
