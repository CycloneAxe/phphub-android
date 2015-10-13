package org.phphub.app.api;

import org.phphub.app.R;
import org.phphub.app.api.entity.UserEntity;

import eu.unicate.retroauth.annotations.Authenticated;
import eu.unicate.retroauth.annotations.Authentication;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

@Authentication(accountType = R.string.auth_account_type, tokenType = R.string.auth_token_type)
public interface UserApi {

    @Authenticated
    @GET("/me")
    Observable<UserEntity.AUser> getMyselfInfo();

    @GET("/users/{userId}")
    Observable<UserEntity.AUser> getUserInfo(@Path("userId") int userId);
}
