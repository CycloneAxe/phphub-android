package org.estgroup.phphub.common.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import org.estgroup.phphub.R;

import eu.unicate.retroauth.AuthAccountManager;

public class UserTokenProvider implements TokenProvider {
    private AuthAccountManager authAccountManager;

    private AccountManager accountManager;

    private Account[] accounts;

    private String accountType, tokenType;

    public UserTokenProvider(Context context, AccountManager accountManager, AuthAccountManager authAccountManager) {
        this.accountManager = accountManager;
        this.authAccountManager = authAccountManager;

        this.accountType = context.getString(R.string.auth_account_type);
        this.tokenType = context.getString(R.string.auth_token_type);
        this.accounts = accountManager.getAccountsByType(accountType);
    }

    @Override
    public String getToken() {
        if (accounts.length > 0) {
            return authAccountManager.getAuthToken(accounts[0], accountType, tokenType);
        }
        return null;
    }
}
