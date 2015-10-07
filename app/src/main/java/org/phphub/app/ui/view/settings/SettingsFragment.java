package org.phphub.app.ui.view.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.phphub.app.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);
    }
}
