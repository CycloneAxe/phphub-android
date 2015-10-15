package org.phphub.app.model;

import android.content.Context;

import com.google.gson.JsonObject;

import org.phphub.app.api.TopicApi;
import org.phphub.app.api.entity.TopicEntity;
import org.phphub.app.common.Constant;
import org.phphub.app.common.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class TopicModel extends BaseModel<TopicApi> {
    public TopicModel(Context context) {
        super(context, TopicApi.class);
    }

    Observable<TopicEntity> getTopics(String filter, int pageIndex) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("include", "user,node,last_reply_user");
        options.put("per_page", String.valueOf(Constant.PER_PAGE));
        options.put("filter", filter);
        options.put("page", String.valueOf(pageIndex));

        return getService().getTopics(options);
    }

    public Observable<TopicEntity> getTopicsByExcellent(int pageIndex) {
        return getTopics("excellent", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByRecent(int pageIndex) {
        return getTopics("newest", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByVote(int pageIndex) {
        return getTopics("vote", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByNobody(int pageIndex) {
        return getTopics("nobody", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByWiki(int pageIndex) {
        return getTopics("wiki", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByJobs(int pageIndex) {
        return getTopics("jobs", pageIndex);
    }

    public Observable<TopicEntity.ATopic> getTopicDetailById(int topicId) {
        Map<String, String> options = new HashMap<>();
        options.put("include", "user,node");
        options.put("columns", "user(signature)");

        return getService().getTopic(topicId, options);
    }

    public Observable<JsonObject> isFavorite(int topicId) {
        return getService().isFavorite(topicId);
    }

    public Observable<JsonObject> delFavorite(int topicId) {
        return getService().delFavorite(topicId);
    }

    public Observable<JsonObject> isFollow(int topicId) {
        return getService().isFollow(topicId);
    }

    public Observable<JsonObject> delFollow(int topicId) {
        return getService().delFollow(topicId);
    }

    public Observable<JsonObject> voteUp(int topicId) {
        return getService().voteUp(topicId);
    }

    public Observable<JsonObject> voteDown(int topicId) {
        return getService().voteDown(topicId);
    }
}