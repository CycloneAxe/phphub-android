package org.estgroup.phphub.common.base;

import android.support.annotation.NonNull;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.api.RequestInterceptorImpl;
import org.estgroup.phphub.common.provider.TokenProvider;

import retrofit.RestAdapter;

public class BaseModel<T> {
    private T service;

    private TokenProvider tokenProvider;

    protected Class<T> serviceClass;

    protected RestAdapter restAdapter;

    private RequestInterceptorImpl requestInterceptor;

    private ThreadLocal<RequestInterceptorImpl> localRequestInterceptor = new ThreadLocal<>();

    public BaseModel(@NonNull TokenProvider tokenProvider, Class<T> serviceClass) {
        this.serviceClass = serviceClass;
        this.tokenProvider = tokenProvider;
        requestInterceptor = new RequestInterceptorImpl();

        this.restAdapter = new RestAdapter.Builder()
                            .setEndpoint(BuildConfig.ENDPOINT)
                            .setRequestInterceptor(requestInterceptor)
                            .build();

        this.service = restAdapter.create(serviceClass);
    }

    public RequestInterceptorImpl asRequestInterceptor() {
        return requestInterceptor;
    }

    public BaseModel<T> once() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor == null) {
            localRequestInterceptor.set(new RequestInterceptorImpl());
        }
        return this;
    }

    public BaseModel<T> setToken(String token) {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            requestInterceptor.setToken(token);
        }
        return this;
    }

    public BaseModel<T> ignoreToken() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            requestInterceptor.setToken(null);
        }
        return this;
    }

    private String getToken() {
        RequestInterceptorImpl requestInterceptor = localRequestInterceptor.get();
        if (requestInterceptor != null) {
            return requestInterceptor.getToken();
        }

        return tokenProvider.getToken();
    }

    public T getService() {
        asRequestInterceptor().setToken(getToken());
        if (localRequestInterceptor.get() != null) {
            localRequestInterceptor.remove();
        }
        return service;
    }
}