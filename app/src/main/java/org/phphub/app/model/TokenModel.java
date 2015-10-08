package org.phphub.app.model;

import android.content.Context;

import org.phphub.app.BuildConfig;
import org.phphub.app.api.TokenApi;
import org.phphub.app.api.entity.element.Token;
import org.phphub.app.common.base.BaseModel;

import rx.Observable;
import static org.phphub.app.common.qualifier.AuthType.*;

public class TokenModel extends BaseModel<TokenApi> {
    public TokenModel(Context context) {
        super(context, TokenApi.class);
    }

    public Observable<Token> tokenGenerator() {
        return service.getToken(AUTH_TYPE_CLIENT, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET);
    }

    public Observable<Token> tokenGenerator(String username, String loginToken) {
        return service.getToken(AUTH_TYPE_PASSWORD,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                username,
                loginToken);
    }
}
