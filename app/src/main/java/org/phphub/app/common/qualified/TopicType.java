package org.phphub.app.common.qualified;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.phphub.app.common.qualified.TopicType.*;

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
