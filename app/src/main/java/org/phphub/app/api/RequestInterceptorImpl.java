package org.phphub.app.api;

import android.text.TextUtils;

import org.phphub.app.BuildConfig;
import org.phphub.app.common.qualifier.AuthType;

import retrofit.RequestInterceptor;

public class RequestInterceptorImpl implements RequestInterceptor {
    private String token;

    private @AuthType String authType;

    private boolean ignore;

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/vnd.PHPHub.v1+json");
        request.addHeader("X-Client-Platform", "Android");
        request.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
        request.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
        request.addHeader("X-Client-Git-Sha", BuildConfig.GIT_SHA);

        if (!ignore && !TextUtils.isEmpty(token)) {
            request.addHeader("Authorization", "Bearer " + token);
        }
    }
}
