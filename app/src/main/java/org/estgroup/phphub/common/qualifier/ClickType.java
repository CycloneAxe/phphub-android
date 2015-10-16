package org.estgroup.phphub.common.qualifier;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.estgroup.phphub.common.qualifier.ClickType.*;

@Retention(SOURCE)
@IntDef({
        CLICK_TYPE_USER_CLICKED,
        CLICK_TYPE_TOPIC_CLICKED
})
public @interface ClickType {
    int CLICK_TYPE_USER_CLICKED = 1000;

    int CLICK_TYPE_TOPIC_CLICKED = 1001;
}