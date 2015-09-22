package org.phphub.app.common.base;

import android.content.Context;

import org.phphub.app.api.BaseApi;


import eu.unicate.retroauth.AuthRestAdapter;
import eu.unicate.retroauth.interceptors.TokenInterceptor;

public class BaseModel<T> {
    protected T service;

    protected AuthRestAdapter restAdapter;

    public BaseModel(Context context, Class<T> serviceClass) {
        this.restAdapter = new AuthRestAdapter.Builder()
                            .setEndpoint(BaseApi.BASE_URL)
                            .build();

        this.service = restAdapter.create(context, TokenInterceptor.BEARER_TOKENINTERCEPTOR, serviceClass);
    }

    public T getService() {
        return service;
    }
}
