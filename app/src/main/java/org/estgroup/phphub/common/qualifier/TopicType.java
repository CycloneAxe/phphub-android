package org.estgroup.phphub.common.qualifier;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.estgroup.phphub.common.qualifier.TopicType.*;

@Retention(SOURCE)
@StringDef({
        TOPIC_TYPE_RECENT,
        TOPIC_TYPE_VOTE,
        TOPIC_TYPE_NOBODY,
        TOPIC_TYPE_JOBS
})
public @interface TopicType {
    String TOPIC_TYPE_RECENT = "recent";

    String TOPIC_TYPE_VOTE = "vote";

    String TOPIC_TYPE_NOBODY = "bobody";

    String TOPIC_TYPE_JOBS = "jobs";
}
