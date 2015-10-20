package org.estgroup.phphub.ui.view.settings;

import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.App;
import org.estgroup.phphub.common.util.Utils;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String LOGOUT_KEY = "logout";

    @Inject
    AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.logined(getActivity(), accountManager)) {
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
                new AlertDialog.Builder(getActivity())
                        .setMessage("确认退出吗？")
                        .setCancelable(false
                        )
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accountManager.removeAccount(Utils.getAccounts(getActivity(), accountManager)[0], null, null);
                                getPreferenceScreen().removePreference(findPreference(LOGOUT_KEY));
                            }
                        })
                        .setNegativeButton("容我想想", null)
                        .show();
                return true;
        }
        return false;
    }
}
