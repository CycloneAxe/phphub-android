package org.estgroup.phphub.common.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import org.estgroup.phphub.R;

import eu.unicate.retroauth.AuthAccountManager;
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

    public static String getTokenType(Context context) {
        return context.getString(R.string.auth_token_type);
    }

    public static String getAccountType(Context context) {
        return context.getString(R.string.auth_account_type);
    }

    public static String getActiveAccountName(Context context, AuthAccountManager authAccountManager) {
        return authAccountManager.getActiveAccountName(getAccountType(context), false);
    }

    public static Account getActiveAccount(Context context, AuthAccountManager authAccountManager) {
        return authAccountManager.getAccountByName(
                getActiveAccountName(context, authAccountManager),
                getAccountType(context)
        );
    }

    public static Account[] getAccounts(Context context, AccountManager accountManager) {
        return accountManager.getAccountsByType(getAccountType(context));
    }

    public static boolean hasLoggedIn(Context context, AccountManager accountManager) {
        Account[] accounts = getAccounts(context, accountManager);
        return accounts.length > 0;
    }
}