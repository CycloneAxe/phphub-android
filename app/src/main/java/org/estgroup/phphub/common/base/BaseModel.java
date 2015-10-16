package org.estgroup.phphub.common.base;

import android.content.Context;
import android.text.TextUtils;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.common.util.ApiUtils;

import eu.unicate.retroauth.AuthRestAdapter;
import eu.unicate.retroauth.interceptors.TokenInterceptor;

public class BaseModel<T> {
    private T service;

    protected Class<T> serviceClass;

    protected AuthRestAdapter authRestAdapter;

    protected boolean ignoreToken = false;

    protected ThreadLocal<Boolean> localIgnoreToken = new ThreadLocal<>();

    public BaseModel(Context context, Class<T> serviceClass) {
        this.serviceClass = serviceClass;

        this.authRestAdapter = new AuthRestAdapter.Builder()
                            .setEndpoint(BuildConfig.ENDPOINT)
                            .setRequestInterceptor(ApiUtils.asRequestInterceptor())
                            .build();

        this.service = authRestAdapter.create(context, TokenInterceptor.BEARER_TOKENINTERCEPTOR, serviceClass);
    }

    public void ignoreToken(boolean ignoreToken) {
        this.ignoreToken = ignoreToken;
    }

    public BaseModel<T> local(boolean ignoreToken) {
        return local(ignoreToken, null);
    }

    public BaseModel<T> local(String token) {
        return local(this.ignoreToken, token);
    }

    public BaseModel<T> local(boolean ignoreToken, String token) {
        localIgnoreToken.set(ignoreToken);
        if (!TextUtils.isEmpty(token)) {
            ApiUtils.asRequestInterceptor()
                    .local(token);
        }
        return this;
    }

    public T getService() {
        ApiUtils.asRequestInterceptor().ignoreToken(ignoreToken);
        return service;
    }
}