package org.phphub.app.common.base;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.pwittchen.prefser.library.Prefser;
import com.levelmoney.velodrome.Velodrome;

import org.phphub.app.R;
import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;
import org.phphub.app.common.internal.di.component.ApiComponent;
import org.phphub.app.common.internal.di.component.AppComponent;
import org.phphub.app.common.internal.di.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.unicate.retroauth.AuthAccountManager;
import icepick.Icepick;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static org.phphub.app.common.Constant.GUEST_TOKEN_KEY;

public abstract class BaseActivity<PresenterType extends Presenter> extends NucleusAppCompatActivity<PresenterType> {
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Nullable
    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectorPresenter();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(getLayoutResId());
        initializeToolbar();
        navigator = getAppComponent().navigator();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isChild()) {
            onTitleChanged(getTitleName(), getTitleColor());
        }
    }


    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolbarTitleView == null) {
            return;
        }
        toolbarTitleView.setText(title);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    protected void initializeToolbar() {
        if (toolbarView == null) {
            return;
        }
        setSupportActionBar(toolbarView);
        if (toolbarTitleView != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (!TextUtils.isEmpty(NavUtils.getParentActivityName(this))) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
        }
    }

    protected Map<String, String> getHttpHeaderAuth() {
        Map<String, String> map = new HashMap<>();
        final String[] token = {""};

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.auth_account_type));
        final AuthAccountManager authAccountManager = new AuthAccountManager(this);
        if (accounts.length > 0) {
            final Account account = accounts[0];
            final String accountType = getString(R.string.auth_account_type),
                    tokenType = getString(R.string.auth_token_type);

            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext(authAccountManager.getAuthToken(account, accountType, tokenType));
                    subscriber.onCompleted();
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
        }

        if (TextUtils.isEmpty(token[0])) {
            Prefser prefser = new Prefser(this);
            token[0] = prefser.get(GUEST_TOKEN_KEY, String.class, "");
        }

        map.put("Authorization", "Bearer " + token[0]);

        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Velodrome.handleResult(this, requestCode, resultCode, data);
    }

    protected CharSequence getTitleName() {
        return getTitle();
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    protected ApiComponent getApiComponent() {
        return ((App) getApplication()).getApiComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected void injectorPresenter() {}

    abstract protected @LayoutRes int getLayoutResId();
}