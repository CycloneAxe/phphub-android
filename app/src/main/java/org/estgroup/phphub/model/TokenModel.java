package org.estgroup.phphub.model;

import android.support.annotation.Nullable;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.api.TokenApi;
import org.estgroup.phphub.api.entity.element.Token;
import org.estgroup.phphub.common.base.BaseModel;
import org.estgroup.phphub.common.provider.TokenProvider;

import rx.Observable;
import static org.estgroup.phphub.common.qualifier.AuthType.*;

public class TokenModel extends BaseModel<TokenApi> {
    public TokenModel(@Nullable TokenProvider provider) {
        super(provider, TokenApi.class);
    }

    public Observable<Token> tokenGenerator() {
        return getService().getToken(AUTH_TYPE_GUEST, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET);
    }

    public Observable<Token> tokenGenerator(String username, String loginToken) {
        return getService().getToken(AUTH_TYPE_USER,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                username,
                loginToken);
    }

    public Observable<Token> refreshToken(String refreshToken) {
        return getService().refreshToken(AUTH_TYPE_REFRESH,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                refreshToken);
    }
}
