package org.phphub.app.ui.view.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.phphub.app.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
