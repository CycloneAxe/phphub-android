package org.phphub.app.api;

import org.phphub.app.api.entity.TopicEntity;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;

public interface TopicApi {
    @GET("/topics/{topicId}")
    Observable<TopicEntity.TopicObj> getTopic(@Path("topicId") int topicId,
                                      @QueryMap Map<String, String> options);

    @GET("/topics")
    Observable<TopicEntity> getTopics(@QueryMap Map<String, String> options);
}