package org.phphub.app.api;

import org.phphub.app.R;
import org.phphub.app.api.entity.NotificationEntity;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.api.entity.UserEntity;

import java.util.Map;

import eu.unicate.retroauth.annotations.Authenticated;
import eu.unicate.retroauth.annotations.Authentication;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

@Authentication(accountType = R.string.auth_account_type, tokenType = R.string.auth_token_type)
public interface UserApi {

    @Authenticated
    @GET("/me")
    Observable<UserEntity.AUser> getMyselfInfo();

    @GET("/users/{userId}")
    Observable<UserEntity.AUser> getUserInfo(@Path("userId") int userId);

    @Authenticated
    @GET("/user/{userId}/attention/topics")
    Observable<TopicEntity> getMyAttentions(@Path("userId") int userId);

    @Authenticated
    @GET("/user/{userId}/favorite/topics")
    Observable<TopicEntity> getMyFavorites(@Path("userId") int userId);

    @Authenticated
    @GET("/user/{userId}/topics")
    Observable<TopicEntity> getMyTopics(@Path("userId") int userId);

    @Authenticated
    @GET("/me/notifications")
    Observable<NotificationEntity> getMyNotifications(@QueryMap Map<String, String> options);
}
