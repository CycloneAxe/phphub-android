package org.phphub.app.api;

import org.phphub.app.api.entity.TopicEntity;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

public interface TopicApi {
    @GET("/topics")
    Observable<TopicEntity> getTopics(@QueryMap Map<String, String> options);
}