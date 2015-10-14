package org.phphub.app.api;

import android.text.TextUtils;

import org.phphub.app.BuildConfig;

import retrofit.RequestInterceptor;

public class RequestInterceptorImpl implements RequestInterceptor {
    private boolean ignoreToken = false;

    private String token;

    private ThreadLocal<String> localToken = new ThreadLocal<>();

    private String getToken() {
        String token = localToken.get();
        if (token != null) {
            localToken.remove();
            return token;
        }
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void ignoreToken(boolean ignoreToken) {
        this.ignoreToken = ignoreToken;
    }

    public RequestInterceptorImpl local(String token) {
        if (!TextUtils.isEmpty(token)) {
            localToken.set(token);
        }
        return this;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/vnd.PHPHub.v1+json");
        request.addHeader("X-Client-Platform", "Android");
        request.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
        request.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
        request.addHeader("X-Client-Git-Sha", BuildConfig.GIT_SHA);

        if (!ignoreToken) {
            String token = getToken();
            if (!TextUtils.isEmpty(token)) {
                request.addHeader("Authorization", "Bearer " + token);
            }
        }
    }
}
