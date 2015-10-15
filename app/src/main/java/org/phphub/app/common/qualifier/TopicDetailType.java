package org.phphub.app.common.qualifier;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.phphub.app.common.qualifier.TopicDetailType.*;

@Retention(SOURCE)
@StringDef({
        TOPIC_DETAIL_TYPE_FAVORITE,
        TOPIC_DETAIL_TYPE_FAVORITE_DEL,
        TOPIC_DETAIL_TYPE_FOLLOW,
        TOPIC_DETAIL_TYPE_FOLLOW_DEL,
        TOPIC_DETAIL_TYPE_VOTE_UP,
        TOPIC_DETAIL_TYPE_VOTE_DOWN
})
public @interface TopicDetailType {

    String TOPIC_DETAIL_TYPE_FAVORITE = "favorite";

    String TOPIC_DETAIL_TYPE_FAVORITE_DEL = "delete_favorite";

    String TOPIC_DETAIL_TYPE_FOLLOW = "follow";

    String TOPIC_DETAIL_TYPE_FOLLOW_DEL = "delete_follow";

    String TOPIC_DETAIL_TYPE_VOTE_UP = "vote_up";

    String TOPIC_DETAIL_TYPE_VOTE_DOWN = "vote_down";
}
