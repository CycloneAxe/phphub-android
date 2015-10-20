package org.estgroup.phphub.api;

import android.text.TextUtils;

import org.estgroup.phphub.BuildConfig;

import retrofit.RequestInterceptor;

public class RequestInterceptorImpl implements RequestInterceptor {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/vnd.PHPHub.v1+json");
        request.addHeader("X-Client-Platform", "Android");
        request.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
        request.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
        request.addHeader("X-Client-Git-Sha", BuildConfig.GIT_SHA);

        if (!TextUtils.isEmpty(token)) {
            request.addHeader("Authorization", "Bearer " + token);
        }
    }
}
