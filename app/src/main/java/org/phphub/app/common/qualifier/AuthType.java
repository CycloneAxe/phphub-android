package org.phphub.app.common.qualifier;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.phphub.app.common.qualifier.AuthType.*;

@Retention(SOURCE)
@StringDef({
        AUTH_TYPE_GUEST,
        AUTH_TYPE_USER,
        AUTH_TYPE_REFRESH
})
public @interface AuthType {
    String AUTH_TYPE_GUEST = "client_credentials";

    String AUTH_TYPE_USER = "login_token";

    String AUTH_TYPE_REFRESH = "refresh_token";
}
