package org.phphub.app.common.util;

import org.phphub.app.api.RequestInterceptorImpl;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApiUtils {

    private static RequestInterceptorImpl requestInterceptor;

    static {
        requestInterceptor = new RequestInterceptorImpl();
    }

    public static boolean hasUnauthorized(Throwable throwable) {
        if (!(throwable instanceof RetrofitError)) {
            return false;
        }

        Response r = ((RetrofitError) throwable).getResponse();
        return r != null && r.getStatus() == 401;
    }

    public static RequestInterceptorImpl asRequestInterceptor() {
        return requestInterceptor;
    }
}
