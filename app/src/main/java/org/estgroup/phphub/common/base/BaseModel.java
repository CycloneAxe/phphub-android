package org.estgroup.phphub.common.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.api.RequestInterceptorImpl;
import org.estgroup.phphub.common.provider.TokenProvider;

import retrofit.RestAdapter;

public abstract class BaseModel<T, R extends BaseModel> {
    private T service;

    @Nullable
    private TokenProvider tokenProvider;

    protected RestAdapter restAdapter;

    private RequestInterceptorImpl requestInterceptor;

    private ThreadLocal<RequestInterceptorImpl> localRequestInterceptor = new ThreadLocal<>();

    public BaseModel(@NonNull Context context, @Nullable TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        requestInterceptor = new RequestInterceptorImpl();

        this.restAdapter = new RestAdapter.Builder()
                            .setEndpoint(BuildConfig.ENDPOINT)
                            .setRequestInterceptor(requestInterceptor)
                            .build();

        this.service = restAdapter.create(getServiceClass());
    }

    protected abstract Class<T> getServiceClass();

    public RequestInterceptorImpl asRequestInterceptor() {
        return requestInterceptor;
    }

    public R once() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor == null) {
            localRequestInterceptor.set(new RequestInterceptorImpl());
        }
        return (R) this;
    }

    public R setToken(final String token) {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            requestInterceptor.setTokenProvider(new TokenProvider() {
                @Override
                public String getToken() {
                    return token;
                }
            });
        }
        return (R) this;
    }

    public R ignoreToken() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            requestInterceptor.setTokenProvider(null);
        }
        return (R) this;
    }

    private TokenProvider TokenProvider() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            return requestInterceptor.getTokenProvider();
        }

        return tokenProvider;
    }

    public T getService() {
        asRequestInterceptor().setTokenProvider(TokenProvider());
        if (localRequestInterceptor.get() != null) {
            localRequestInterceptor.remove();
        }
        return service;
    }
}