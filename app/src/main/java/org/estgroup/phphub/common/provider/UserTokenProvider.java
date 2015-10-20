package org.estgroup.phphub.common.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;

import org.estgroup.phphub.R;

import eu.unicate.retroauth.AuthAccountManager;

public class UserTokenProvider implements TokenProvider {
    private Context context;

    private AuthAccountManager authAccountManager;

    private AccountManager accountManager;

    private Account[] accounts;

    private String accountType, tokenType;

    public UserTokenProvider(Context context, AccountManager accountManager, AuthAccountManager authAccountManager) {
        this.context = context;
        this.accountManager = accountManager;
        this.authAccountManager = authAccountManager;

        this.accountType = context.getString(R.string.auth_account_type);
        this.tokenType = context.getString(R.string.auth_token_type);
        this.accounts = accountManager.getAccountsByType(accountType);
    }

    @Override
    public String getToken() {
        if (accounts.length <= 0) {
            Activity activity = (context instanceof Activity) ? (Activity) context : null;
            accountManager.addAccount(accountType, tokenType, null, null, activity, null, null);
            return null;
        }
        return authAccountManager.getAuthToken(accounts[0], accountType, tokenType);
    }
}
