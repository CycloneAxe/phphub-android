package org.estgroup.phphub.common.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import org.estgroup.phphub.R;

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

    public static Account[] getAccounts(Context context, AccountManager accountManager) {
        String accountType = context.getString(R.string.auth_account_type);
        Account[] accounts = accountManager.getAccountsByType(accountType);
        return accounts;
    }

    public static boolean logined(Context context, AccountManager accountManager) {
        Account[] accounts = getAccounts(context, accountManager);
        return accounts.length > 0;
    }
}
