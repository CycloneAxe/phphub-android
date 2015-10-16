package org.estgroup.phphub.common.service;

import android.content.Context;

import org.estgroup.phphub.R;

import eu.unicate.retroauth.AuthenticationService;

public class AuthService extends AuthenticationService {
    @Override
    public String getLoginAction(Context context) {
        return context.getString(R.string.authentication_action);
    }
}