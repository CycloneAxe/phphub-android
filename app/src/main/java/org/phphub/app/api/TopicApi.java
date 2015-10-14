package org.phphub.app.api;

import com.google.gson.JsonObject;

import org.phphub.app.api.entity.TopicEntity;

import java.util.Map;

import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

public interface TopicApi {
    @GET("/topics/{topicId}")
    Observable<TopicEntity.ATopic> getTopic(@Path("topicId") int topicId,
                                      @QueryMap Map<String, String> options);

    @GET("/topics")
    Observable<TopicEntity> getTopics(@QueryMap Map<String, String> options);

    @POST("/topics/{topicId}/favorite")
    Observable<JsonObject> isFavorite(@Path("topicId") int topicId);

    @DELETE("/topics/{topicId}/favorite")
    Observable<JsonObject> delFavorite(@Path("topicId") int topicId);

    @POST("/topics/{topicId}/attention")
    Observable<JsonObject> isFollow(@Path("topicId") int topicId);

    @DELETE("/topics/{topicId}/attention")
    Observable<JsonObject> delFollow(@Path("topicId") int topicId);

}