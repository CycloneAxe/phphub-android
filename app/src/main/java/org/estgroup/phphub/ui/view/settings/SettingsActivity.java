package org.estgroup.phphub.ui.view.settings;

import android.content.Context;
import android.content.Intent;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;

public class SettingsActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.settings;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.settings);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
