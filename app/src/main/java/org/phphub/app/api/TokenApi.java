package org.phphub.app.api;

import org.phphub.app.api.entity.element.Token;
import org.phphub.app.common.qualifier.AuthType;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

public interface TokenApi {
    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<Token> getToken(@Field("grant_type") @AuthType String authType,
                               @Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<Token> getToken(@Field("grant_type") @AuthType String authType,
                               @Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret,
                               @Field("username") String username,
                               @Field("login_token") String loginToken);
}