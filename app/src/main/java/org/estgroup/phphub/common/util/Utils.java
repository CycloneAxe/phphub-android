package org.estgroup.phphub.common.util;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class Utils {

    public static boolean hasUnauthorized(Throwable throwable) {
        if (!(throwable instanceof RetrofitError)) {
            return false;
        }

        Response r = ((RetrofitError) throwable).getResponse();
        return r != null && r.getStatus() == 401;
    }
}
