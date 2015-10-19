package org.estgroup.phphub.common.qualifier;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.estgroup.phphub.common.qualifier.UserTopicType.*;

@Retention(SOURCE)
@StringDef({
        USER_TOPIC_TYPE,
        USER_TOPIC_FOLLOW_TYPE,
        USER_TOPIC_FAVORITE_TYPE
})
public @interface UserTopicType {
    String USER_TOPIC_TYPE = "user_topic_type";

    String USER_TOPIC_FOLLOW_TYPE = "user_topic_follow_type";

    String USER_TOPIC_FAVORITE_TYPE = "user_topic_favorite_type";

}
