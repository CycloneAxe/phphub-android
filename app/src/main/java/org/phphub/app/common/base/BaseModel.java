package org.phphub.app.common.base;

import android.content.Context;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.BuildConfig;
import org.phphub.app.api.RequestInterceptorImpl;

import static org.phphub.app.common.Constant.*;

import eu.unicate.retroauth.AuthRestAdapter;
import eu.unicate.retroauth.interceptors.TokenInterceptor;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class BaseModel<T> {
    protected T service;

    protected Class<T> serviceClass;

    protected AuthRestAdapter authRestAdapter;

    protected static RequestInterceptorImpl requestInterceptor;

    static {
        requestInterceptor = new RequestInterceptorImpl();
        requestInterceptor.setIgnore(true);
    }

    public BaseModel(Context context, final boolean injectGuestToken, Class<T> serviceClass) {
        final Prefser prefser = new Prefser(context);
        this.serviceClass = serviceClass;

        if (injectGuestToken) {
            String guestToken = prefser.get(GUEST_TOKEN_KEY, String.class, "");
            if (!TextUtils.isEmpty(guestToken)) {
                requestInterceptor.setToken(guestToken);
                requestInterceptor.setIgnore(false);
            }
        }

        this.authRestAdapter = new AuthRestAdapter.Builder()
                            .setEndpoint(BuildConfig.ENDPOINT)
                            .setRequestInterceptor(requestInterceptor)
                            .build();

        this.service = authRestAdapter.create(context, TokenInterceptor.BEARER_TOKENINTERCEPTOR, serviceClass);
    }

    public static RequestInterceptorImpl getRequestInterceptor() {
        return requestInterceptor;
    }

    public T getService() {
        return service;
    }

    public T getService(RequestInterceptor requestInterceptor) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.ENDPOINT)
                .setRequestInterceptor(requestInterceptor)
                .build();
        return restAdapter.create(serviceClass);
    }
}