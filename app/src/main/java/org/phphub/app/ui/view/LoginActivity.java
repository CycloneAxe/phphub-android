package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.phphub.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.unicate.retroauth.AuthenticationActivity;

public class LoginActivity extends AuthenticationActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        initializeToolbar();
    }

    protected void initializeToolbar() {
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleView.setText(R.string.please_login);
    }
}
