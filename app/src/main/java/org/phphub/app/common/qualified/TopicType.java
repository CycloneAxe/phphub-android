package org.phphub.app.common.qualified;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        TopicType.TOPIC_TYPE_RECENT,
        TopicType.TOPIC_TYPE_VOTE,
        TopicType.TOPIC_TYPE_NOBODY
})
public @interface TopicType {
    String TOPIC_TYPE_RECENT = "recent";

    String TOPIC_TYPE_VOTE = "vote";

    String TOPIC_TYPE_NOBODY = "bobody";
}
