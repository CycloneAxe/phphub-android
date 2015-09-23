package org.phphub.app.common.qualified;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        ClickType.CLICK_TYPE_USER_CLICKED,
        ClickType.CLICK_TYPE_TOPIC_CLICKED
})
public @interface ClickType {
    int CLICK_TYPE_USER_CLICKED = 1000;

    int CLICK_TYPE_TOPIC_CLICKED = 1001;
}