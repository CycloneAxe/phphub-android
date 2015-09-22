package org.phphub.app.ui;

import android.os.Bundle;

import org.phphub.app.R;

import butterknife.ButterKnife;
import eu.unicate.retroauth.AuthenticationActivity;

public class LoginActivity extends AuthenticationActivity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
    }
}
