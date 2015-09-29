package org.phphub.app.common.service;

import android.content.Context;

import org.phphub.app.R;

import eu.unicate.retroauth.AuthenticationService;

public class AuthService extends AuthenticationService {
    @Override
    public String getLoginAction(Context context) {
        return context.getString(R.string.authentication_action);
    }
}