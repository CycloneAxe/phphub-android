package org.phphub.app.common.base;

import android.content.Context;
import android.text.TextUtils;


import com.github.pwittchen.prefser.library.Prefser;

import org.phphub.app.BuildConfig;
import static org.phphub.app.common.Constant.*;


import eu.unicate.retroauth.AuthRestAdapter;
import eu.unicate.retroauth.interceptors.TokenInterceptor;
import retrofit.RequestInterceptor;

public class BaseModel<T> {
    protected T service;

    protected AuthRestAdapter restAdapter;

    public BaseModel(Context context, Class<T> serviceClass) {
        final Prefser prefser = new Prefser(context);
        this.restAdapter = new AuthRestAdapter.Builder()
                            .setEndpoint(BuildConfig.ENDPOINT)
                            .setRequestInterceptor(new RequestInterceptor() {
                                @Override
                                public void intercept(RequestFacade request) {
                                    String guestToken = prefser.get(GUEST_TOKEN, String.class, "");
                                    request.addHeader("Accept", "application/vnd.PHPHub.v1+json");
                                    if (!TextUtils.isEmpty(guestToken)) {
                                        request.addHeader("Authorization", "Bearer " + guestToken);
                                    }

                                    request.addHeader("X-Client-Platform", "Android");
                                    request.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
                                    request.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
                                    request.addHeader("X-Client-Git-Sha", BuildConfig.GIT_SHA);
                                }
                            })
                            .build();

        this.service = restAdapter.create(context, TokenInterceptor.BEARER_TOKENINTERCEPTOR, serviceClass);
    }

    public T getService() {
        return service;
    }
}