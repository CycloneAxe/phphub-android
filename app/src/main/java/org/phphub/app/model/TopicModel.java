package org.phphub.app.model;

import android.content.Context;

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

        return service.getTopics(options);
    }

    public Observable<TopicEntity> getTopicsByExcellent(int pageIndex) {
        return getTopics("excellent", pageIndex);
    }

    public Observable<TopicEntity> getTopicsByRecent(int pageIndex) {
        return getTopics("recent", pageIndex);
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
}