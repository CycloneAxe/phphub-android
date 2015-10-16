package org.estgroup.phphub.common.base;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.App;
import org.estgroup.phphub.common.Navigator;
import org.estgroup.phphub.common.internal.di.component.ApiComponent;
import org.estgroup.phphub.common.internal.di.component.AppComponent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusSupportFragment;

public abstract class BaseSupportFragment<PresenterType extends Presenter> extends NucleusSupportFragment<PresenterType> {
    @Nullable
    @Bind(R.id.toolbar)
    public Toolbar toolbarView;

    @Nullable
    @Bind(R.id.toolbar_title)
    public TextView toolbarTitleView;

    public Navigator navigator;

    @Inject
    AccountManager accountManager;

    String accountType, tokenType;

    Account[] accounts;

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Icepick.saveInstanceState(this, bundle);
    }

    @Override
    public void onCreate(Bundle bundle) {
        injectorPresenter();
        super.onCreate(bundle);
        navigator = getAppComponent().navigator();
    }

    protected boolean isLogin() {
        accountManager = AccountManager.get(getActivity());
        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);

        if (accounts.length > 0) {
            return true;
        }

        return false;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this, view);

        if (toolbarTitleView != null &&!TextUtils.isEmpty(getTitle())) {
            toolbarTitleView.setText(getTitle());
        }
    }

    protected String getTitle() {
        return "";
    }

    protected AppComponent getAppComponent() {
        return ((App) getActivity().getApplication()).getAppComponent();
    }

    protected ApiComponent getApiComponent() {
        return ((App) getActivity().getApplication()).getApiComponent();
    }

    protected void injectorPresenter() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}