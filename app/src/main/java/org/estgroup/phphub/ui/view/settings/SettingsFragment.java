package org.estgroup.phphub.ui.view.settings;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.App;

import javax.inject.Inject;

import eu.unicate.retroauth.AuthAccountManager;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String LOGOUT_KEY = "logout";

    @Inject
    AccountManager accountManager;

    @Inject
    AuthAccountManager authAccountManager;

    Account[] accounts;

    String accountType, tokenType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);
        if (accounts.length > 0) {
            Preference logoutPreference = new Preference(getActivity());
            logoutPreference.setKey(LOGOUT_KEY);
            logoutPreference.setLayoutResource(R.layout.common_logout);
            logoutPreference.setOnPreferenceClickListener(this);
            getPreferenceScreen().addPreference(logoutPreference);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case LOGOUT_KEY:
                accountManager.removeAccount(accounts[0], null, null);
                getPreferenceScreen().removePreference(findPreference(LOGOUT_KEY));
                return true;
        }
        return false;
    }
}
