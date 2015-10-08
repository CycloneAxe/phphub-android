package org.phphub.app.common.qualifier;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static org.phphub.app.common.qualifier.AuthType.*;

@Retention(SOURCE)
@StringDef({
        AUTH_TYPE_CLIENT,
        AUTH_TYPE_PASSWORD
})
public @interface AuthType {
    String AUTH_TYPE_CLIENT = "client_credentials";

    String AUTH_TYPE_PASSWORD = "login_token";
}
