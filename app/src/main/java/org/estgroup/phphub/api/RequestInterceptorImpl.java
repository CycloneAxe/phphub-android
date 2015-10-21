package org.estgroup.phphub.api;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.common.provider.TokenProvider;

import retrofit.RequestInterceptor;

public class RequestInterceptorImpl implements RequestInterceptor {
    @Nullable
    private TokenProvider tokenProvider;

    @Nullable
    public TokenProvider getTokenProvider() {
        return tokenProvider;
    }

    public void setTokenProvider(@Nullable TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private String getToken() {
        if (tokenProvider != null) {
            return tokenProvider.getToken();
        }
        return null;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/vnd.PHPHub.v1+json");
        request.addHeader("X-Client-Platform", "Android");
        request.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
        request.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
        request.addHeader("X-Client-Git-Sha", BuildConfig.GIT_SHA);

        if (tokenProvider != null) {
            String token = getToken();
            if (!TextUtils.isEmpty(token)) {
                request.addHeader("Authorization", "Bearer " + token);
            }
        }
    }
}
