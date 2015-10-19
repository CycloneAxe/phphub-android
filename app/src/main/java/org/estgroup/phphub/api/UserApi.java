package org.estgroup.phphub.api;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.TopicEntity;
import org.estgroup.phphub.api.entity.UserEntity;

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

    @GET("/user/{userId}/attention/topics")
    Observable<TopicEntity> getAttentions(@Path("userId") int userId,
                                          @QueryMap Map<String, String> options);

    @GET("/user/{userId}/favorite/topics")
    Observable<TopicEntity> getFavorites(@Path("userId") int userId,
                                         @QueryMap Map<String, String> options);

    @GET("/user/{userId}/topics")
    Observable<TopicEntity> getTopics(@Path("userId") int userId,
                                      @QueryMap Map<String, String> options);

    @Authenticated
    @GET("/me/notifications")
    Observable<NotificationEntity> getMyNotifications(@QueryMap Map<String, String> options);
}
