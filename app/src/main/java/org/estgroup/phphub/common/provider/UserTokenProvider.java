package org.estgroup.phphub.common.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;

import eu.unicate.retroauth.AuthAccountManager;
import eu.unicate.retroauth.exceptions.AuthenticationCanceledException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserTokenProvider implements TokenProvider {
    private Context context;

    private AuthAccountManager authAccountManager;

    private AccountManager accountManager;

    private String accountType, tokenType;

    public UserTokenProvider(Context context, AccountManager accountManager, AuthAccountManager authAccountManager) {
        this.context = context;
        this.accountManager = accountManager;
        this.authAccountManager = authAccountManager;

        this.accountType = context.getString(R.string.auth_account_type);
        this.tokenType = context.getString(R.string.auth_token_type);
    }

    @Override
    public String getToken() {
        final Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts.length <= 0) {
            Activity activity = (context instanceof Activity) ? (Activity) context : null;
            accountManager.addAccount(accountType, tokenType, null, null, activity, null, null);
            return null;
        }

        final String[] token = {null};
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onNext(authAccountManager.getAuthToken(accounts[0], accountType, tokenType));
                    subscriber.onCompleted();
                } catch (AuthenticationCanceledException e) {
                    subscriber.onError(e);
                }
            }
        })
        .filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String token) {
                return !TextUtils.isEmpty(token);
            }
        })
        .subscribeOn(Schedulers.io())
        .toBlocking()
        .forEach(new Action1<String>() {
            @Override
            public void call(String s) {
                token[0] = s;
            }
        });
        return token[0];
    }
}
