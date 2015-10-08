package org.phphub.app.common.qualifier;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.phphub.app.common.qualifier.MainTabType.*;

@Retention(SOURCE)
@StringDef({
        MAIN_TAB_TYPE_RECOMMENDED,
        MAIN_TAB_TYPE_TOPICS,
        MAIN_TAB_TYPE_WIKI,
        MAIN_TAB_TYPE_ME
})
public @interface MainTabType {
    String MAIN_TAB_TYPE_RECOMMENDED = "recommended";

    String MAIN_TAB_TYPE_TOPICS = "topics";

    String MAIN_TAB_TYPE_WIKI = "wiki";

    String MAIN_TAB_TYPE_ME = "me";
}
