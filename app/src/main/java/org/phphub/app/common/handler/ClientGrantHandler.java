package org.phphub.app.common.handler;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.phphub.app.model.TokenModel;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClientGrantHandler implements ErrorHandler {
    private Context context;

    public ClientGrantHandler(Context context) {
        this.context = context;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        Logger.e("aaa error %s", cause.getBody().toString());
        if (r != null && r.getStatus() == 401) {
            Logger.e("client grant 401 unauthorizedException");
            TokenModel tokenModel = new TokenModel(context.getApplicationContext());
            tokenModel.tokenGenerator();
        }
        return cause;
    }
}